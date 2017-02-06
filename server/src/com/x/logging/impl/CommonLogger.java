package com.x.logging.impl;

import com.x.logging.ILogger;
import org.slf4j.LoggerFactory;

/**
* 类名: CommonLogger </br>
* 描述: 使用slf4j输出的log </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class CommonLogger implements ILogger
{
	/**
	 * WARNING!!!! DO NOT DELETE THIS NEVER!!!
	 * This is used to initialize Logger, which will load xml file.
	 * 这里坚决不能删掉，用来初始化Logger
	 */
	static
	{
		LoggerFactory.getLogger("root");
	}

	@Override
	public void trace(String msg)
	{
		LoggerFactory.getLogger(CommonLogger.class).trace(msg);
	}

	@Override
	public void trace(String msg, Object... objects)
	{
		LoggerFactory.getLogger(CommonLogger.class).trace(msg, objects);
	}

	@Override
	public void trace(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(CommonLogger.class).trace(msg, throwable);
	}

	@Override
	public void debug(String msg)
	{
		LoggerFactory.getLogger(CommonLogger.class).debug(msg);
	}

	@Override
	public void debug(String msg, Object... objects)
	{
		LoggerFactory.getLogger(CommonLogger.class).debug(msg, objects);
	}

	@Override
	public void debug(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(CommonLogger.class).debug(msg, throwable);
	}

	@Override
	public void info(String msg)
	{
		LoggerFactory.getLogger(CommonLogger.class).info(msg);
	}

	@Override
	public void info(String msg, Object... objects)
	{
		LoggerFactory.getLogger(CommonLogger.class).info(msg, objects);
	}

	@Override
	public void info(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(CommonLogger.class).info(msg, throwable);
	}

	@Override
	public void warn(String msg)
	{
		LoggerFactory.getLogger(CommonLogger.class).warn(msg);
	}

	@Override
	public void warn(String msg, Object... objects)
	{
		LoggerFactory.getLogger(CommonLogger.class).warn(msg, objects);
	}

	@Override
	public void warn(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(CommonLogger.class).warn(msg, throwable);
	}

	@Override
	public void error(String msg)
	{
		LoggerFactory.getLogger(CommonLogger.class).error(msg);
	}

	@Override
	public void error(String msg, Object... objects)
	{
		LoggerFactory.getLogger(CommonLogger.class).error(msg, objects);
	}

	@Override
	public void error(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(CommonLogger.class).debug(msg, throwable);
	}

	@Override
	public void trace(Class c, String msg)
	{
		LoggerFactory.getLogger(c).trace(msg);
	}

	@Override
	public void trace(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).trace(msg, objects);
	}

	@Override
	public void trace(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).trace(msg, throwable);
	}

	@Override
	public void debug(Class c, String msg)
	{
		LoggerFactory.getLogger(c).debug(msg);
	}

	@Override
	public void debug(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).debug(msg, objects);
	}

	@Override
	public void debug(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).debug(msg, throwable);
	}

	@Override
	public void info(Class c, String msg)
	{
		LoggerFactory.getLogger(c).info(msg);
	}

	@Override
	public void info(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).info(msg, objects);
	}

	@Override
	public void info(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).info(msg, throwable);
	}

	@Override
	public void warn(Class c, String msg)
	{
		LoggerFactory.getLogger(c).warn(msg);
	}

	@Override
	public void warn(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).warn(msg, objects);
	}

	@Override
	public void warn(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).warn(msg, throwable);
	}

	@Override
	public void error(Class c, String msg)
	{
		LoggerFactory.getLogger(c).error(msg);
	}

	@Override
	public void error(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).error(msg, objects);
	}

	@Override
	public void error(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).debug(msg, throwable);
	}
}
