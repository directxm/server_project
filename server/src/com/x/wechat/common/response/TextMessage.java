package com.x.wechat.common.response;

/**
* 类名: TextMessage </br>
* 描述: 文本消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class TextMessage extends BaseMessage
{
	// 回复的消息内容
	private String content;


	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
