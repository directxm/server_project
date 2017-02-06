package com.x.logging.impl;

import com.x.logging.ILogger;

/**
* 类名: SystemOutLogger </br>
* 描述: 使用println输出的log </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class SystemOutLogger implements ILogger
{
	@Override
	public void trace(String msg)
	{
		System.out.println(msg);
	}

	@Override
	public void trace(String msg, Object... objects)
	{
		System.out.println(String.format(msg, objects));
	}

	@Override
	public void trace(String msg, Throwable throwable)
	{
		System.out.println(msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void debug(String msg)
	{
		System.out.println(msg);
	}

	@Override
	public void debug(String msg, Object... objects)
	{
		System.out.println(String.format(msg, objects));
	}

	@Override
	public void debug(String msg, Throwable throwable)
	{
		System.out.println(msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void info(String msg)
	{
		System.out.println(msg);
	}

	@Override
	public void info(String msg, Object... objects)
	{
		System.out.println(String.format(msg, objects));
	}

	@Override
	public void info(String msg, Throwable throwable)
	{
		System.out.println(msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void warn(String msg)
	{
		System.out.println(msg);
	}

	@Override
	public void warn(String msg, Object... objects)
	{
		System.out.println(String.format(msg, objects));
	}

	@Override
	public void warn(String msg, Throwable throwable)
	{
		System.out.println(msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void error(String msg)
	{
		System.out.println(msg);
	}

	@Override
	public void error(String msg, Object... objects)
	{
		System.out.println(String.format(msg, objects));
	}

	@Override
	public void error(String msg, Throwable throwable)
	{
		System.out.println(msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void trace(Class c, String msg)
	{
		System.out.println(c.getClass().getName() + " " + msg);
	}

	@Override
	public void trace(Class c, String msg, Object... objects)
	{
		System.out.println(c.getClass().getName() + " " + String.format(msg, objects));
	}

	@Override
	public void trace(Class c, String msg, Throwable throwable)
	{
		System.out.println(c.getClass().getName() + " " + msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void debug(Class c, String msg)
	{
		System.out.println(c.getClass().getName() + " " + msg);
	}

	@Override
	public void debug(Class c, String msg, Object... objects)
	{
		System.out.println(c.getClass().getName() + " " + String.format(msg, objects));
	}

	@Override
	public void debug(Class c, String msg, Throwable throwable)
	{
		System.out.println(c.getClass().getName() + " " + msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void info(Class c, String msg)
	{
		System.out.println(c.getClass().getName() + " " + msg);
	}

	@Override
	public void info(Class c, String msg, Object... objects)
	{
		System.out.println(c.getClass().getName() + " " + String.format(msg, objects));
	}

	@Override
	public void info(Class c, String msg, Throwable throwable)
	{
		System.out.println(c.getClass().getName() + " " + msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void warn(Class c, String msg)
	{
		System.out.println(c.getClass().getName() + " " + msg);
	}

	@Override
	public void warn(Class c, String msg, Object... objects)
	{
		System.out.println(c.getClass().getName() + " " + String.format(msg, objects));
	}

	@Override
	public void warn(Class c, String msg, Throwable throwable)
	{
		System.out.println(c.getClass().getName() + " " + msg);
		throwable.printStackTrace(System.out);
	}

	@Override
	public void error(Class c, String msg)
	{
		System.out.println(c.getClass().getName() + " " + msg);
	}

	@Override
	public void error(Class c, String msg, Object... objects)
	{
		System.out.println(c.getClass().getName() + " " + String.format(msg, objects));
	}

	@Override
	public void error(Class c, String msg, Throwable throwable)
	{
		System.out.println(c.getClass().getName() + " " + msg);
		throwable.printStackTrace(System.out);
	}
}
