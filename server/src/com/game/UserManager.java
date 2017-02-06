package com.game;

import com.x.wechat.data.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ����: UserManager </br>
 * ����: �û����� </br>
 * ������Ա: fatum </br>
 * ����ʱ��: 16-11-24 </br>
 * �����汾: V1.0 </br>
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
