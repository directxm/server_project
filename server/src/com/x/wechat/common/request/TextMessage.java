package com.x.wechat.common.request;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: TextMessage </br>
* 描述: 请求消息之文本消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class TextMessage extends BaseMessage
{
    // 消息内容
    private String content;


	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	@Override
	public void parseXML(Element element)
	{
		this.content = XMLUtils.getTextString(element, "Content");

		super.parseXML(element);
	}
}