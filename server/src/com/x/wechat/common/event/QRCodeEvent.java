package com.x.wechat.common.event;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: QRCodeEvent </br>
* 描述: 扫描带参数二维码事件 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class QRCodeEvent extends BaseEvent
{
	// 事件KEY值
	private String eventKey;
	// 用于换取二维码图片
	private String ticket;


	public String getEventKey()
	{
		return eventKey;
	}

	public void setEventKey(String eventKey)
	{
		this.eventKey = eventKey;
	}

	public String getTicket()
	{
		return ticket;
	}

	public void setTicket(String ticket)
	{
		this.ticket = ticket;
	}

	@Override
	public void parseXML(Element element)
	{
		this.eventKey = XMLUtils.getTextString(element, "EventKey");
		this.ticket = XMLUtils.getTextString(element, "Ticket");

		super.parseXML(element);
	}
}
