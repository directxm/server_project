package com.x.database.cache;

import com.x.database.cache.actor.CacheConsumer;
import com.x.io.CodeRuntimeException;
import com.x.io.CoreErrorCode;
import com.x.util.JSONUtils;
import com.x.logging.Logger;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 5/7/14 5:25 PM
 */
@Deprecated
public class Procedure
{
	private static CacheConsumer cacheConsumer;

	public static void init(CacheConsumer cacheConsumer)
	{
		Procedure.cacheConsumer = cacheConsumer;
	}

	public <T> T getValue(Class<T> tClass, Object id)
	{
		long nano = System.nanoTime();
		try
		{
			TableKey key = new TableKey(tClass, id);
			byte[] value = cacheConsumer.get(key.toString());
			return JSONUtils.parseObject(value, tClass);
		}
		catch(Exception e)
		{
			throw new CodeRuntimeException(CoreErrorCode.FATAL, e);
		}
		finally
		{
			Logger.debug(this.getClass(), "GetTable {} {}@{}", System.nanoTime() - nano, tClass, id);
		}
	}

	public <T extends VersionSaveable> void save(T t)
	{
		if(null == t) return;
		long nano = System.nanoTime();
		try
		{
			TableKey key = new TableKey(t.getClass(), t.key());
			t.setVersion(t.getVersion() + 1);
			//Logger.debug(Benchmark.class, "SaveTable0.01 {} {}@{} {}", System.nanoTime() - nano, t.getClass().getSimpleName(), t.key(), t.getVersion());
			cacheConsumer.save(key.toString(), JSONUtils.toJSONBytes(t), t.getVersion());
		}
		finally
		{
			Logger.debug(this.getClass(), "SaveTable {} {}@{}", System.nanoTime() - nano, t.getClass().getSimpleName(), t.key());
		}
	}

	public <T extends Saveable> void delete(T t)
	{
		if(null == t) return;
		long nano = System.nanoTime();
		try
		{
			TableKey key = new TableKey(t.getClass(), t.key());
			cacheConsumer.delete(key.toString());
		}
		finally
		{
			Logger.debug(this.getClass(), "RemoveTable {} {}@{}", System.nanoTime() - nano, t.getClass().getSimpleName(), t.key());
		}
	}

	public static Procedure current()
	{
		return instance;
	}

	private static final Procedure instance = new Procedure();
}
