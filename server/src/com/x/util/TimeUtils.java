package com.x.util;

import java.sql.Timestamp;
import java.util.TimeZone;

/**
* 类名: OSUtils </br>
* 描述: json工具 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class TimeUtils
{
	private static long Offset = 0;

	public static long time()
	{
		return (System.currentTimeMillis() + Offset) / 1000;
	}

	public static Timestamp now()
	{
		return new Timestamp(timeMills());
	}

	public static long timeMills()
	{
		return System.currentTimeMillis() + Offset;
	}

	/**
	 * 这个方法只能在启动的时候执行一次，且在整个程序运行期间不得调用
	 *
	 * @param offset 偏移的毫秒数
	 */
	public static void setTimeOffset(long offset)
	{
		Offset = offset;
	}

	public static long timezone()
	{
		TimeZone timeZone = TimeZone.getDefault();
		return timeZone.getOffset(timeMills()) / 1000L;
	}
}
