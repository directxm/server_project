package com.x.wechat.common.response;

/**
* 类名: Video </br>
* 描述: 视频model </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class Video
{
	// 媒体文件id
	private String mediaId;
	// 缩略图的媒体id
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
}
