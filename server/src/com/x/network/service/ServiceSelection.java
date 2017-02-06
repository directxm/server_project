package com.x.network.service;

import com.x.logging.Logger;
import com.x.network.standalone.ServicePath;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 10/13/15 4:30 PM
 */
public class ServiceSelection
{
	private final ServiceSystem system;
	private final String name;
	protected final Set<Long> pidSet = new ConcurrentSkipListSet<Long>();

	ServiceSelection(ServiceSystem system, String name)
	{
		this.system = system;
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public ServiceSystem getSystem()
	{
		return system;
	}

	public void mark(long pid)
	{
		pidSet.add(pid);
	}

	public void unMark(long pid)
	{
		pidSet.remove(pid);
	}

	public ServicePath pick()
	{
		for(long pid : pidSet)
		{
			return system.getServicePipe(pid).getRemotePath();
		}
		Logger.warn(ServiceSelection.class, "ServiceSelection({}) has no pipe", name);
		return null;
	}

	public void tell(Object message)
	{
		for(long pid : pidSet)
		{
			system.tell(pid, message);
		}
	}
}
