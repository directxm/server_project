package com.x.wechat.common.response;

/**
* 类名: ImageMessage </br>
* 描述: 图片消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class ImageMessage extends BaseMessage
{
	private Image image;


	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}
}
