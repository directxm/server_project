package com.x.network.service;

import com.x.io.CodeException;
import com.x.io.CoreErrorCode;
import com.x.logging.Logger;
import com.x.network.standalone.ServiceProvider;
import com.x.util.ExceptionUtils;
import com.x.network.io.Address;
import com.x.network.standalone.ServiceContext;
import com.x.network.standalone.ServicePath;
import com.x.network.io.State;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 7/2/15 2:32 PM
 */
public abstract class ServicePipe
{
	//public static final AttributeKey PIPE_SESSION_KEY = new AttributeKey(ServicePipe.class, "__PIPE_ID__");

	protected final ServiceSystem system;
	protected final ServiceProvider provider;
	protected final long pid;

	protected final ServicePath remotePath;
	protected final ServicePath localPath;

	private State state = null;
	private ServiceContext serviceContext = null;
	private final ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<>();

	protected ServicePipe(ServiceSystem system, ServiceProvider provider, long pid, Address remoteAddress, Address localAddress)
	{
		this.system = system;
		this.provider = provider;
		this.pid = pid;
		this.remotePath = new ServicePath(pid, remoteAddress);
		this.localPath = new ServicePath(pid, localAddress);
	}

	public ServiceSystem getSystem()
	{
		return system;
	}

	public ServiceProvider getProvider()
	{
		return provider;
	}

	public long getPid()
	{
		return pid;
	}

	public ServicePath getRemotePath()
	{
		return remotePath;
	}

	/**
	 * 下面这几个方法的实现都必须要确保线程安全
	 *
	 * @return 是否开启
	 */
	public abstract boolean isOpened();

	public abstract void close();

	protected abstract void innerTell(Object message);

	public abstract void secureInput(byte[] bytes);

	public abstract void secureOutput(byte[] bytes);

	public abstract void onSetState(State state);

	public final void setContext(ServiceContext serviceContext)
	{
		synchronized(this)
		{
			this.serviceContext = serviceContext;
		}
	}

	public final ServiceContext getContext()
	{
		synchronized(this)
		{
			return serviceContext;
		}
	}

	public final Object getAttribute(String name)
	{
		return attributes.get(name);
	}

	public final void setAttribute(String name, Object object)
	{
		attributes.put(name, object);
	}

	public final Object removeAttribute(String name)
	{
		return attributes.remove(name);
	}


	public void mark(String name)
	{
		system.mark(name, pid);
	}

	public void unMark()
	{
		system.unMark(pid);
	}

	public void tell(Object message)
	{
		this.innerTell(message);
	}

	public final void setState(State state)
	{
		synchronized(this)
		{
			this.state = state;
		}
		onSetState(state);
	}

	public final boolean allowMessage(Object message)
	{
		/*synchronized(this)
		{
			if(state != null)
			{
				return state.hasMessage(message);
			}
		}*/
		return true;
	}

	public void pipeOpened()
	{
		Logger.trace(this.getClass(), "{} opened.", toString());
		ServiceCell cell = system.getServiceCell(pid);
		cell.offer(this, MessageType.PIPE_OPENED, null);
	}

	public void pipeClosed()
	{
		unMark();
		ServiceCell cell = system.getServiceCell(pid);
		cell.offer(this, MessageType.PIPE_CLOSED, serviceContext);
		synchronized(this)
		{
			serviceContext = null;
		}
		attributes.clear();
		system.clearQuestionWatchers(pid);
		Logger.trace(ServicePipe.class, "{} closed.", toString());
	}

	public void pipeIdled(Object status)
	{
		Logger.trace(ServicePipe.class, "{} Idle: {}.", toString(), status);
		ServiceCell cell = system.getServiceCell(pid);
		cell.offer(this, MessageType.PIPE_IDLED, status);
	}

	public void pipeCaughtException(Throwable cause)
	{
		Logger.trace(ServicePipe.class, "{} caught exception {} {}", toString(), cause, ExceptionUtils.getExceptionStackTraceFileLineAndMethod(cause));
		ServiceCell cell = system.getServiceCell(pid);
		cell.offer(this, MessageType.PIPE_CAUGHT_EXCEPTION, cause);
	}

	public void pipeReceivedMessage(Object message) throws CodeException
	{
		if(provider.isDumpReceivedMessage() || provider.isDumpMessage())
		{
			Logger.trace(ServicePipe.class, "ReceivedFrom[{}]: {}", remotePath, message);
		}
		if(message != null)
		{
			if(!system.dispatchMessage(pid, message))
			{
				if(!allowMessage(message))
				{
					throw new CodeException(CoreErrorCode.ILLEGAL, "Protocol State invalid: " + message.getClass().getSimpleName() + "!@" + state);
				}
				ServiceCell cell = system.getServiceCell(pid);
				cell.offer(this, MessageType.PIPE_RECEIVED_MESSAGE, message);
			}
		}
	}

	public void pipeSentMessage(Object message)
	{
		if(provider.isDumpSentMessage() || provider.isDumpMessage())
		{
			Logger.trace(ServicePipe.class, "SentTo[{}]: {}", remotePath, message);
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		ServicePipe that = (ServicePipe)o;

		if(pid != that.pid) return false;
		if(system != null ? !system.equals(that.system) : that.system != null) return false;
		return provider != null ? provider.equals(that.provider) : that.provider == null;

	}

	@Override
	public int hashCode()
	{
		int result = system != null ? system.hashCode() : 0;
		result = 31 * result + (provider != null ? provider.hashCode() : 0);
		result = 31 * result + (int)(pid ^ (pid >>> 32));
		return result;
	}

	@Override
	public String toString()
	{
		return "ServicePipe{" +
				"provider=" + provider.getName() +
				", pid=" + pid +
				", remotePath=" + remotePath.getAddress() +
				", localPath=" + localPath.getAddress() +
				'}';
	}
}
