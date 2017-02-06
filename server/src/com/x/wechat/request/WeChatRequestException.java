package com.x.wechat.request;

import com.x.io.CodeException;
import com.x.io.ErrorCode;

/**
 * 类名: WeChatRequestException </br>
 * 描述: 微信异常基类 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 */
public class WeChatRequestException extends CodeException
{
	public WeChatRequestException(ErrorCode errorCode)
	{
		super(errorCode);
	}

	public WeChatRequestException(String s, Throwable throwable)
	{
		super(s, throwable);
	}
}
