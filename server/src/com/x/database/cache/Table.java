package com.x.database.cache;

import com.x.util.JSONUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/16/16 1:33 PM
 */

@Entity
@javax.persistence.Table(name = "t")
public class Table implements Value, Comparable<Table>
{
	private TableKey key;
	private byte[] value;
	private long accessTime;
	private int version = 0;

	public Table()
	{
	}

	public Table(TableKey key, byte[] value)
	{
		this.key = key;
		this.value = value;
		this.accessTime = System.currentTimeMillis();
	}

	@Id
	public String getId()
	{
		return JSONUtils.toJSONString(key);
	}

	public void setId(String id)
	{
		this.key = JSONUtils.parseObject(id, TableKey.class);
	}

	@Lob
	public byte[] getValue()
	{
		return value;
	}

	public void setValue(byte[] value)
	{
		this.value = value;
	}

	public synchronized long getAccessTime()
	{
		return accessTime;
	}

	public synchronized void setAccessTime(long accessTime)
	{
		this.accessTime = accessTime;
	}

	@Override
	public TableKey key()
	{
		return key;
	}

	@Override
	public synchronized int version()
	{
		return version;
	}

	public synchronized void version(int version)
	{
		this.version = version;
	}

	@Override
	public synchronized void updateVersion()
	{
		++version;
	}

	@Override
	public synchronized boolean isExpired(long now)
	{
		return now - accessTime > 86400L * 1000L;
	}

	@Override
	public synchronized void touch()
	{
		accessTime = System.currentTimeMillis();
	}

	@Override
	public String toString()
	{
		return "Table{" + key + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Table table = (Table)o;

		return key != null ? key.equals(table.key) : table.key == null;
	}

	@Override
	public int hashCode()
	{
		return key != null ? key.hashCode() : 0;
	}

	@Override
	public int compareTo(Table o)
	{
		if(o == null) throw new NullPointerException("CompareTable to Null");
		if(this == o) return 0;
		if(this.equals(o)) return 0;
		if(key == null && o.key == null) return 0;
		if(key == null) return -1;
		if(o.key == null) return 1;
		return this.key.compareTo(o.key);
	}
}
