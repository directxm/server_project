package com.x.network.service;

import com.x.network.io.Rpc;
import com.x.logging.Logger;
import com.x.network.io.Protocol;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/24/15 2:59 PM
 */
public final class BlockedAskResponseWatcher extends AskResponseWatcher
{
	public BlockedAskResponseWatcher(long pid, Protocol sendMessage, int timeout)
	{
		super(null, pid, sendMessage, timeout);
	}

	@Override
	public void timeout()
	{
		Logger.warn("Timeout({})-Send({})to[{}]@[{}]with({}).", getTimeout(), rpc.getContent().getClass().getSimpleName(), pid, coroutine, rpc);
		synchronized(this)
		{
			this.notifyAll();
		}
	}

	@Override
	public synchronized void answer(Rpc v)
	{
		receivedRpc = v;
		synchronized(this)
		{
			this.notifyAll();
		}
	}
}
