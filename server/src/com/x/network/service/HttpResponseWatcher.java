package com.x.network.service;

import com.x.logging.Logger;
import com.x.network.io.ResponseWatcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/20/16 3:58 PM
 */
final class HttpResponseWatcher extends ResponseWatcher
{
	private static final AtomicInteger ID = new AtomicInteger(0);

	private final int id;
	private final String url;
	private final long pid;
	private ServiceCoroutine coroutine;

	private String response = null;
	private Exception exception = null;

	public HttpResponseWatcher(ServiceCoroutine coroutine, long pid, String url, int timeout)
	{
		super(timeout);
		this.coroutine = coroutine;
		this.pid = pid;
		this.id = ID.incrementAndGet();
		this.url = url;
	}

	public int getId()
	{
		return id;
	}

	public String getUrl()
	{
		return url;
	}

	@Override
	public void timeout()
	{
		Logger.warn("Timeout({})-GET({})-@[{}]", getTimeout(), getUrl(), coroutine);
		ServiceCell cell = coroutine.getServiceSystem().getServiceCell(pid);
		cell.offer(null, MessageType.PIPE_RESPONSE_CONTINUE, coroutine);
	}

	public synchronized String getResponse()
	{
		return response;
	}

	public synchronized void setResponse(Object object)
	{
		if(object instanceof Exception)
		{
			this.exception = (Exception)object;
		}
		else if(object instanceof String)
		{
			this.response = (String)object;
		}
		Logger.debug(HttpResponseWatcher.class, "Received http response {} {}", this.response, this.exception);
		ServiceCell cell = coroutine.getServiceSystem().getServiceCell(pid);
		cell.offer(null, MessageType.PIPE_RESPONSE_CONTINUE, coroutine);
	}

	public synchronized Exception getException()
	{
		return exception;
	}
}
