package com.x.wechat.data;


import com.x.database.cache.VersionSaveable;
import com.x.util.TimeUtils;

import java.sql.Timestamp;

/**
 * 类名: User </br>
 * 描述: 核心用户类 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 *           进入游戏时如果是新建用户在拉取 WeChatUser 创建并且存盘, 否则读取数据库(loading 过程中)
 */
//@Entity
//@javax.persistence.Table(name = "user")
public class User extends VersionSaveable//implements Value, Comparable<User>
{
	//private UserKey key;

	/////////////////////////WeChatUser/////////////////////////////////
	// 用户的标识
    private String openId;
    // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
    private int subscribe;
    // 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    private String subscribeTime;
    // 昵称
    private String nickName;
    // 用户的性别（1是男性，2是女性，0是未知）
    private int sex;
    // 用户所在国家
    private String country;
    // 用户所在省份
    private String province;
    // 用户所在城市
    private String city;
    // 用户的语言，简体中文为zh_CN
    private String language;
	// 用户头像链接
    private String headImgUrl;

	///////////////////////binding information//////////////////////////////
	protected String phone;
	protected String qq;

	///////////////////////////////////////////////////////////////

	protected long cashAdd = 0; // 充值
	protected long cashSystem = 0; // 系统赠送
	protected long cashUse = 0; // 花费
	protected long cashBonus = 0; // 赢得

	protected int cashDailyUse = 0; // 当日花费
	protected int cashDailyBonus = 0; // 当日赢得


	protected String inviteCode = "";
	protected String inviterInviteCode = "";

	protected Timestamp lastLoginTime;
	protected Timestamp lastLogoutTime;
	protected Timestamp dailyUpdateTime;

	// 未開大獎轉數
	// 當日轉數
	// 當日入幣或吐幣數
	// 近幾日大獎次數


	protected User()
	{
	}

	/*protected User(String openId)
	{
		this.openId = openId;
	}*/

	public static User create(SNSUser snsUser)
	{
		User user = new User();

		// 用户标识
	    user.openId = snsUser.getOpenId();
	    // 用户昵称
	    user.nickName = snsUser.getNickName();
	    // 性别（1是男性，2是女性，0是未知）
	    user.sex = snsUser.getSex();
	    // 国家
	    user.country = snsUser.getCountry();
	    // 省份
	    user.province = snsUser.getProvince();
	    // 城市
	    user.city = snsUser.getCity();
	    // 用户头像链接
	    user.headImgUrl = snsUser.getHeadImgUrl();
		// 用户的语言，简体中文为zh_CN
	    user.language = snsUser.getLanguage();

		return user;
	}

	public static User create(WeChatUser weChatUser)
	{
		User user = new User();


		return user;
	}

	public static User test(String key)
	{
		User user = new User();
		user.openId = key;

		return user;
	}

	/*@Override
	public int compareTo(User o)
	{
		return 0;
	}

	@Override
	public int version()
	{
		return 0;
	}

	@Override
	public Key key()
	{
		return null;
	}

	@Override
	public void updateVersion()
	{

	}

	// 用于定时清理
	@Override
	public boolean isExpired(long now)
	{
		return false;
	}

	@Override
	public void touch()
	{

	}*/

	@Override
	public Object key()
	{
		return getOpenId();
	}

	public String getKey()
	{
		return getOpenId();
	}

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public int getSubscribe()
	{
		return subscribe;
	}

	public void setSubscribe(int subscribe)
	{
		this.subscribe = subscribe;
	}

	public String getSubscribeTime()
	{
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime)
	{
		this.subscribeTime = subscribeTime;
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

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getHeadImgUrl()
	{
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl)
	{
		this.headImgUrl = headImgUrl;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public long getCashAdd()
	{
		return cashAdd;
	}

	public void setCashAdd(long cashAdd)
	{
		this.cashAdd = cashAdd;
	}

	public long getCashSystem()
	{
		return cashSystem;
	}

	public void setCashSystem(long cashSystem)
	{
		this.cashSystem = cashSystem;
	}

	public long getCashUse()
	{
		return cashUse;
	}

	public void setCashUse(long cashUse)
	{
		this.cashUse = cashUse;
	}

	public long getCashBonus()
	{
		return cashBonus;
	}

	public void setCashBonus(long cashBonus)
	{
		this.cashBonus = cashBonus;
	}

	public int getCashDailyUse()
	{
		return cashDailyUse;
	}

	public void setCashDailyUse(int cashDailyUse)
	{
		this.cashDailyUse = cashDailyUse;
	}

	public int getCashDailyBonus()
	{
		return cashDailyBonus;
	}

	public void setCashDailyBonus(int cashDailyBonus)
	{
		this.cashDailyBonus = cashDailyBonus;
	}

	public String getInviteCode()
	{
		return inviteCode;
	}

	public void setInviteCode(String inviteCode)
	{
		this.inviteCode = inviteCode;
	}

	public String getInviterInviteCode()
	{
		return inviterInviteCode;
	}

	public void setInviterInviteCode(String inviterInviteCode)
	{
		this.inviterInviteCode = inviterInviteCode;
	}

	public Timestamp getLastLoginTime()
	{
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime)
	{
		this.lastLoginTime = lastLoginTime;
	}

	public Timestamp getLastLogoutTime()
	{
		return lastLogoutTime;
	}

	public void setLastLogoutTime(Timestamp lastLogoutTime)
	{
		this.lastLogoutTime = lastLogoutTime;
	}

	public Timestamp getDailyUpdateTime()
	{
		return dailyUpdateTime;
	}

	public void setDailyUpdateTime(Timestamp dailyUpdateTime)
	{
		this.dailyUpdateTime = dailyUpdateTime;
	}
}
