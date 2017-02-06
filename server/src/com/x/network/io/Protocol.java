package com.x.network.io;

import java.io.Serializable;


/**
 * ����: Protocol </br>
 * ����: ����ͨ��Э�� </br>
 * ������Ա: fatum </br>
 * ����ʱ��: 16-11-9 </br>
 * �����汾: V1.0 </br>
 */
public interface Protocol extends Cloneable, Serializable
{
	int getType();

	//int getSerial();

	//void setSerial(int serial);

	Protocol clone() throws CloneNotSupportedException;
}
