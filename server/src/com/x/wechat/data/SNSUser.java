package com.x.wechat.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.x.util.JSONUtils;

import java.util.List;

/**
 * 类名: SNSUser </br>
 * 描述: 网页授权获取的用户信息 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
public class SNSUser
{
	// 用户标识
    private String openId;
    // 用户昵称
    private String nickName;
    // 性别（1是男性，2是女性，0是未知）
    private int sex;
    // 国家
    private String country;
    // 省份
    private String province;
    // 城市
    private String city;
    // 用户头像链接
    private String headImgUrl;
	// 用户的语言，简体中文为zh_CN
    private String language;
    // 用户特权信息
    private List<String> privilegeList;

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getHeadImgUrl()
	{
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl)
	{
		this.headImgUrl = headImgUrl;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public List<String> getPrivilegeList()
	{
		return privilegeList;
	}

	public void setPrivilegeList(List<String> privilegeList)
	{
		this.privilegeList = privilegeList;
	}

	public String toJSONString()
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("openid", openId);
		jsonObject.put("nickname", nickName);
		jsonObject.put("sex", sex);
		jsonObject.put("country", country);
		jsonObject.put("province", province);
		jsonObject.put("city", city);
		jsonObject.put("headimgurl", headImgUrl);
		jsonObject.put("language", language);

		JSONArray array = new JSONArray();
		for(String o : privilegeList)
		{
			array.add(o);
		}
		jsonObject.put("privilegeList", array);

		return jsonObject.toJSONString();
	}

	public void parseJSON(JSONObject jsonObject)
	{
		if(jsonObject != null)
		{
			openId = JSONUtils.getString(jsonObject, "openid");
			nickName = JSONUtils.getString(jsonObject, "nickname");
			sex = JSONUtils.getInteger(jsonObject, "sex");
			country = JSONUtils.getString(jsonObject, "country");
			province = JSONUtils.getString(jsonObject, "province");
			city = JSONUtils.getString(jsonObject, "city");
			headImgUrl = JSONUtils.getString(jsonObject, "headimgurl");
			language = JSONUtils.getString(jsonObject, "language");

			JSONArray array = JSONUtils.getJSONArray(jsonObject, "privilegeList");
			if(array != null)
			{
				for(Object o : array)
				{
					privilegeList.add(o.toString());
				}
			}
		}
	}
}
