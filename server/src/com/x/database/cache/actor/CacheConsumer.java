package com.x.database.cache.actor;

import com.x.io.CodeException;

@Deprecated
public abstract class CacheConsumer
{
	private final int id;
	protected CacheProducer cacheProducer = null;

	protected CacheConsumer(int id)
	{
		this.id = id;
	}

	public final int getId()
	{
		return id;
	}

	public void start()
	{

	}

	public boolean isConnected()
	{
		return true;
	}

	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public abstract byte[] get(String key) throws CodeException;
	public abstract void delete(String key);
	public abstract void save(String key, byte[] value, int version);
}
