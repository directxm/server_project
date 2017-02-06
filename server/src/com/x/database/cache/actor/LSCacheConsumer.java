package com.x.database.cache.actor;

import com.x.io.CodeException;
import com.x.logging.Logger;

/**
 * Created by fatum on 2016/11/24.
 */
@Deprecated
public class LSCacheConsumer extends CacheConsumer
{
	public LSCacheConsumer(int id) throws Exception
	{
		super(id);
	}

	@Override
	public byte[] get(String key) throws CodeException
	{
		if(key == null) return null;
		long nano = System.nanoTime();
		try
		{

		}
		finally
		{
			Logger.debug(this.getClass(), "get table {} {}", System.nanoTime() - nano, key);
		}
		return new byte[0];
	}

	@Override
	public void delete(String key)
	{
		if(key == null) return;
		long nano = System.nanoTime();
		try
		{



		}
		finally
		{
			Logger.debug(this.getClass(), "get table {} {}", System.nanoTime() - nano, key);
		}

	}

	@Override
	public void save(String key, byte[] value, int version)
	{

	}
}
