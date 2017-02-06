package com.x.lang;

/**
 * 只统计当初始化或者reset后,过了多少毫秒,并不提供当前时间获取的方法,如果想取得当前时间,请看: @see com.freejoy.util.TimeUtil
 *
 * @author yanfengbing
 * @version 1.0
 * @since 7/7/15 7:03 PM
 */
public class TimeWatcher
{
	private long t = System.currentTimeMillis();

	public synchronized long startTime()
	{
		return t;
	}

	public synchronized void reset()
	{
		t = System.currentTimeMillis();
	}

	public synchronized long elapse()
	{
		return System.currentTimeMillis() - t;
	}
}
