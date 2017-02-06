package com.x.io;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 7/22/15 12:03 PM
 */
public class OptimisticException extends RuntimeException
{
	public OptimisticException()
	{
	}

	public OptimisticException(String s)
	{
		super(s);
	}

	public OptimisticException(String s, Throwable throwable)
	{
		super(s, throwable);
	}

	public OptimisticException(Throwable throwable)
	{
		super(throwable);
	}
}
