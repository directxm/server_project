package com.x.wechat.common.response;

/**
* 类名: BaseMessage </br>
* 描述: 消息基类（公众帐号 -> 普通用户） </br>
* 开发人员： fatum </br>
* 创建时间： 16-10-8 </br>
* 发布版本：V1.0 </br>
 */
public class BaseMessage
{
	// 接收方帐号（收到的OpenID）
	private String toUserName;
	// 开发者微信号
	private String fromUserName;
	// 消息创建时间 （整型）
	private long createTime;
	// 消息类型
	private String msgType;

	public void parseXML(String xml)
	{
	}

	public String toXML()
	{
		return "";
	}


	public String getToUserName()
	{
		return toUserName;
	}

	public void setToUserName(String toUserName)
	{
		this.toUserName = toUserName;
	}

	public String getFromUserName()
	{
		return fromUserName;
	}

	public void setFromUserName(String fromUserName)
	{
		this.fromUserName = fromUserName;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public String getMsgType()
	{
		return msgType;
	}

	public void setMsgType(String msgType)
	{
		this.msgType = msgType;
	}
}
