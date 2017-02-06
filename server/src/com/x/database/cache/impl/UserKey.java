package com.x.database.cache.impl;

import com.x.database.cache.Key;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/26/14 5:30 PM
 */
@Deprecated
public class UserKey implements Key
{
	protected int channel = 0;
	protected String uid = "";
	protected int serverId = 0;

	UserKey()
	{
		this(0, "", 0);
	}

	public UserKey(int channel, String uid, int serverId)
	{
		this.uid = uid;
		this.channel = channel;
		this.serverId = serverId;
	}

	@Override
	public UserKey clone() throws CloneNotSupportedException
	{
		return (UserKey)super.clone();
	}

	public int getChannel()
	{
		return channel;
	}

	public void setChannel(int channel)
	{
		this.channel = channel;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public int getServerId()
	{
		return serverId;
	}

	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		UserKey userKey = (UserKey)o;

		if(channel != userKey.channel) return false;
		if(serverId != userKey.serverId) return false;
		if(uid != null ? !uid.equals(userKey.uid) : userKey.uid != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = uid != null ? uid.hashCode() : 0;
		result = 31 * result + channel;
		result = 31 * result + serverId;
		return result;
	}

	@Override
	public String toString()
	{
		return "[" + uid + "@" + channel + "#" + serverId + "]";
	}

	@Override
	public int hash(int bound)
	{
		if(bound <= 1) return 0;
		if(uid != null && uid.length() > 0)
		{
			return Math.abs(uid.codePointAt(0)) % bound;
		}
		return 0;
	}
}
