package com.x.config;

/**
* 类名: ConfigException </br>
* 描述: 异常 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class ConfigException extends Exception
{
	private final String configName;

	public ConfigException(String configName)
	{
		this.configName = configName;
	}

	public ConfigException(String configName, String message)
	{
		super(message);
		this.configName = configName;
	}

	public ConfigException(String configName, String message, Throwable cause)
	{
		super(message, cause);
		this.configName = configName;
	}

	public ConfigException(String configName, Throwable cause)
	{
		super(cause);
		this.configName = configName;
	}

	public String getConfigName()
	{
		return configName;
	}
}
