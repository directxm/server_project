package com.x.lang;

import com.x.util.TimeUtils;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
* 类名: TimeSpan </br>
* 描述: 时间间隔类 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-10 </br>
* 发布版本: V1.0 </br>
 */
public class TimeSpan implements Comparable<TimeSpan>
{
	public final static TimeSpan ZERO = new TimeSpan(0);

	private long _totalMilliSeconds = 0;

	/**
	 * 当天的时分秒
	 */
	public TimeSpan()
	{
		this(new Time(TimeUtils.timeMills()));
	}

	public TimeSpan(long totalMilliSeconds)
	{
		_totalMilliSeconds = totalMilliSeconds;
	}

	public TimeSpan(Date afterDate, Date beforeDate)
	{
		this(afterDate.getTime() - beforeDate.getTime());
	}

	public TimeSpan(Time time)
	{
		Calendar now = Calendar.getInstance();
		now.setTime(time);
		int hours = now.get(Calendar.HOUR_OF_DAY);
		int minutes = now.get(Calendar.MINUTE);
		int seconds = now.get(Calendar.SECOND);
		_totalMilliSeconds = (hours * 60L * 60L + minutes * 60L + seconds) * 1000L;
	}

	public TimeSpan(int hours, int minutes, int seconds)
	{
		_totalMilliSeconds = (hours * 60L * 60L + minutes * 60L + seconds) * 1000L;
	}

	public TimeSpan(int days, int hours, int minutes, int seconds)
	{
		_totalMilliSeconds = (days * 24L * 60L * 60L + hours * 60L * 60L + minutes * 60L + seconds) * 1000L;
	}

	public long getTotalSeconds()
	{
		return _totalMilliSeconds == 0 ? 0 : _totalMilliSeconds / 1000L;
	}
	public long getMilliSeconds()
	{
		return _totalMilliSeconds;
	}
	public long  getSeconds()
	{
		return (_totalMilliSeconds % (1000L * 60L * 60L * 24L) % (1000L * 60L * 60L) % (1000L * 60L)) / 1000L;
	}
	public long getMinutes()
	{
		return (_totalMilliSeconds % (1000L * 60L * 60L * 24L) % (1000L * 60L * 60L)) / (1000L * 60L);
	}
	public long getHours()
	{
		return (_totalMilliSeconds % (1000L * 60L * 60L * 24L)) / (1000L * 60L * 60L);
	}

	public long getDays()
	{
		return _totalMilliSeconds / (1000L * 60L * 60L * 24L);
	}

	@Override
	public int compareTo(TimeSpan timeSpan)
	{
		if(this == timeSpan) return 0;
		if(timeSpan == null) throw new NullPointerException("TimeSpan compare timeSpan=null");
		long diff = this._totalMilliSeconds - timeSpan._totalMilliSeconds;
		if(diff < 0) return -1;
		if(diff > 0) return 1;
		return 0;
	}

	public boolean after(TimeSpan timeSpan)
	{
		return timeSpan == null || this._totalMilliSeconds > timeSpan._totalMilliSeconds;
	}
	public boolean before(TimeSpan timeSpan)
	{
		return timeSpan != null && this._totalMilliSeconds < timeSpan._totalMilliSeconds;
	}

	public static TimeSpan parse(String s) throws ParseException
	{
		String[] ss = s.split("\\.|:");
		if(ss.length != 3 && ss.length != 4) throw new ParseException("More Than three ':', " + s, -1);

		if(ss.length == 4)
		{
			int days = Integer.parseInt(ss[0]);
			int hours = Integer.parseInt(ss[1]);
			int minites = Integer.parseInt(ss[2]);
			int seconds = Integer.parseInt(ss[3]);
			return new TimeSpan(days, hours, minites, seconds);
		}
		else
		{
			int hours = Integer.parseInt(ss[0]);
			int minites = Integer.parseInt(ss[1]);
			int seconds = Integer.parseInt(ss[2]);
			return new TimeSpan(hours, minites, seconds);
		}
	}

	public String format()
	{
		if(getDays() <= 0)
			return String.format(getHours() < 10 ? "%02d:%02d:%02d" : "%d:%02d:%02d", getHours(), getMinutes(), getSeconds());
		else
			return String.format(getHours() < 10 ? "%d:%02d:%02d:%02d" : "%d:%d:%02d:%02d", getDays(), getHours(), getMinutes(), getSeconds());
	}

	@Override
	public String toString()
	{
		return format();
	}
}
