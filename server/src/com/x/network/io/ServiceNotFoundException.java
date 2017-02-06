package com.x.network.io;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 7/1/15 4:45 PM
 */
public class ServiceNotFoundException extends RuntimeException
{
	public ServiceNotFoundException()
	{
	}

	public ServiceNotFoundException(String s)
	{
		super(s);
	}

	public ServiceNotFoundException(String s, Throwable throwable)
	{
		super(s, throwable);
	}

	public ServiceNotFoundException(Throwable throwable)
	{
		super(throwable);
	}
}
