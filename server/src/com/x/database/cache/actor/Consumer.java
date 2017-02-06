package com.x.database.cache.actor;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/26/16 4:37 PM
 */
@Deprecated
public abstract class Consumer
{
	private int id;

	public Consumer(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void dispose()
	{
		ConsumerManager.getInstance().removeConsumer(this);
	}

	@Override
	public String toString()
	{
		return "Consumer{" +
				"id=" + id +
				'}';
	}
}
