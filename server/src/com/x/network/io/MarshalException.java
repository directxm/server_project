package com.x.network.io;

import com.x.io.CodeException;
import com.x.io.CoreErrorCode;

/**
 * @author yanfengbing
 * @since 13-6-19 下午5:00
 */
@Deprecated
class MarshalException extends CodeException
{
	public MarshalException()
	{
		super(CoreErrorCode.MARSHAL);
	}

	public MarshalException(String s)
	{
		super(CoreErrorCode.MARSHAL, s);
	}

	public MarshalException(Throwable throwable)
	{
		super(CoreErrorCode.MARSHAL, throwable);
	}

	public MarshalException(String s, Throwable throwable)
	{
		super(CoreErrorCode.MARSHAL, s, throwable);
	}
}
