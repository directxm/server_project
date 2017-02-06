package com.x.database.cache;

import java.io.Serializable;

/**
 * 不是线程安全的，所有实现类都要自己处理线程事宜
 *
 * @author yanfengbing
 * @version 1.0
 * @since 2/11/14 3:56 PM
 */
public interface Value extends Serializable, Cloneable
{
	/**
	 * 获得版本号
	 *
	 * @return 当前版本
	 */
	int version();

	/**
	 * 获得key
	 *
	 * @return Value的key
	 */
	Key key();

	/**
	 * 增加版本号
	 */
	void updateVersion();

	/**
	 * 判断Value是否过期
	 *
	 * @param now 当前时间，以毫秒为单位
	 * @return 是否过期
	 */
	boolean isExpired(long now);

	/**
	 * 更新访问时间
	 */
	void touch();
}
