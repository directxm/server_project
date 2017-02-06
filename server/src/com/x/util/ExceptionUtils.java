package com.x.util;

import com.x.io.CodeException;
import com.x.io.CodeRuntimeException;
import com.x.io.CoreErrorCode;
import com.x.io.ErrorCode;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/31/15 4:09 PM
 */
public class ExceptionUtils
{
	public static ErrorCode getExceptionErrorCode(Throwable e)
	{
		if(e == null)
			return CoreErrorCode.UNKNOWN;
		Throwable t = e;
		ErrorCode r = CoreErrorCode.ERROR;
		do
		{
			if(t instanceof CodeException)
			{
				r = ((CodeException)t).getCode();
			}
			if(t instanceof CodeRuntimeException)
			{
				r = ((CodeRuntimeException)t).getCode();
			}
			if(t instanceof RuntimeException)
			{
				r = CoreErrorCode.FATAL;
			}
			t = t.getCause();
		}
		while(t != null);
		return r;
	}

	public static int getExceptionErrorCodeValue(Throwable e)
	{
		return getExceptionErrorCode(e).code();
	}

	public static String getExceptionMessage(Throwable e)
	{
		if(e == null)
			return "";
		StringBuilder builder = new StringBuilder();
		Throwable t = e;
		do
		{
			if(t instanceof CodeException || t instanceof CodeRuntimeException)
			{
				if(t.getMessage() != null && t.getMessage().length() > 0)
				{
					builder.append(t.getMessage()).append(" ");
				}
			}
			else
			{
				builder.append(t.getClass().getSimpleName()).append(":").append(t.getMessage()).append(" ");
			}
			t = t.getCause();
		}
		while(t != null);
		return builder.toString();
	}

	public static String getExceptionStackTraceFileLineAndMethod(Throwable e)
	{
		if(e == null)
			return "";
		StringBuilder builder = new StringBuilder();
		Throwable t = e;
		while(t != null)
		{
			StackTraceElement[] stackTraceElements = t.getStackTrace();
			if(stackTraceElements != null && stackTraceElements.length > 0)
			{
				for(StackTraceElement stackTraceElement : stackTraceElements)
				{
					builder.append(String.format("[%s:%s: %s]", stackTraceElement.getFileName(), stackTraceElement.getLineNumber(), stackTraceElement.getMethodName()));
				}
			}
			t = t.getCause();
			if(t != null)
			{
				builder.append("-->");
			}
		}
		return builder.toString();
	}
}
