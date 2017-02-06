package com.x.wechat.common.response;

/**
* 类名: VideoMessage </br>
* 描述: 视频消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class VideoMessage extends BaseMessage
{
	// 视频
	private Video video;

	public Video getVideo()
	{
		return video;
	}

	public void setVideo(Video video)
	{
		this.video = video;
	}
}
