package com.x.wechat.common.response;

import java.util.List;

/**
* 类名: NewsMessage </br>
* 描述: 文本消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class NewsMessage extends BaseMessage
{
	// 图文消息个数，限制为10条以内
	private int articleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> articles;


	public int getArticleCount()
	{
		return articleCount;
	}

	public void setArticleCount(int articleCount)
	{
		this.articleCount = articleCount;
	}

	public List<Article> getArticles()
	{
		return articles;
	}

	public void setArticles(List<Article> articles)
	{
		this.articles = articles;
	}
}
