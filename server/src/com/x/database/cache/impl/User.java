package com.x.database.cache.impl;

import com.x.database.cache.VersionSaveable;

import java.sql.Timestamp;

/**
 * @author yanfengbing
 * @since 13-7-4 PM3:35
 */
@Deprecated
public class User extends VersionSaveable
{
	protected UserKey key = null;
	protected String identity = "";
	protected String username = "";
	protected String os = "";
	protected String address = "";
	protected Timestamp createTime = null;
	protected long roleId = 0;

	public User()
	{
	}

	public User(UserKey key, String identity)
	{
		this.key = key;
		this.identity = identity;
	}

	public UserKey getKey()
	{
		return key;
	}

	public void setKey(UserKey key)
	{
		this.key = key;
	}

	public String getIdentity()
	{
		return identity;
	}

	public void setIdentity(String identity)
	{
		this.identity = identity;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getOs()
	{
		return os;
	}

	public void setOs(String os)
	{
		this.os = os;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(long roleId)
	{
		this.roleId = roleId;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		User o = (User)super.clone();
		o.key = key == null ? null : key.clone();
		return o;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		User user = (User)o;

		return !(key != null ? !key.equals(user.key) : user.key != null);
	}

	@Override
	public int hashCode()
	{
		return key != null ? key.hashCode() : 0;
	}

	@Override
	public String toString()
	{
		return "User:" + key.toString() + "[" + roleId + "]";
	}

	@Override
	public Object key()
	{
		return key;
	}
}
