package com.x.config;

/**
* 类名: IConfigListener </br>
* 描述: xml内容变化的监听接口 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public interface ConfigListener
{
	void onUpdate(String version);
}
