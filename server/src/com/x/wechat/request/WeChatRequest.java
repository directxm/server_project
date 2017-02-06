package com.x.wechat.request;

import java.io.Serializable;

/**
 * 类名: IRequest </br>
 * 描述: 微信请求接口 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 */
public interface WeChatRequest extends Cloneable, Serializable
{
	WeChatRequest clone() throws CloneNotSupportedException;
}
