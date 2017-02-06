package com.x.config;

import com.x.logging.ILogger;
import com.x.logging.impl.SystemOutLogger;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.ValueRequiredException;

import java.text.MessageFormat;

/**
* 类名: ConfigExceptionHandler </br>
* 描述: 异常处理 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class ConfigExceptionHandler
{
	private final ILogger logger;

	public ConfigExceptionHandler(ILogger logger)
	{
		this.logger = logger;
	}

	public ConfigException handleException(Exception e)
	{
		ILogger log = logger;
		if(log == null)
		{
			log = new SystemOutLogger();
		}
		if(e != null)
		{
			Throwable t = e;
			int i = 0;
			String configName = null;
			while(t != null)
			{
				if(t instanceof ConfigException)
				{
					configName = ((ConfigException)t).getConfigName();
					log.error(ConfigLoader.class, "{} {} {}", i, getFileLineAndMethod(t), formatException((Exception)t));
				}
				else if(t instanceof PersistenceException)
				{
					if(t instanceof ValueRequiredException)
					{
						log.error(ConfigLoader.class, "{} {} {}", i, getFileLineAndMethod(t), formatException((ValueRequiredException)t));
					}
					else if(t instanceof ElementException)
					{
						log.error(ConfigLoader.class, "{} {} {}", i, getFileLineAndMethod(t), formatException((ElementException)t));
					}
					else
					{
						log.error(ConfigLoader.class, "{} {} {}", i, getFileLineAndMethod(t), formatException((PersistenceException)t));
					}
				}
				else if(t instanceof NumberFormatException)
				{
					log.error(ConfigLoader.class, "{} {} {}", i, getFileLineAndMethod(t), formatException((NumberFormatException)t));
				}
				else
				{
					log.error(ConfigLoader.class, "{} {} {}", i, getFileLineAndMethod(t), formatException((Exception)t));
				}
				t = t.getCause();
				++i;
			}
			if(e instanceof ConfigException)
			{
				return (ConfigException)e;
			}
			else
			{
				return new ConfigException(configName, e);
			}
		}
		return null;
	}

	private String formatException(PersistenceException t)
	{
		return "读取错误：" + t.getClass().getSimpleName() + ": " + t.getMessage();
	}

	private String formatException(Exception t)
	{
		return "错误：" + t.getClass().getSimpleName() + ": " + t.getMessage();
	}

	private String formatException(ValueRequiredException t)
	{
		String message = t.getMessage();
		if(message != null)
		{
			int index = message.indexOf("on");
			if(index >= 0)
			{
				message = message.substring(index);
			}
		}
		return "需要某个字段，但是没有填：Empty value " + message;
	}

	private String formatException(ElementException t)
	{
		return "字段配置里面有，但是代码中并没有写，一般将Root的strict改成false即可：" + t.getMessage();
	}

	private String formatException(NumberFormatException t)
	{
		return "某个需要填数字的字段填的不是数字：" + t.getMessage();
	}

	private String getFileLineAndMethod(Throwable t)
	{
		if(t == null)
			return "";
		StackTraceElement[] stackTraceElements = t.getStackTrace();
		if(stackTraceElements == null || stackTraceElements.length == 0)
			return "";
		return MessageFormat.format("{} {}", "" + stackTraceElements[0].getFileName() + ":" + stackTraceElements[0].getLineNumber() + ":", stackTraceElements[0].getMethodName());
	}
}
