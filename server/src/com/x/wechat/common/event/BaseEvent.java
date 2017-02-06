package com.x.wechat.common.event;

//import com.x.wechat.request.IRequest;
import com.x.util.XMLUtils;
import com.x.wechat.common.Request;
import org.dom4j.Element;

/**
* 类名: BaseEvent </br>
* 描述: 事件基类 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class BaseEvent extends Request
{
	// register factory

	// 开发者微信号
	private String toUserName;
	// 发送方帐号（一个OpenID）
	private String fromUserName;
	// 消息创建时间 （整型）
	private long createTime;
	// 消息类型(event)
	private String msgType;
	// 事件类型(subscribe/unsubscribe/scan/LOCATION/CLICK)
	private String event;


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

	public String getEvent()
	{
		return event;
	}

	public void setEvent(String event)
	{
		this.event = event;
	}

	@Override
	public Request clone() throws CloneNotSupportedException
	{
		return null;
	}

	@Override
	public void parseXML(Element element)
	{
		//Element e = element.element("MsgType");

		// 开发者微信号
		this.toUserName = XMLUtils.getTextString(element, "ToUserName");
		// 发送方帐号（一个OpenID）
		this.fromUserName = XMLUtils.getTextString(element, "FromUserName");
		// 消息创建时间 （整型）
		this.createTime = XMLUtils.getTextLong(element, "CreateTime");
		// 消息类型(event)
		this.msgType = XMLUtils.getTextString(element, "MsgType");
		// 事件类型(subscribe/unsubscribe/scan/LOCATION/CLICK)
		this.event = XMLUtils.getTextString(element, "Event");
	}

	@Override
	public Element toXML()
	{
		return null;
	}
}
