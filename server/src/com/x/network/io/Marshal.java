package com.x.network.io;

/**
 * 类名: Marshal </br>
 * 描述: 序列化接口 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-11-9 </br>
 * 发布版本: V1.0 </br>
 */
@Deprecated
public interface Marshal
{
	String marshal() throws Exception;
	void unmarshal(String str) throws Exception;
}
