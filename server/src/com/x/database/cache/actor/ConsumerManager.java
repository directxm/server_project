package com.x.database.cache.actor;

import com.x.logging.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 给Center和Producer用的
 *
 * @author yanfengbing
 * @version 1.0
 * @since 3/26/16 3:49 PM
 */
@Deprecated
public class ConsumerManager
{
	private static final ConsumerManager instance = new ConsumerManager();

	public static ConsumerManager getInstance()
	{
		return instance;
	}

	private final ConcurrentHashMap<Integer, Consumer> consumers = new ConcurrentHashMap<>();

	public Consumer getConsumer(int consumerId)
	{
		return consumers.get(consumerId);
	}

	public void addConsumer(Consumer consumer)
	{
		if(consumers.containsKey(consumer.getId()))
			throw new IllegalArgumentException("Same Consumer Id " + consumer.getId());
		consumers.put(consumer.getId(), consumer);
		Logger.info(CacheConsumer.class, "Consumer add {}", consumer);
	}

	public void removeConsumer(Consumer consumer)
	{
		consumers.remove(consumer.getId());
		Logger.info(CacheConsumer.class, "Consumer remove {}", consumer);
	}

	/*public void broadcast(ServiceWrapper service, Object message)
	{
		for(Consumer consumer : consumers.values())
		{
			service.tell(consumer.getServicePath(), message);
		}
	}*/

	public int getConsumerCount()
	{
		return consumers.size();
	}
}
