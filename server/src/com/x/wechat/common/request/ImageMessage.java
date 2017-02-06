package com.x.wechat.common.request;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: ImageMessage </br>
* 描述: 请求消息之图片消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class ImageMessage extends BaseMessage
{
    // 图片链接
    private String picUrl;
    private String mediaId;


	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getMediaId()
	{
		return mediaId;
	}

	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}

	@Override
	public void parseXML(Element element)
	{
		this.picUrl = XMLUtils.getTextString(element, "PicUrl");
		this.mediaId = XMLUtils.getTextString(element, "MediaId");

		super.parseXML(element);
	}
}