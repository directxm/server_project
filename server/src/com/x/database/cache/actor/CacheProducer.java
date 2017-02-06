package com.x.database.cache.actor;

import com.x.database.cache.Cache;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/26/16 3:04 PM
 */
@Deprecated
public abstract class CacheProducer
{
	private Cache tableCache;

	public CacheProducer(Cache cache, int bound, int id)
	{
		tableCache = cache;
		tableCache.init(bound, id);
	}

	public void start() throws Exception
	{

	}

	public void stop()
	{
		tableCache.interrupt();
		tableCache.saveAll();
		//super.stop();
	}

	public void forceSave()
	{
		tableCache.interrupt();
		tableCache.saveAll();
	}

	public boolean isStopped()
	{
		return ConsumerManager.getInstance().getConsumerCount() == 0 && tableCache.isFinished();
	}
}
