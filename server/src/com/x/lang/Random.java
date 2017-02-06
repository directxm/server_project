package com.x.lang;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yanfengbing
 * @since 13-7-3 PM4:37
 */
public class Random
{
	private static final java.util.Random __RANDOM__ = new java.util.Random();

	static
	{
		__RANDOM__.setSeed(System.currentTimeMillis());
	}

	public Random()
	{

	}

	public static int randomInt(int i)
	{
		return __RANDOM__.nextInt(i);
	}

	public static int randomInt(int min, int max)
	{
		return min + randomInt(max - min);
	}

	public static double randomDouble()
	{
		return __RANDOM__.nextDouble();
	}

	public static long randomLong(long i)
	{
		long n = __RANDOM__.nextLong();
		if(n < 0) n = -n;
		return n % i;
	}

	public static long randomLong(long min, long max)
	{
		return min + randomLong(max - min);
	}

	public static <T> T random(Collection<T> collection)
	{
		if(collection == null) return null;
		int size = collection.size();
		if(size == 0) return null;
		int n = __RANDOM__.nextInt(size);
		Iterator<T> iterator = collection.iterator();
		int i = 0;
		while(iterator.hasNext())
		{
			T e = iterator.next();
			if(i == n)
			{
				return e;
			}
			++i;
		}
		return null;
	}

	public static <K, V> Map.Entry<K, V> random(Map<K, V> map)
	{
		if(map == null) return null;
		int size = map.size();
		if(size == 0) return null;
		int n = __RANDOM__.nextInt(size);
		Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
		int i = 0;
		while(iterator.hasNext())
		{
			Map.Entry<K, V> e = iterator.next();
			if(i == n)
			{
				return e;
			}
			++i;
		}
		return null;
	}
}
