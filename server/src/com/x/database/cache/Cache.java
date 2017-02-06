package com.x.database.cache;

import com.x.io.OptimisticException;
import com.x.logging.Logger;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1.0版本是NBTA和RMI实现的.
 * 2.0版本是基于自己的网络库实现的.
 *
 * @author yanfengbing
 * @version 2.0
 * @since 2/13/14 2:09 PM
 */
public class Cache<T extends Value>
{
	public static final int MAX_LOCKER_COUNT = 30;

	private static final class Pended<B extends Value>
	{
		protected AtomicBoolean pending = new AtomicBoolean(false);
		protected B value;
		protected long timestamp = -1;
		protected B older;
		protected long older_timestamp = -1;
		private long startTime = 0;

		private Pended(B value, long timestamp)
		{
			this.value = value;
			this.older = value;
			this.timestamp = timestamp;
			this.older_timestamp = timestamp;
		}

		public boolean isPending()
		{
			return pending.get();
		}

		public boolean startPending()
		{
			startTime = System.currentTimeMillis();
			return pending.compareAndSet(false, true);
		}

		public void setPending(boolean pending)
		{
			this.pending.set(pending);
		}

		public void setNewer(B newer, long timestamp)
		{
			this.older = value;
			this.older_timestamp = this.timestamp;
			this.value = newer;
			this.timestamp = timestamp;
		}

		public void revert()
		{
			value = older;
			timestamp = older_timestamp;
			pending.set(false);
		}

		public void commit()
		{
			pending.set(false);
		}
	}

	private static final class Record<B extends Value>
	{
		protected B value;
		protected long timestamp;
		protected boolean dirty = true;

		public Record(B value, long timestamp)
		{
			this.value = value;
			this.timestamp = timestamp;
		}

		public void set(B value, long timestamp)
		{
			this.value = value;
			this.timestamp = timestamp;
			this.dirty = true;
		}

		public boolean isDirty()
		{
			return dirty;
		}

		public void save()
		{
			dirty = false;
		}
	}

	public static final class Bean<B extends Value> implements Serializable
	{
		public B value;
		public long timestamp;

		public Bean(B value, long timestamp)
		{
			this.value = value;
			this.timestamp = timestamp;
		}
	}

	private final Lock[] lockers = new ReentrantLock[MAX_LOCKER_COUNT];
	private final Map<Key, Pended<T>> pendings = new ConcurrentHashMap<>();
	private final Map<Key, Record<T>> goods = new ConcurrentHashMap<>();
	private final ConcurrentSkipListMap<Key, T> savedQueue = new ConcurrentSkipListMap<>();

	private Class<? extends Value> agent = null;
	private Persister<T> persister = null;
	private boolean interrupted = false;

	public Cache(Class<? extends Value> agent, Persister<T> persister)
	{
		this.agent = agent;
		this.persister = persister;
	}

	public void init(int bound, int cacheId)
	{
		if(null != persister)
		{
			Map<Key, T> map = persister.loadAll(bound, cacheId);
			if(null != map)
			{
				for(Map.Entry<Key, T> entry : map.entrySet())
				{
					// 匹配是否是该Cache的数据 (key.hash(index) 从 0 开始，所以需要加一)
					if(entry.getKey().hash(bound) == (cacheId % bound))
					{
						Record<T> record = new Record<>(entry.getValue(), 0);
						record.save();
						goods.put(entry.getKey(), record);
					}
				}
			}
			Logger.debug(this.getClass(), "Cache " + cacheId + " load " + goods.size());
		}

		//加载完成之后 启动 后台线程
		Thread worker = new Worker();
		worker.setDaemon(true);
		worker.setPriority(Thread.MIN_PRIORITY);
		worker.setName("CacheWorkerThread-" + worker.getId());
		worker.start();

		Thread remover = new Remover();
		remover.setDaemon(true);
		remover.setPriority(Thread.MIN_PRIORITY);
		remover.setName("CacheRemoverThread-" + remover.getId());
		remover.start();
	}

	public final Class getAgent()
	{
		return agent;
	}

	public Lock getLocker(Key key)
	{
		int lockerid = Math.abs(key.hashCode());
		lockerid %= MAX_LOCKER_COUNT;
		synchronized(lockers)
		{
			return lockers[lockerid] == null ? lockers[lockerid] = new ReentrantLock() : lockers[lockerid];
		}
	}

	/**
	 * 先从cache中找，找不到从数据库中找。
	 *
	 * @param key key
	 * @return 找到的值，数据库中也没有就是null
	 * @throws java.rmi.RemoteException
	 */
	public Bean<T> get(Key key, long timestamp) throws RemoteException
	{
		Logger.trace(this.getClass(), "cache get key " + key);
		Bean<T> bean = null;
		Lock lock = getLocker(key);
		lock.lock();
		try
		{
			if(timestamp > 0)
			{
				Pended<T> pended = pendings.get(key);
				if(pended != null && pended.timestamp == timestamp)
				{
					bean = new Bean<>(pended.value, pended.timestamp);
				}
				else
				{
					Record<T> record = goods.get(key);
					if(record != null)
						bean = new Bean<>(record.value, record.timestamp);
				}
			}
			if(bean == null)
			{
				Record<T> record = goods.get(key);
				if(record != null)
					bean = new Bean<>(record.value, record.timestamp);
			}
			if(bean == null)
			{
				T value = find(key);
				if(value != null)
				{
					Pended<T> pended = new Pended<>(value, 0);
					pendings.put(key, pended);
					Record<T> record = goods.get(key);
					if(record == null)
					{
						record = new Record<>(value, 0);
						record.save();
						goods.put(key, record);
					}
					else
					{
						record.set(value, 0);
						record.save();
					}
					bean = new Bean<>(record.value, record.timestamp);
				}
			}
			if(bean != null && bean.value != null) bean.value.touch();
		}
		catch(Exception e)
		{
			throw new RemoteException("Cache.get", e);
		}
		finally
		{
			lock.unlock();
		}
		return bean;
	}

	public Bean<T> remove(Key key) throws RemoteException
	{
		Logger.trace(this.getClass(), "cache remove value {}", key);
		Bean<T> bean = null;
		Lock lock = getLocker(key);
		lock.lock();
		try
		{
			Record<T> record = goods.get(key);
			if(record != null)
				bean = new Bean<>(record.value, record.timestamp);
			goods.remove(key);
			pendings.remove(key);
			savedQueue.remove(key);
			delete(key);
		}
		catch(Exception e)
		{
			throw new RemoteException("Cache.remove", e);
		}
		finally
		{
			lock.unlock();
		}
		return bean;
	}

	public void saveBegin(T value, long timestamp) throws RemoteException
	{
		Logger.trace(this.getClass(), "cache saveBegin@{} value={}, version={}", timestamp, value, value.version());
		Lock lock = getLocker(value.key());
		lock.lock();
		try
		{
			boolean newer = false;
			Pended<T> pended = pendings.get(value.key());
			if(pended == null)
			{
				pended = new Pended<>(value, timestamp);
				pendings.put(value.key(), pended);
				newer = true;
			}
			if(pended.isPending())
			{
				if(System.currentTimeMillis() - pended.startTime < 5000)
				{
					throw new OptimisticException("Cache is Working@" + pended.timestamp);
				}
				else
				{
					Logger.warn(this.getClass(), "Cache Working Timeout@ " + pended.timestamp + " " + value);
					pended.revert();
				}
			}
			if(!newer && value.version() - pended.value.version() <= 0)
			{
				throw new OptimisticException("" + value.key() + " version is not greater than older_version=" + pended.value.version() + ", now_version=" + value.version());
			}
			pended.startPending();
			pended.setNewer(value, timestamp);
		}
		finally
		{
			lock.unlock();
		}
	}

	public void saveCommit(Key key, long timestamp)
	{
		Logger.trace(this.getClass(), "cache saveCommit@{} key={}", timestamp, key);
		Lock lock = getLocker(key);
		lock.lock();
		try
		{
			Pended<T> pended = pendings.get(key);
			if(pended != null && pended.isPending() && pended.timestamp == timestamp)
			{
				Record<T> record = goods.get(key);
				if(record == null)
				{
					record = new Record<>(pended.value, pended.timestamp);
					goods.put(key, record);
				}
				else
				{
					record.set(pended.value, pended.timestamp);
				}
				//pended.value.updateVersion();
				pended.commit();
				savedQueue.put(key, pended.value);
			}
			else
			{
				throw new OptimisticException("Cache is not Working@" + timestamp);
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public void saveRollback(Key key, long timestamp)
	{
		Logger.trace(this.getClass(), "cache saveRollback@{} key={}", timestamp, key);
		Lock lock = getLocker(key);
		lock.lock();
		try
		{
			Pended<T> pended = pendings.get(key);
			if(pended != null && pended.isPending() && pended.timestamp == timestamp)
			{
				pended.revert();
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public void interrupt()
	{
		interrupted = true;
	}

	public void saveAll()
	{
		Map.Entry<Key, T> entry = savedQueue.pollFirstEntry();
		while(entry != null)
		{
			T value = entry.getValue();
			if(value != null)
			{
				save(value);
			}
			entry = savedQueue.pollFirstEntry();
		}
	}

	/**
	 * 从数据库或别的地方查找，需要被继承
	 *
	 * @param key key
	 * @return value
	 */
	private T find(Key key)
	{
		if(persister != null)
		{
			return persister.find(key);
		}
		return null;
	}

	private void save(T value)
	{
		if(persister != null)
		{
			persister.save(value);
		}
	}

	private void delete(Key key)
	{
		if(persister != null)
		{
			persister.remove(key);
		}
	}

	public boolean isFinished()
	{
		return savedQueue.size() == 0;
	}

	private class Worker extends Thread
	{
		private final long waitSeconds = 15L * 60L * 1000L;
		@Override
		public void run()
		{
			while(!Cache.this.interrupted)
			{
				try
				{
					Map.Entry<Key, T> entry = savedQueue.pollFirstEntry();
					while(entry != null)
					{
						T value = entry.getValue();
						if(value != null)
						{
							Cache.this.save(value);
						}
						entry = savedQueue.pollFirstEntry();
					}
					Thread.sleep(waitSeconds);
				}
				catch(InterruptedException e)
				{
					Logger.warn(this.getClass(), e.getMessage(), e);
				}
			}
		}
	}

	private class Remover extends Thread
	{
		@Override
		public void run()
		{
			while(!Cache.this.interrupted)
			{
				for(Map.Entry<Key, Record<T>> entry : goods.entrySet())
				{
					getLocker(entry.getKey()).lock();
					try
					{
						if(entry.getValue() != null)
						{
							Record<T> record = entry.getValue();
							if(record.value == null || record.value.isExpired(System.currentTimeMillis()))
							{
								pendings.remove(entry.getKey());
								goods.remove(entry.getKey());
							}
						}
					}
					finally
					{
						getLocker(entry.getKey()).unlock();
					}
				}
				try
				{
					Thread.sleep(1000L * 60L * 60L);
				}
				catch(InterruptedException e)
				{
					Logger.warn(this.getClass(), e.getMessage(), e);
				}
			}
		}
	}
}
