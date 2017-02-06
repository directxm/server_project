package com.x.wechat.common.response;

/**
* 类名: MusicMessage </br>
* 描述: 音乐消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class MusicMessage extends BaseMessage
{
	// 音乐
	private Music music;

	public Music getMusic()
	{
		return music;
	}

	public void setMusic(Music music)
	{
		this.music = music;
	}
}
