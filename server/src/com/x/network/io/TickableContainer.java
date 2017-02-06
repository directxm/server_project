package com.x.network.io;

import com.x.network.io.Tickable;
import com.x.network.ServiceThreadPool;

import java.util.IdentityHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/29/14 4:22 PM
 */
public class TickableContainer
{
	private final IdentityHashMap<Tickable, ScheduledFuture<?>> tickableMap = new IdentityHashMap<>();
	private final ServiceThreadPool serviceThreadPool;

	public TickableContainer(ServiceThreadPool serviceThreadPool)
	{
		this.serviceThreadPool = serviceThreadPool;
	}

	public void addTickable(Tickable t)
	{
		if(t != null)
		{
			synchronized(tickableMap)
			{
				tickableMap.put(t, serviceThreadPool.scheduleWithFixedDelay(t::tick, t.elapse(), t.elapse(), TimeUnit.MILLISECONDS));
			}
		}
	}

	public Tickable removeTickable(Tickable t)
	{
		if(t != null)
		{
			synchronized(tickableMap)
			{
				ScheduledFuture<?> scheduledFuture = tickableMap.remove(t);
				if(scheduledFuture != null)
				{
					scheduledFuture.cancel(false);
				}
			}
		}
		return t;
	}
}
