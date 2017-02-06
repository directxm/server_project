package com.x.logging;

import com.x.util.OSUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
* 类名: Logger </br>
* 描述: 日志类 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class Logger
{
	/**
	 * WARNING!!!! DO NOT DELETE THIS NEVER!!!
	 * This is used to initialize Logger, which will load xml file.
	 * 这里坚决不能删掉，用来初始化Logger
	 */
	static
	{
		String logDir = System.getProperty("log.dir");
		if(logDir == null)
		{
			if(OSUtils.isLinux())
			{
				System.setProperty("log.dir", "/export/logs");
			}
			else
			{
				System.setProperty("log.dir", System.getProperty("java.io.tmpdir"));
			}
		}

		//DOMConfigurator.configure("log4j.xml");//加载.xml文件
		LoggerFactory.getLogger("root");
	}

	public static void init()
	{

	}

	public static String getContext(String name)
	{
		return MDC.get(name);
	}

	public static void putContext(String name, String value)
	{
		MDC.put(name, value);
	}

	public static void removeContext(String name)
	{
		MDC.remove(name);
	}

	public static void trace(String msg)
	{
		LoggerFactory.getLogger(Logger.class).trace(msg);
	}

	public static void trace(String msg, Object... objects)
	{
		LoggerFactory.getLogger(Logger.class).trace(msg, objects);
	}

	public static void trace(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(Logger.class).trace(msg, throwable);
	}

	public static void debug(String msg)
	{
		LoggerFactory.getLogger(Logger.class).debug(msg);
	}

	public static void debug(String msg, Object... objects)
	{
		LoggerFactory.getLogger(Logger.class).debug(msg, objects);
	}

	public static void debug(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(Logger.class).debug(msg, throwable);
	}

	public static void info(String msg)
	{
		LoggerFactory.getLogger(Logger.class).info(msg);
	}

	public static void info(String msg, Object... objects)
	{
		LoggerFactory.getLogger(Logger.class).info(msg, objects);
	}

	public static void info(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(Logger.class).info(msg, throwable);
	}

	public static void warn(String msg)
	{
		LoggerFactory.getLogger(Logger.class).warn(msg);
	}

	public static void warn(String msg, Object... objects)
	{
		LoggerFactory.getLogger(Logger.class).warn(msg, objects);
	}

	public static void warn(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(Logger.class).warn(msg, throwable);
	}

	public static void error(String msg)
	{
		LoggerFactory.getLogger(Logger.class).error(msg);
	}

	public static void error(String msg, Object... objects)
	{
		LoggerFactory.getLogger(Logger.class).error(msg, objects);
	}

	public static void error(String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(Logger.class).debug(msg, throwable);
	}

	public static void trace(Class c, String msg)
	{
		LoggerFactory.getLogger(c).trace(msg);
	}

	public static void trace(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).trace(msg, objects);
	}

	public static void trace(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).trace(msg, throwable);
	}

	public static void debug(Class c, String msg)
	{
		LoggerFactory.getLogger(c).debug(msg);
	}

	public static void debug(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).debug(msg, objects);
	}

	public static void debug(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).debug(msg, throwable);
	}

	public static void info(Class c, String msg)
	{
		LoggerFactory.getLogger(c).info(msg);
	}

	public static void info(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).info(msg, objects);
	}

	public static void info(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).info(msg, throwable);
	}

	public static void warn(Class c, String msg)
	{
		LoggerFactory.getLogger(c).warn(msg);
	}

	public static void warn(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).warn(msg, objects);
	}

	public static void warn(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).warn(msg, throwable);
	}

	public static void error(Class c, String msg)
	{
		LoggerFactory.getLogger(c).error(msg);
	}

	public static void error(Class c, String msg, Object... objects)
	{
		LoggerFactory.getLogger(c).error(msg, objects);
	}

	public static void error(Class c, String msg, Throwable throwable)
	{
		LoggerFactory.getLogger(c).debug(msg, throwable);
	}
}
