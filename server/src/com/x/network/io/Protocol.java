package com.x.network.io;

import java.io.Serializable;


/**
 * 类名: Protocol </br>
 * 描述: 网络通信协议 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-11-9 </br>
 * 发布版本: V1.0 </br>
 */
public interface Protocol extends Cloneable, Serializable
{
	int getType();

	//int getSerial();

	//void setSerial(int serial);

	Protocol clone() throws CloneNotSupportedException;
}
