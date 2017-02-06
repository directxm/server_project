package com.x.wechat.common.request;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: LinkMessage </br>
* 描述: 请求消息之链接消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class LinkMessage extends BaseMessage
{
    // 消息标题
    private String title;
    // 消息描述
    private String description;
    // 消息链接
    private String url;


	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	public void parseXML(Element element)
	{
		this.title = XMLUtils.getTextString(element, "Title");
		this.description = XMLUtils.getTextString(element, "Description");
		this.url = XMLUtils.getTextString(element, "Url");

		super.parseXML(element);
	}
}