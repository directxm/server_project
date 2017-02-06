package com.x.wechat.common;

import org.dom4j.Element;

import java.io.Serializable;

/**
 * 类名: Request </br>
 * 描述: 微信请求接口 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-12 </br>
 * 发布版本: V1.0 </br>
 */
@Deprecated
public abstract class Request implements Cloneable, Serializable
{
	public abstract void parseXML(Element element);

	public abstract Element toXML();
}
