package com.game;

import com.x.wechat.data.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 类名: UserManager </br>
 * 描述: 用户管理 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-11-24 </br>
 * 发布版本: V1.0 </br>
 */
public class UserManager
{
	private static final ConcurrentHashMap<String, User> USERS = new ConcurrentHashMap<>();

	public static User get(String openId)
	{
		return USERS.get(openId);
	}

	public static void add(User user)
	{
		if(user == null) return;
		USERS.put(user.getOpenId(), user);
	}

	public static User remove(String openId)
	{
		User user = USERS.remove(openId);
		return user;
	}

}
