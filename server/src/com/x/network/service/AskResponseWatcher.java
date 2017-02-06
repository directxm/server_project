package com.x.network.service;

import com.x.network.io.Rpc;
import com.x.network.io.ResponseWatcher;
import com.x.logging.Logger;
import com.x.network.io.Protocol;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/24/15 2:59 PM
 */
class AskResponseWatcher extends ResponseWatcher
{
	protected ServiceCoroutine coroutine;
	protected long pid;
	protected Rpc rpc;
	protected Rpc receivedRpc = null;

	public AskResponseWatcher(ServiceCoroutine coroutine, long pid, Protocol sendMessage, int timeout)
	{
		super(timeout);
		this.coroutine = coroutine;
		this.pid = pid;
		this.rpc = new Rpc(coroutine == null ? pid : coroutine.getCid(), sendMessage);
	}

	public Rpc getRpc()
	{
		return rpc;
	}

	public void timeout()
	{
		Logger.warn("Timeout({})-Send({})to[{}]@[{}]with({}).", getTimeout(), rpc.getContent().getClass().getSimpleName(), pid, coroutine, rpc);
		ServiceCell cell = coroutine.getServiceSystem().getServiceCell(pid);
		cell.offer(null, MessageType.PIPE_RESPONSE_CONTINUE, coroutine);
	}

	public synchronized void answer(Rpc v)
	{
		receivedRpc = v;
		ServiceCell cell = coroutine.getServiceSystem().getServiceCell(pid);
		cell.offer(null, MessageType.PIPE_RESPONSE_CONTINUE, coroutine);
	}

	public synchronized Rpc getReceivedRpc()
	{
		return receivedRpc;
	}
}
