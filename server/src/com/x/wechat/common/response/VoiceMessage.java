package com.x.wechat.common.response;

/**
* 类名: VoiceMessage </br>
* 描述: 语音消息</br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class VoiceMessage extends BaseMessage
{
	// 语音
	private Voice voice;

	public Voice getVoice()
	{
		return voice;
	}

	public void setVoice(Voice voice)
	{
		this.voice = voice;
	}
}
