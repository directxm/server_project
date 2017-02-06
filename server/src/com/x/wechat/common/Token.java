package com.x.wechat.common;

/**
 * 类名: Token </br>
 * 描述: 凭证 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 *           废除由 AccessToken 代替
 */
@Deprecated
public class Token
{
	// 接口访问凭证
    private String accessToken;
    // 凭证有效期，单位：秒
    private int expiresIn;


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
}
