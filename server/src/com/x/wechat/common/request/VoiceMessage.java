package com.x.wechat.common.request;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: VoiceMessage </br>
* 描述: 请求消息之语音消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class VoiceMessage extends BaseMessage
{
    // 媒体ID
    private String mediaId;
    // 语音格式
    private String format;


	public String getMediaId()
	{
		return mediaId;
	}

	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	@Override
	public void parseXML(Element element)
	{
		this.format = XMLUtils.getTextString(element, "Format");
		this.mediaId = XMLUtils.getTextString(element, "MediaId");

		super.parseXML(element);
	}
}