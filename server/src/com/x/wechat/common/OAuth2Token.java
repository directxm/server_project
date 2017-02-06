package com.x.wechat.common;

import com.alibaba.fastjson.JSONObject;
import com.x.util.JSONUtils;

/**
 * 类名: OAuth2Token </br>
 * 描述: 网页授权信息 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
public class OAuth2Token
{
	// 网页授权接口调用凭证
    private String accessToken;
    // 凭证有效时长
    private int expiresIn;
    // 用于刷新凭证
    private String refreshToken;
    // 用户标识
    private String openId;
    // 用户授权作用域
    private String scope;

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public int getExpiresIn()
	{
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn)
	{
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken()
	{
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken)
	{
		this.refreshToken = refreshToken;
	}

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public String toJSONString()
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("access_token", accessToken);
		jsonObject.put("expires_in", expiresIn);
		jsonObject.put("refresh_token", refreshToken);
		jsonObject.put("openid", openId);
		jsonObject.put("scope", scope);

		return jsonObject.toJSONString();
	}

	public void parseJSON(JSONObject jsonObject)
	{
		if(jsonObject != null)
		{
			accessToken = JSONUtils.getString(jsonObject, "access_token");
			expiresIn = JSONUtils.getInteger(jsonObject, "expires_in");
			refreshToken = JSONUtils.getString(jsonObject, "refresh_token");
			openId = JSONUtils.getString(jsonObject, "openid");
			scope = JSONUtils.getString(jsonObject, "scope");
		}
	}

}
