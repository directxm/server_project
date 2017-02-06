package com.x.wechat.common.request;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: VideoMessage </br>
* 描述: 请求消息之视频消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class VideoMessage extends BaseMessage
{
    // 媒体ID
    private String mediaId;
    // 语音格式
    private String thumbMediaId;


	public String getMediaId()
	{
		return mediaId;
	}

	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}

	public String getThumbMediaId()
	{
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId)
	{
		this.thumbMediaId = thumbMediaId;
	}

	@Override
	public void parseXML(Element element)
	{
		this.thumbMediaId = XMLUtils.getTextString(element, "ThumbMediaId");
		this.mediaId = XMLUtils.getTextString(element, "MediaId");

		super.parseXML(element);
	}
}