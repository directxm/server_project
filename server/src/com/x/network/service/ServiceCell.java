package com.x.network.service;

import com.x.logging.Logger;
import com.x.network.standalone.ServiceContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ServiceCell 是处理单元，所有的处理单元都是一样的，属于同一个ServiceSystem。
 *
 * @author yanfengbing
 * @since 13-9-18 PM4:46
 */
class ServiceCell implements Runnable
{
	private final ServiceSystem system;
	private final long id;

	private final AtomicBoolean running = new AtomicBoolean(false);

	private final MessageBox messageBox = new MessageBox();

	ServiceCell(ServiceSystem system, long id)
	{
		this.system = system;
		this.id = id;
		Logger.trace(ServiceCell.class, "" + toString() + " created.");
	}

	public final ServiceSystem getSystem()
	{
		return system;
	}

	public final long getId()
	{
		return id;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		ServiceCell that = (ServiceCell)o;

		return id == that.id && (system != null ? system.equals(that.system) : that.system == null);
	}

	@Override
	public int hashCode()
	{
		int result = system != null ? system.hashCode() : 0;
		result = 31 * result + (int)(id ^ (id >>> 32));
		return result;
	}

	@Override
	public String toString()
	{
		return "ServiceCell{" +
				"system=" + system.getName() +
				", id=" + id +
				'}';
	}

	public String getName()
	{
		return toString();
	}

	final void offer(ServicePipe pipe, MessageType messageType, Object msg)
	{
		Message message = new Message(pipe, messageType, msg);
		messageBox.offer(message);
		if(!running.get() && messageBox.size() > 0)
		{
			getSystem().executeCell(this);
		}
	}

	public final void run()
	{
		if(!running.compareAndSet(false, true)) return;
		try
		{
			Message message = messageBox.poll();
			if(message != null)
			{
				ServicePipe pipe = message.getServicePipe();
				Object object = message.getContent();
				switch(message.getMessageType())
				{
					case PIPE_CLOSED:
						if(object != null)
						{
							ServiceContext context = (ServiceContext)object;
							context.dispose();
						}
						pipe.getProvider().pipeClosed(pipe);
						break;
					case PIPE_RESPONSE_CONTINUE:
					{
						ServiceCoroutine coroutine = (ServiceCoroutine)object;
						coroutine.resume();
						break;
					}
					case PIPE_OPENED:
						pipe.getProvider().pipeOpened(pipe);
						break;
					case PIPE_CAUGHT_EXCEPTION:
						pipe.getProvider().pipeCaughtException(pipe, (Throwable)object);
						break;
					case PIPE_RECEIVED_MESSAGE:
					{
						long cid = pipe.getProvider().calculateServiceCoroutineId(pipe, object);
						ServiceCoroutine coroutine = system.getServiceCoroutine(cid);
						coroutine.offer(message);
						coroutine.execute();
						break;
					}
					case PIPE_IDLED:
						pipe.getProvider().pipeIdled(pipe, object);
						break;
					default:
						Logger.warn(ServicePipe.class, "Unknown Message Type {} @{} with {}", message.getMessageType(), pipe, object);
						break;
				}
			}
		}
		finally
		{
			running.set(false);
			if(messageBox.size() > 0)
			{
				getSystem().executeCell(this);
			}
		}
	}
}
