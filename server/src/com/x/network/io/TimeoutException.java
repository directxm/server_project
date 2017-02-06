package com.x.network.io;

import com.x.io.CodeException;
import com.x.io.CoreErrorCode;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/19/16 11:27 AM
 */
public class TimeoutException extends CodeException
{
	public TimeoutException()
	{
		super(CoreErrorCode.TIMEOUT);
	}

	public TimeoutException(String s)
	{
		super(CoreErrorCode.TIMEOUT, s);
	}

	public TimeoutException(String s, Throwable throwable)
	{
		super(CoreErrorCode.TIMEOUT, s, throwable);
	}

	public TimeoutException(Throwable throwable)
	{
		super(CoreErrorCode.TIMEOUT, throwable);
	}
}
