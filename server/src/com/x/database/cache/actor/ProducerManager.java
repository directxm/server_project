package com.x.database.cache.actor;

import com.x.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 给Center和Consumer使用
 *
 * @author yanfengbing
 * @version 1.0
 * @since 3/26/16 3:49 PM
 */
@Deprecated
public class ProducerManager
{
	private static final ProducerManager instance = new ProducerManager();

	public static ProducerManager getInstance()
	{
		return instance;
	}

	private ConcurrentHashMap<Integer, Producer> producers = new ConcurrentHashMap<>();

	public Producer getProducer(int producerId)
	{
		return producers.get(producerId);
	}

	public void addProducer(Producer producer)
	{
		if(producers.containsKey(producer.getId()))
			throw new IllegalArgumentException("Same Producer Id " + producer.getId());
		producers.put(producer.getId(), producer);
		Logger.info(CacheProducer.class, "Producer add {}", producer);
	}

	public void removeProducer(Producer producer)
	{
		producers.remove(producer.getId());
		Logger.info(CacheProducer.class, "Producer remove {}", producer);
	}

	public Map<Integer, String> dumpProcuders()
	{
		Map<Integer, String> map = new HashMap<>();
		for(Producer producer : producers.values())
		{
			//map.put(producer.getId(), producer.getAddress().toString());
		}
		return map;
	}
}
