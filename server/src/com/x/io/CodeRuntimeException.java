package com.x.io;

import com.x.util.ExceptionUtils;

/**
 * @author yanfengbing
 * @since 13-12-9 PM1:49
 */
public class CodeRuntimeException extends RuntimeException
{
	private final ErrorCode errorCode;

	public CodeRuntimeException(ErrorCode errorCode, String s)
	{
		super(s);
		this.errorCode = errorCode;
	}

	public CodeRuntimeException(ErrorCode errorCode, String s, Throwable throwable)
	{
		super(s, throwable);
		this.errorCode = errorCode;
	}

	public CodeRuntimeException(ErrorCode errorCode, Throwable throwable)
	{
		super(throwable);
		this.errorCode = errorCode;
	}

	public CodeRuntimeException(String s, Throwable throwable)
	{
		super(s, throwable);
		this.errorCode = ExceptionUtils.getExceptionErrorCode(throwable);
	}

	public CodeRuntimeException(Throwable other)
	{
		super(other.getMessage(), other);
		this.errorCode = ExceptionUtils.getExceptionErrorCode(other);
	}

	public ErrorCode getCode()
	{
		return errorCode;
	}

	@Override
	public String toString()
	{
		return "[CODE: " + errorCode.code() + "][" + errorCode.message() + "] " +
				super.getMessage();
	}

	@Override
	public String getMessage()
	{
		return "" + super.getMessage();
	}
}
