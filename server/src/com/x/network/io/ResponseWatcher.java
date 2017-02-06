package com.x.network.io;

import com.x.lang.TimeWatcher;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/21/16 2:33 PM
 */
public abstract class ResponseWatcher
{
	private final TimeWatcher watchDog;
	private final int timeout;

	public ResponseWatcher(int timeout)
	{
		this.watchDog = new TimeWatcher();
		this.timeout = timeout;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public boolean isTimeout()
	{
		return watchDog.elapse() > timeout * 1000L;
	}

	public abstract void timeout();
}
