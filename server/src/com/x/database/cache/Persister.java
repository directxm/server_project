package com.x.database.cache;

import java.util.Map;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 2/13/14 11:31 AM
 */
public interface Persister<T extends Value>
{
	T find(Key key);

	void save(T value);

	void remove(Key value);

	/**
	 * 加载的时候 可以根据 bound 和 cacheId 进行一次过滤
	 * 减少不必要加载进来的用户
	 *
	 * @param bound   cache 的 数量
	 * @param cacheId 目前Cache 的Id
	 * @return 返回所有load的数据
	 */
	Map<Key, T> loadAll(int bound, int cacheId);
}
