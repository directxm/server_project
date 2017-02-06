package com.x.network.service;

import com.x.io.CodeException;
import com.x.io.CoreErrorCode;
import com.x.lang.TimeWatcher;
import com.x.network.io.Rpc;
import com.x.logging.Logger;
import com.x.network.io.Protocol;
import com.x.util.ExceptionUtils;
import com.offbynull.coroutines.user.Continuation;
import com.offbynull.coroutines.user.Coroutine;
import com.offbynull.coroutines.user.CoroutineRunner;
import com.x.network.io.RemoteException;
import com.x.network.standalone.ServicePath;
import com.x.network.io.TimeoutException;
import org.slf4j.MDC;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 2/24/16 4:24 PM
 */
final class ServiceCoroutine implements Coroutine
{
	private final ServiceCoroutineStack stack = new ServiceCoroutineStack();
	private final AtomicBoolean using = new AtomicBoolean(true);
	private final AtomicBoolean finishing = new AtomicBoolean(false);
	private final TimeWatcher finishingTimeWatcher = new TimeWatcher();
	private final CoroutineRunner coroutineRunner;

	private boolean paused = false;
	private final TimeWatcher pauseWatcher = new TimeWatcher();
	private long pauseTime = 0;
	private final WeakHashMap<ServiceWrapper, Boolean> serviceWrapperWeakHashMap = new WeakHashMap<>();
	final MessageBox messageBox = new MessageBox();
	private final long cid;
	private final ServiceSystem system;

	ServiceCoroutine(ServiceSystem system, long cid)
	{
		this.system = system;
		this.cid = cid;
		coroutineRunner = new CoroutineRunner(this);
	}

	final ServiceSystem getServiceSystem()
	{
		return system;
	}

	final long getCid()
	{
		return cid;
	}

	public final void setUsing(boolean using)
	{
		this.using.set(using);
	}

	final boolean checkFinishing()
	{
		return !isPaused() && finishing.get() && messageBox.size() == 0 && !using.get() && finishingTimeWatcher.elapse() > 60000 && serviceWrapperWeakHashMap.size() == 0;
	}

	final void offer(Message message)
	{
		finishing.set(false);
		messageBox.offer(message);
		using.set(false);
	}

	private boolean isPaused()
	{
		return paused && pauseWatcher.elapse() < pauseTime;
	}

	public void execute()
	{
		if(!isPaused())
		{
			while(messageBox.size() > 0)
			{
				if(coroutineRunner.execute())
				{
					break;
				}
			}
		}
		if(!isPaused() && messageBox.size() == 0)
		{
			finishing.set(true);
			finishingTimeWatcher.reset();
		}
	}

	private void pause(Continuation c, long millis)
	{
		//System.err.println("++++++++++++++ pause @" + TimeUtil.now() + ", " + coroutineRunner + " with continuation " + c);
		paused = true;
		pauseTime = millis;
		pauseWatcher.reset();
		c.suspend();
	}

	void resume()
	{
		paused = false;
		pauseTime = 0;
		if(!coroutineRunner.execute())
		{
			while(messageBox.size() > 0)
			{
				if(coroutineRunner.execute())
				{
					break;
				}
			}
		}
		if(!isPaused() && messageBox.size() == 0)
		{
			finishing.set(true);
			finishingTimeWatcher.reset();
		}
	}

	public void tell(long pid, Object message)
	{
		system.tell(pid, stack.filter(pid, message));
		using.set(false);
	}

	public void tell(ServicePath receiver, Object message)
	{
		system.tell(receiver, stack.filter(receiver, message));
		using.set(false);
	}

	public void tell(ServiceSelection selection, Object message)
	{
		for(long pid : selection.pidSet)
		{
			system.tell(pid, stack.filter(pid, message));
		}
		using.set(false);
	}

	@SuppressWarnings("unchecked")
	public <P extends Protocol, V extends Protocol> V ask(Continuation c, ServicePath receiver, P sendMessage, int timeout, Class<V> receivedMessageClass) throws CodeException
	{
		AskResponseWatcher watcher = system.call(this, receiver.getPid(), sendMessage, timeout);
		system.tell(receiver, watcher.getRpc());
		Map<String, String> mdcContext = MDC.getCopyOfContextMap();
		pause(c, timeout * 1000L);
		MDC.setContextMap(mdcContext);
		Rpc receivedRpc = watcher.getReceivedRpc();
		using.set(false);
		if(receivedRpc == null)
		{
			throw new TimeoutException(String.format("wait for rpc %x time out!", watcher.getRpc().getXid()));
		}
		else
		{
			if(receivedRpc.getContent() == null)
			{
				if(receivedRpc.getRetCode() != 0)
				{
					throw new RemoteException(receivedRpc.getRetCode(), receivedRpc.getMsg());
				}
				else
				{
					throw new TimeoutException(String.format("wait for rpc %x time out!", watcher.getRpc().getXid()));
				}
			}
			else
			{
				if(receivedRpc.getRetCode() != 0)
				{
					throw new RemoteException(receivedRpc.getRetCode(), receivedRpc.getMsg());
				}
				else
				{
					if(receivedMessageClass.isAssignableFrom(receivedRpc.getContent().getClass()))
					{
						return (V)receivedRpc.getContent();
					}
					else
					{
						throw new CodeException(CoreErrorCode.ILLEGAL, "need " + receivedMessageClass.getSimpleName() + ", but received " + receivedRpc.getContent().getClass().getSimpleName());
					}
				}
			}
		}
	}

	public <P extends Protocol, V extends Protocol> V ask(Continuation c, ServiceSelection selection, P sendMessage, int timeout, Class<V> receivedMessageClass) throws CodeException
	{
		return ask(c, selection.pick(), sendMessage, timeout, receivedMessageClass);
	}

	/*@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	String doHttpGet(Continuation c, long pid, String url, Map<String, String> params, int timeout) throws CodeException
	{
		HttpResponseWatcher future;
		try
		{
			future = system.call(this, pid, HttpUtils.encodeURL(url, params, "UTF-8"), timeout);
		}
		catch(UnsupportedEncodingException e)
		{
			throw new CodeException(CoreErrorCode.ENCODING, "UTF-8", e);
		}
		Map<String, String> mdcContext = MDC.getCopyOfContextMap();
		pause(c, timeout * 1000L);
		MDC.setContextMap(mdcContext);
		if(null != future.getException())
		{
			throw new RemoteException(future.getException());
		}
		else
		{
			if(future.getResponse() == null)
			{
				throw new TimeoutException(String.format("wait for http request %s timeout(%d).", url, future.getTimeout()));
			}
			else
			{
				return future.getResponse();
			}
		}
	}

	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	String doHttpPost(Continuation c, long pid, String url, String param, int timeout) throws CodeException
	{
		HttpResponseWatcher future;
		future = system.call(this, pid, url, param, timeout);
		Map<String, String> mdcContext = MDC.getCopyOfContextMap();
		pause(c, timeout * 1000L);
		MDC.setContextMap(mdcContext);
		if(null != future.getException())
		{
			throw new RemoteException(future.getException());
		}
		else
		{
			if(future.getResponse() == null)
			{
				throw new TimeoutException(String.format("wait for http request %s %s timeout(%d).", url, param, future.getTimeout()));
			}
			else
			{
				return future.getResponse();
			}
		}
	}*/

	@Override
	public void run(Continuation continuation) throws Exception
	{
//			System.err.println("++++++ serviceCellCoroutine " + coroutine + " " + System.identityHashCode(coroutine) + " run with continuation " + continuation + " @" + i);
		Message message = messageBox.poll();
		if(message != null)
		{
			try
			{
				ServicePipe pipe = message.getServicePipe();
				if(pipe != null)
				{
					ServiceWrapper service = system.getServiceWrapper(pipe.provider, pipe.pid, this);//new ServiceWrapper(system, pipe.provider, pipe.pid, coroutine);
					serviceWrapperWeakHashMap.put(service, true);
					Object object = message.getContent();
					try
					{
						stack.init(pipe.getPid(), object);
						try
						{
							switch(message.getMessageType())
							{
								case PIPE_RECEIVED_MESSAGE:
									if(object instanceof Protocol)
									{
										pipe.getProvider().process(continuation, service, pipe.getRemotePath(), (Protocol)object);
									}
									else
									{
										pipe.getProvider().processUnknownObject(continuation, service, pipe.getRemotePath(), object);
									}
									break;
							}
						}
						catch(Exception e)
						{
							Exception ee = stack.caughtException(e);
							if(ee != null)
							{
								throw ee;
							}
						}
					}
					catch(Exception e)
					{
						Logger.warn(ServiceCell.class, "{} caught exception. {} {}", this, e.toString(), ExceptionUtils.getExceptionStackTraceFileLineAndMethod(e));
						pipe.getProvider().processExceptionCaught(continuation, service, pipe.getRemotePath(), message.getContent(), e);
					}
					finally
					{
						stack.clear();
					}
				}
				else
				{
					Logger.warn(ServiceCell.class, "{} ServicePipe {} is closed.", this, message.getServicePipe().getPid());
				}
			}
			catch(Exception e)
			{
				Logger.warn(ServiceCell.class, "{} caught exception at exceptionCaught. {} {}", this, e, ExceptionUtils.getExceptionStackTraceFileLineAndMethod(e));
			}
		}
	}

	boolean isRpc()
	{
		return stack.getStackType() == StackType.RPC;
	}

	private enum StackType
	{
		NONE,
		PROTOCOL,
		RPC,
	}

	private class ServiceCoroutineStack
	{
		private StackType stackType = StackType.NONE;
		private long pid;
		private Rpc rpc = null;
		private boolean responsed = false;

		synchronized StackType getStackType()
		{
			return stackType;
		}

		public synchronized void init(long pid, Object message)
		{
			this.pid = pid;
			if(message instanceof Rpc)
			{
				stackType = StackType.RPC;
				rpc = (Rpc)message;
			}
			else
			{
				stackType = StackType.PROTOCOL;
			}
		}

		public synchronized Object filter(ServicePath receiver, Object message)
		{
			return filter(receiver.getPid(), message);
		}

		public synchronized Object filter(long pid, Object message)
		{
			switch(stackType)
			{
				case RPC:
					if(message instanceof Protocol)
					{
						if(!responsed)
						{
							if(this.pid == pid)
							{
								Rpc response = new Rpc(getCid(), rpc.getXid());
								response.setResponse();
								response.setContent(message);
								responsed = true;
								return response;
							}
						}
					}
				default:
					break;
			}
			return message;
		}

		synchronized Exception caughtException(Exception e)
		{
			switch(stackType)
			{
				case RPC:
					if(!responsed)
					{
						int code = ExceptionUtils.getExceptionErrorCodeValue(e);
						String msg = ExceptionUtils.getExceptionMessage(e);
						Rpc response = new Rpc(getCid(), rpc.getXid());
						response.setResponse();
						response.setRetCode(code);
						response.setMsg(msg);
						tell(pid, response);
						responsed = true;
						Logger.warn(ServiceCoroutineStack.class, "Rpc process caught exception and return: {} {}", e.toString(), ExceptionUtils.getExceptionStackTraceFileLineAndMethod(e));
						return null;
					}
					break;
			}
			return e;
		}

		public synchronized void clear()
		{
			if(!isPaused())
			{
				stackType = StackType.NONE;
				pid = 0;
				rpc = null;
				responsed = false;
			}
		}
	}
}
