package com.x.wechat.common;

/**
 * 类名: IJSONSerializable </br>
 * 描述: JSON序列化接口 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 */
public interface JSONSerializable
{
	public void parseJSON(String str);
	public String toJSON();
}
