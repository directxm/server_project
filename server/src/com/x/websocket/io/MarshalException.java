package com.x.websocket.io;

import javax.websocket.EncodeException;
import java.nio.ByteBuffer;

/**
 * 类名: MarshalException </br>
 * 描述:  </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-11-11 </br>
 * 发布版本: V1.0 </br>
 */
public class MarshalException extends EncodeException
{
	public MarshalException(ByteBuffer bb, String message, Throwable cause)
	{
		super(bb, message, cause);
	}

	public MarshalException(String encodedString, String message, Throwable cause)
	{
		super(encodedString, message, cause);
	}

	public MarshalException(ByteBuffer bb, String message)
	{
		super(bb, message);
	}

	public MarshalException(String encodedString, String message)
	{
		super(encodedString, message);
	}
}
