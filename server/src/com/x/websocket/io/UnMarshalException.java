package com.x.websocket.io;

import javax.websocket.DecodeException;
import java.nio.ByteBuffer;

/**
 * 类名: UnMarshalException </br>
 * 描述:  </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-11-11 </br>
 * 发布版本: V1.0 </br>
 */
public class UnMarshalException extends DecodeException
{
	public UnMarshalException(ByteBuffer bb, String message, Throwable cause)
	{
		super(bb, message, cause);
	}

	public UnMarshalException(String encodedString, String message, Throwable cause)
	{
		super(encodedString, message, cause);
	}

	public UnMarshalException(ByteBuffer bb, String message)
	{
		super(bb, message);
	}

	public UnMarshalException(String encodedString, String message)
	{
		super(encodedString, message);
	}
}
