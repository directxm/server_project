package com.x.network.io;

import com.x.io.CodeException;
import com.x.io.ErrorCode;
import com.x.util.ExceptionUtils;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/19/16 11:27 AM
 */
public class RemoteException extends CodeException
{
	public RemoteException(int codeValue, String msg)
	{
		super(new ErrorCode()
		{
			@Override
			public int code()
			{
				return codeValue;
			}

			@Override
			public String message()
			{
				return msg;
			}
		}, msg);
	}

	public RemoteException(Throwable throwable)
	{
		super(ExceptionUtils.getExceptionErrorCode(throwable), throwable);
	}
}
