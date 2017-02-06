package com.x.database.cache.actor;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/26/16 4:37 PM
 */
@Deprecated
public abstract class Producer
{
	private int id;

	public Producer(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public String getAbbr()
	{
		return "Producer" + id;
	}

	public void dispose()
	{
		ProducerManager.getInstance().removeProducer(this);
	}

	@Override
	public String toString()
	{
		return "Producer{" +
				"id=" + id +
				'}';
	}
}
