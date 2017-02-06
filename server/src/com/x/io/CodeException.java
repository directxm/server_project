package com.x.io;

import com.x.util.ExceptionUtils;

import java.io.IOException;

/**
 * @author yanfengbing
 * @since 13-12-7 PM4:10
 */
public class CodeException extends IOException
{
	private final ErrorCode errorCode;

	public CodeException(ErrorCode errorCode)
	{
		this.errorCode = errorCode;
	}

	public CodeException(ErrorCode errorCode, String s)
	{
		super(s);
		this.errorCode = errorCode;
	}

	public CodeException(ErrorCode errorCode, String s, Throwable throwable)
	{
		super(s, throwable);
		this.errorCode = errorCode;
	}

	public CodeException(ErrorCode errorCode, Throwable throwable)
	{
		super(throwable);
		this.errorCode = errorCode;
	}

	public CodeException(String s, Throwable throwable)
	{
		super(s, throwable);
		this.errorCode = ExceptionUtils.getExceptionErrorCode(throwable);
	}

	public CodeException(Throwable other)
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
		return "[CODE: " + errorCode.code() + "][" + errorCode.message() + "] "
				+ super.getMessage();
	}

	public String getMessage()
	{
		return "" + super.getMessage();
	}
}
