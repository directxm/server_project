package com.x.wechat.request;

import com.alibaba.fastjson.JSONObject;
//import com.app.WeChatServlet;
//import com.x.logging.Logger;
import com.x.process.Processor;
import com.x.wechat.common.AccessToken;
import com.x.wechat.common.Request;
import com.x.wechat.util.HttpsUtils;
//import org.omg.PortableServer._ServantLocatorStub;


/**
 * 类名: RequestProcessor </br>
 * 描述: 微信请求处理 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-18 </br>
 * 发布版本: V1.0 </br>
 */
public abstract class WeChatRequestProcessor<T extends Request> implements Processor<T>
{
	protected AccessToken accessToken;
	protected T request;

	protected WeChatRequestProcessor()
	{
	}

	public abstract String process(T request) throws WeChatRequestException;

	/*public final String processRequest(WeChatServlet servlet, T request) throws RequestException
	{
		long nano = System.nanoTime();
		try
		{
			this.servlet = servlet;
			this.accessToken = servlet.getAccessToken();
			this.request = request;
			return processRequest(request);
		}
		finally
		{
			Logger.debug(this.getClass(), "processRequest {} {}", System.nanoTime() - nano, request.getClass().getSimpleName());
		}
	}*/

	/**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) throws WeChatRequestException
    {
	    return HttpsUtils.httpsRequest(requestUrl, requestMethod, outputStr);
    }

	public void clear()
	{
		this.accessToken = null;
		this.request = null;
	}

}
