package com.x.lang;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/31/15 1:09 PM
 */
public class Locker
{
	private final Lock[] lockers;

	public Locker(int count)
	{
		lockers = new ReentrantLock[count];
	}

	public Lock getLock(Object o)
	{
		int i = 0;
		if(o != null)
		{
			i = Math.abs(o.hashCode());
		}
		i %= lockers.length;
		synchronized(lockers)
		{
			return lockers[i] == null ? lockers[i] = new ReentrantLock() : lockers[i];
		}
	}
}
