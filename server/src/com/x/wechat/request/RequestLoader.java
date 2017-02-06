package com.x.wechat.request;

import com.x.io.Loader;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 类名: RequestLoader </br>
 * 描述: 请求数据预加载器 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 *           废除
 */
public abstract class RequestLoader implements Loader<String, WeChatRequest>
{
	protected final ConcurrentHashMap<String, WeChatRequest> requests = new ConcurrentHashMap<>();

	public RequestLoader()
	{

	}

	@Override
	public WeChatRequest load(String t)
	{
		//return requests.get(t).clone();
		return null;
	}
}
