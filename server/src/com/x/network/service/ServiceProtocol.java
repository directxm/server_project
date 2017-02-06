package com.x.network.service;

/**
 * 仅支持TCP。
 *
 * @author yanfengbing
 * @version 1.0
 * @since 6/30/15 5:22 PM
 */
public enum ServiceProtocol
{
	TCP("fs.tcp");

	private String prefix;

	ServiceProtocol(String prefix)
	{
		this.prefix = prefix;
	}

	public String prefix()
	{
		return prefix;
	}

	public static ServiceProtocol parse(String s)
	{
		if(s == null) throw new IllegalArgumentException("protocol is null");
		if(s.equals(TCP.prefix()))
		{
			return TCP;
		}
		throw new IllegalArgumentException("protocol is not valid");
	}
}
