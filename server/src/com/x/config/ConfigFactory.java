package com.x.config;

/**
* 类名: IConfigFactory </br>
* 描述: 工厂类接口,用于注册xml文件 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public interface ConfigFactory
{
	void registerConfigs() throws ConfigException;
}
