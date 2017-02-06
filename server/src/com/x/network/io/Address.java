package com.x.network.io;

import com.x.util.CompareUtils;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 6/17/15 2:19 PM
 */
public class Address implements Serializable, Comparable<Address>
{
	private final InetSocketAddress address;

	public Address(String ip, int port)
	{
		this.address = new InetSocketAddress(ip, port);
	}

	public Address(InetSocketAddress address)
	{
		this.address = address;
	}

	public InetSocketAddress getAddress()
	{
		return address;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Address that = (Address)o;

		if(address == null || that.address == null)
		{
			return address == null && that.address == null;
		}
		int port1 = address.getPort();
		int port2 = that.address.getPort();
		if(port1 != port2)
		{
			return false;
		}
		InetAddress inetAddress1 = address.getAddress();
		InetAddress inetAddress2 = that.address.getAddress();
		if(inetAddress1 == null || inetAddress2 == null)
		{
			return address.equals(that.address);
		}
		return inetAddress1.isAnyLocalAddress() && inetAddress2.isAnyLocalAddress() || inetAddress1.equals(inetAddress2);
	}

	@Override
	public int hashCode()
	{
		if(address == null)
		{
			return 0;
		}
		else
		{
			InetAddress inetAddress = address.getAddress();
			int port = address.getPort();
			if(inetAddress == null || inetAddress.isAnyLocalAddress())
			{
				return port;
			}
			else
			{
				return address.hashCode();
			}
		}
	}

	@Override
	public String toString()
	{
		return address == null ? "/" : address.getAddress().getHostAddress() + ":" + address.getPort();
	}

	public boolean available()
	{
		return address != null;
	}

	public static Address parse(String addressStr)
	{
		if(addressStr == null) return new Address(null);
		String[] p = addressStr.split(":");
		if(p.length != 2) return new Address(null);
		return new Address(p[0], Integer.parseInt(p[1]));
	}

	@Override
	public int compareTo(Address address)
	{
		if(address == null) throw new NullPointerException("Address");
		if(this == address) return 0;
		if(this.equals(address)) return 0;
		if(this.address == null) return -1;
		if(address.address == null) return 1;
		int port1 = this.address.getPort();
		int port2 = address.address.getPort();
		if(port1 != port2)
		{
			return port1 - port2;
		}
		InetAddress inetAddress1 = this.address.getAddress();
		InetAddress inetAddress2 = address.address.getAddress();
		if(inetAddress1 == null && inetAddress2 == null)
		{
			return 0;
		}
		if(inetAddress1 == null) return -1;
		if(inetAddress2 == null) return 1;
		return CompareUtils.compare(inetAddress1.getAddress(), inetAddress2.getAddress());
	}
}
