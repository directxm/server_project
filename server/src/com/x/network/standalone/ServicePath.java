package com.x.network.standalone;

import com.x.network.io.Address;

import java.io.Serializable;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 6/29/15 4:16 PM
 */
public class ServicePath implements Serializable, Comparable<ServicePath>
{
	private final long pid;
	private final Address address;

	public ServicePath(long pid, Address address)
	{
		this.pid = pid;
		this.address = address;
	}

	public long getPid()
	{
		return pid;
	}

	public Address getAddress()
	{
		return address;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		ServicePath that = (ServicePath)o;

		return pid == that.pid;

	}

	@Override
	public int hashCode()
	{
		return (int)(pid ^ (pid >>> 32));
	}

	@Override
	public String toString()
	{
		return "ServicePath{" +
				"pid=" + pid +
				", address=" + address +
				'}';
	}

	@Override
	public int compareTo(ServicePath o)
	{
		return (int)(this.pid - o.pid);
	}
}
