package com.x.network.service;

import com.x.lang.Locker;
import com.x.lang.RunnablePool;
import com.x.network.standalone.ServicePath;
import com.x.network.io.Tickable;
import com.x.logging.Logger;
import com.x.network.ServiceThreadPool;
import com.x.network.io.TickableContainer;
import com.x.network.io.Protocol;
import com.x.network.standalone.ServiceProvider;
import com.x.util.TimeUtils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

/**
 * ActorSystem拥有本地地址和远程地址，如果没有远程地址，那么不能提供远程服务。目前只支持tcp协议。
 *
 * @author yanfengbing
 * @version 1.0
 * @since 6/29/15 4:13 PM
 */
public class ServiceSystem implements Comparable<ServiceSystem>
{
	private static ServiceSystem instance;

	public static ServiceSystem getInstance()
	{
		return instance;
	}

	public static ServiceSystem createInstance(String name, int id, ServiceThreadPool serviceThreadPool)
	{
		if(instance == null)
		{
			synchronized(ServiceSystem.class)
			{
				if(instance == null)
				{
					//Logger.debug(ServiceSystem.class, "Load SystemDataLoader {} and SystemProtocolLoader {}", SystemDataLoader.getInstance(), SystemProtocolLoader.getInstance());
					instance = new ServiceSystem(name, id, serviceThreadPool);
					return instance;
				}
			}
		}
		throw new UnsupportedOperationException("ServiceSystem.createInstance create a singleton instance of ServiceSystem. Your can't call this twice!");
	}

	private final AtomicLong atomicId = new AtomicLong(0);

	protected final String name;
	protected final int id;

	private final ServiceThreadPool serviceThreadPool;
	private final Locker locker;
	private final ServiceCell[] serviceCells;
	private final TickableContainer tickableContainer;

	private boolean started = false;

	private final AtomicLong time = new AtomicLong(0);
	private final AtomicLong deltaTime = new AtomicLong(0);

	private final ConcurrentHashMap<String, ServiceProvider> serviceProviders = new ConcurrentHashMap<>();
	private final Set<ServiceIo> serviceIoSet = new CopyOnWriteArraySet<>();

	private final ConcurrentHashMap<Long, ServicePipe> servicePipes = new ConcurrentHashMap<>();
	private final QuestionDispatcher questionDispatcher = new QuestionDispatcher();

	private final ConcurrentHashMap<String, ServiceSelection> serviceSelections = new ConcurrentHashMap<>();

	protected ServiceSystem(String name, int id, ServiceThreadPool serviceThreadPool)
	{
		this.name = name == null ? "" : name;
		this.id = id & 0x0000FFFF;
		this.serviceThreadPool = serviceThreadPool;
		this.tickableContainer = new TickableContainer(this.serviceThreadPool);
		locker = new Locker(this.serviceThreadPool.getThreadCount());
		serviceCells = new ServiceCell[this.serviceThreadPool.getThreadCount()];
		for(int i = 0; i < this.serviceThreadPool.getThreadCount(); ++i)
		{
			serviceCells[i] = new ServiceCell(this, i + 1);
		}
	}

	public void start() throws Exception
	{
		synchronized(this)
		{
			if(!started)
			{
				time.set(TimeUtils.timeMills());
				serviceThreadPool.scheduleWithFixedDelay((Runnable)() ->
				{
					questionDispatcher.update();
					deltaTime.set(TimeUtils.timeMills() - time.get());
					time.set(time.get() + deltaTime.get());
				}, 1000L, 1000L, TimeUnit.MILLISECONDS);
				for(ServiceProvider serviceProvider : serviceProviders.values())
				{
					serviceProvider.start();
				}
				RunnablePool.executeRunnable(this::recycleServiceCoroutines, 10, 10);
				RunnablePool.executeRunnable(this::recycleServiceWrappers, 10, 10);
				started = true;
				Logger.debug(this.getClass(), "" + toString() + " started!");
			}
		}
	}

	public void stop()
	{
		synchronized(this)
		{
			if(started)
			{
				serviceSelections.clear();
				servicePipes.values().forEach(ServicePipe::close);
				servicePipes.clear();
				serviceProviders.values().forEach(ServiceProvider::stop);
				serviceProviders.clear();
				serviceIoSet.forEach(ServiceIo::stop);
				serviceIoSet.clear();
				serviceThreadPool.shutdown();
				time.set(0);
				started = false;
				Logger.debug(this.getClass(), "" + toString() + " stopped!");
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public boolean isStarted()
	{
		return started;
	}

	public void addServiceProvider(ServiceProvider serviceProvider) throws Exception
	{
		if(serviceProvider == null)
		{
			return;
		}
		if(serviceProviders.containsKey(serviceProvider.getName()))
		{
			throw new Exception("Duplicated ServiceProvider " + serviceProvider);
		}
		synchronized(this)
		{
			if(!serviceIoSet.contains(serviceProvider.getServiceIo()))
			{
				serviceIoSet.add(serviceProvider.getServiceIo());
			}
			serviceProviders.put(serviceProvider.getName(), serviceProvider);
			if(started)
			{
				serviceProvider.start();
			}
		}
	}

	public void removeServiceProvider(String serviceProviderName)
	{
		ServiceProvider serviceProvider = serviceProviders.remove(serviceProviderName);
		if(serviceProvider != null)
		{
			serviceProvider.stop();
		}
	}

	public ServiceProvider getServiceProvider(String serviceProviderName)
	{
		return serviceProviders.get(serviceProviderName);
	}

	public ServiceSelection find(String name)
	{
		ServiceSelection serviceSelection = serviceSelections.get(name);
		if(serviceSelection == null)
		{
			Logger.warn(this.getClass(), "Not found service {}", name);
		}
		return serviceSelection;
	}

	void mark(String name, long pid)
	{
		ServiceSelection serviceSelection = serviceSelections.get(name);
		if(serviceSelection == null)
		{
			synchronized(serviceSelections)
			{
				serviceSelection = serviceSelections.get(name);
				if(serviceSelection == null)
				{
					serviceSelection = new ServiceSelection(this, name);
					serviceSelections.put(name, serviceSelection);
				}
			}
		}
		serviceSelection.mark(pid);
	}

	void unMark(long pid)
	{
		for(ServiceSelection serviceSelection : serviceSelections.values())
		{
			serviceSelection.unMark(pid);
		}
	}

	void executeCell(ServiceCell cell)
	{
		serviceThreadPool.execute(cell);
	}

	public long nextId()
	{
		return ((atomicId.incrementAndGet() << 16) | id);
	}

	public long getTime()
	{
		return time.get();
	}

	void clearQuestionWatchers(long pid)
	{
		questionDispatcher.clear(pid);
	}

	public void executeRunnable(Runnable runnable)
	{
		serviceThreadPool.executeRunnable(runnable);
	}

	public void executeRunnable(Runnable runnable, int waitseconds)
	{
		serviceThreadPool.executeRunnable(runnable, waitseconds);
	}

	public void executeRunnable(Runnable runnable, long milliseconds)
	{
		serviceThreadPool.executeRunnable(runnable, milliseconds);
	}

	public void executeRunnable(Runnable runnable, int waitseconds, int periodSeconds)
	{
		serviceThreadPool.executeRunnable(runnable, waitseconds, periodSeconds);
	}

	public void executeRunnable(Runnable runnable, long milliSeconds, long periodMilliSeconds)
	{
		serviceThreadPool.executeRunnable(runnable, milliSeconds, periodMilliSeconds);
	}

	public void executeRunnable(Runnable runnable, Timestamp date)
	{
		serviceThreadPool.executeRunnable(runnable, date);
	}

	public void executeRunnable(Runnable runnable, Timestamp date, int periodSeconds)
	{
		serviceThreadPool.executeRunnable(runnable, date, periodSeconds);
	}

	public void executeRunnable(Runnable runnable, Timestamp date, long periodMilliSeconds)
	{
		serviceThreadPool.executeRunnable(runnable, date, periodMilliSeconds);
	}

	public void addTickable(Tickable t)
	{
		tickableContainer.addTickable(t);
	}

	public Tickable removeTickable(Tickable t)
	{
		return tickableContainer.removeTickable(t);
	}

	public void addServicePipe(ServicePipe pipe)
	{
		servicePipes.put(pipe.getPid(), pipe);
	}

	public void removeServicePipe(ServicePipe pipe)
	{
		servicePipes.remove(pipe.getPid());
	}

	public boolean dispatchMessage(long pid, Object message)
	{
		return questionDispatcher.dispatch(pid, message);
	}

	protected AskResponseWatcher call(ServiceCoroutine coroutine, long pid, Protocol sendMessage, int timeout)
	{
		return questionDispatcher.call(coroutine, pid, sendMessage, timeout);
	}

	public BlockedAskResponseWatcher call(long pid, Protocol sendMessage, int timeout)
	{
		return questionDispatcher.call(pid, sendMessage, timeout);
	}

	/*protected HttpResponseWatcher call(ServiceCoroutine coroutine, long pid, String url, int timeout)
	{
		HttpResponseWatcher watcher = questionDispatcher.call(coroutine, pid, url, timeout);
		RunnablePool.executeRunnable(() ->
		{
			try
			{
				String re = HttpUtils.doHttpGet(url, timeout);
				questionDispatcher.dispatch(url, watcher.getId(), re);
			}
			catch(IOException e)
			{
				questionDispatcher.dispatch(url, watcher.getId(), e);
			}
		});
		return watcher;
	}

	protected HttpResponseWatcher call(ServiceCoroutine coroutine, long pid, String url, String param, int timeout)
	{
		String key = url + param;
		HttpResponseWatcher watcher = questionDispatcher.call(coroutine, pid, key, timeout);
		RunnablePool.executeRunnable(() ->
		{
			try
			{
				String re = HttpUtils.doHttpPost(url, param, timeout);
				questionDispatcher.dispatch(key, watcher.getId(), re);
			}
			catch(IOException e)
			{
				questionDispatcher.dispatch(key, watcher.getId(), e);
			}
		});
		return watcher;
	}*/

	public ServicePipe getServicePipe(long pid)
	{
		return servicePipes.get(pid);
	}

	public void tell(long pipeId, Object message)
	{
		ServicePipe pipe = servicePipes.get(pipeId);
		if(pipe != null)
		{
			pipe.tell(message);
		}
	}

	public void tell(ServicePath receiver, Object message)
	{
		ServicePipe pipe = servicePipes.get(receiver.getPid());
		if(pipe != null)
		{
			pipe.tell(message);
		}
	}

	public void tell(ServiceSelection selection, Object message)
	{
		if(selection != null)
		{
			selection.tell(message);
		}
	}

	public void close(ServicePath remotePath)
	{
		ServicePipe pipe = servicePipes.get(remotePath.getPid());
		if(pipe != null)
		{
			pipe.close();
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		ServiceSystem that = (ServiceSystem)o;

		return id == that.id;

	}

	@Override
	public int hashCode()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return "ServiceSystem{" + name + ", " + id + '}';
	}

	@Override
	public int compareTo(ServiceSystem o)
	{
		if(o == null) throw new NullPointerException("ServiceSystem compare to null!");
		if(this == o) return 0;
		if(this.equals(o)) return 0;
		int ret = id - o.id;
		if(ret == 0) return name.compareTo(o.name);
		return ret;
	}

	public ServiceCell getServiceCell(long id)
	{
		int i = (int)(Math.abs(id) % (long)serviceCells.length);
		return serviceCells[i];
	}

	private final ConcurrentHashMap<Long, ServiceCoroutine> serviceCoroutineMap = new ConcurrentHashMap<>();

	ServiceCoroutine getServiceCoroutine(long cid)
	{
		Lock lock = locker.getLock(cid);
		try
		{
			lock.lock();
			ServiceCoroutine c = serviceCoroutineMap.get(cid);
			if(c == null)
			{
				c = new ServiceCoroutine(this, cid);
				serviceCoroutineMap.put(cid, c);
			}
			c.setUsing(true);
			return c;
		}
		finally
		{
			lock.unlock();
		}
	}

	public int getServiceCoroutinesCount()
	{
		return serviceCoroutineMap.size();
	}

	private void recycleServiceCoroutines()
	{
		Iterator<Map.Entry<Long, ServiceCoroutine>> iterator = serviceCoroutineMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			ServiceCoroutine c = iterator.next().getValue();
			Lock lock = locker.getLock(c.getCid());
			try
			{
				lock.lock();
				if(c.checkFinishing())
				{
					iterator.remove();
					Logger.debug(this.getClass(), "" + c.toString() + " stopped!");
				}
				Logger.debug(this.getClass(), "{} has {} messages.", c, c.messageBox.size());
			}
			finally
			{
				lock.unlock();
			}
		}
	}

	private class ServiceWrapperWeakReference extends WeakReference<ServiceWrapper>
	{
		private String key;

		public ServiceWrapperWeakReference(ServiceWrapper referent, ReferenceQueue<? super ServiceWrapper> q)
		{
			super(referent, q);
			key = "" + referent.getPid() + "_" + referent.getCid();
		}

		public String getKey()
		{
			return key;
		}
	}

	private final ConcurrentHashMap<String, ServiceWrapperWeakReference> serviceWrapperMap = new ConcurrentHashMap<>();
	private final ReferenceQueue<ServiceWrapper> referenceQueue = new ReferenceQueue<>();

	ServiceWrapper getServiceWrapper(ServiceProvider provider, long pid, ServiceCoroutine c)
	{
		ServiceWrapper wrapper;
		String key = "" + pid + "_" + c.getCid();
		ServiceWrapperWeakReference ref = serviceWrapperMap.get(key);
		if(ref == null || (wrapper = ref.get()) == null)
		{
			synchronized(serviceWrapperMap)
			{
				ref = serviceWrapperMap.get(key);
				if(ref == null || (wrapper = ref.get()) == null)
				{
					wrapper = new ServiceWrapper(this, provider, pid, c);
					ref = new ServiceWrapperWeakReference(wrapper, referenceQueue);
					Logger.debug(this.getClass(), "Create ServiceWrapper {}, {}", ref, key);
					serviceWrapperMap.put(key, ref);
				}
			}
		}
		return wrapper;
	}

	private void recycleServiceWrappers()
	{
		ServiceWrapperWeakReference ref;
		while((ref = (ServiceWrapperWeakReference)referenceQueue.poll()) != null)
		{
			ServiceWrapperWeakReference ref2 = serviceWrapperMap.get(ref.getKey());
			if(ref2 != null)
			{
				if(ref2 == ref)
				{
					synchronized(serviceWrapperMap)
					{
						ref2 = serviceWrapperMap.get(ref.getKey());
						if(ref2 != null)
						{
							if(ref2 == ref)
							{
								Logger.debug(this.getClass(), "Remove ServiceWrapper {}, {}", ref, ref.getKey());
								serviceWrapperMap.remove(ref.getKey());
							}
							else
							{
								Logger.debug(this.getClass(), "New ServiceWrapper, 2 can't remove {}, {}, {}", ref2, ref, ref.getKey());
							}
						}
					}
				}
				else
				{
					Logger.debug(this.getClass(), "New ServiceWrapper, 1 can't remove {}, {}, {}", ref2, ref, ref.getKey());
				}
			}
		}
	}

}
