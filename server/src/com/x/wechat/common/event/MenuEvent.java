package com.x.wechat.common.event;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: MenuEvent </br>
* 描述: 自定义菜单事件 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class MenuEvent extends BaseEvent
{
	// 事件KEY值，与自定义菜单接口中KEY值对应
	private String eventKey;


	public String getEventKey()
	{
		return eventKey;
	}

	public void setEventKey(String eventKey)
	{
		this.eventKey = eventKey;
	}

	@Override
	public void parseXML(Element element)
	{
		this.eventKey = XMLUtils.getTextString(element, "EventKey");
		super.parseXML(element);
	}
}
