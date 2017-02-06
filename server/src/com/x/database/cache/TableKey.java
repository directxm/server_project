package com.x.database.cache;

import com.x.util.JSONUtils;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/26/14 5:32 PM
 */
public class TableKey implements Key, Comparable<TableKey>
{
	protected String agent;
	protected String id;

	public TableKey()
	{
	}

	public TableKey(Class<?> agent, Object id)
	{
		this.agent = agent.getName();
		this.id = id == null ? "" : id.toString();
	}

	public String getAgent()
	{
		return agent;
	}

	public void setAgent(String agent)
	{
		this.agent = agent;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		TableKey tableKey = (TableKey)o;

		if(agent != null ? !agent.equals(tableKey.agent) : tableKey.agent != null) return false;
		return id != null ? id.equals(tableKey.id) : tableKey.id == null;
	}

	@Override
	public int hashCode()
	{
		int result = agent != null ? agent.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}

	@Override
	public int hash(int bound)
	{
		if(bound <= 1 || id == null) return 0;
		return Math.abs(id.hashCode()) % bound;
	}

	@Override
	public String toString()
	{
		return JSONUtils.toJSONString(this);
	}

	@Override
	public int compareTo(TableKey o)
	{
		if(o == null) throw new NullPointerException("TableKey::compareTo object is null");
		if(this == o) return 0;
		if(this.equals(o)) return 0;
		int i = compare(agent, o.agent);
		if(i == 0)
		{
			i = compare(id, o.id);
		}
		return i;
	}

	private static int compare(String o1, String o2)
	{
		if(o1 == null && o2 == null) return 0;
		if(o1 != null && o2 == null) return 1;
		if(o1 == null) return -1;
		return o1.compareTo(o2);
	}
}