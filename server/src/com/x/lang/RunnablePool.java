package com.x.lang;

import com.x.logging.Logger;
import com.x.util.ExceptionUtils;
import com.x.util.TimeUtils;

import java.sql.Timestamp;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanfengbing
 * @since 13-6-24 下午4:13
 */
public class RunnablePool
{
	private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = (t, e) -> Logger.warn(RunnablePool.class, "RunnablePool thread {} caught exception: {} {}", t.getName(), e.toString(), ExceptionUtils.getExceptionStackTraceFileLineAndMethod(e));
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory()
	{
		private final SecurityManager s = System.getSecurityManager();
		private final ThreadGroup group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix = "RunnablePool-";

		@Override
		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(group, r,
					namePrefix + threadNumber.getAndIncrement(),
					0);
			t.setUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
			if(t.isDaemon())
				t.setDaemon(false);
			if(t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	});
	private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
	{
		private final SecurityManager s = System.getSecurityManager();
		private final ThreadGroup group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix = "RunnablePool-Timer-";

		@Override
		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(group, r,
					namePrefix + threadNumber.getAndIncrement(),
					0);
			t.setUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
			if(t.isDaemon())
				t.setDaemon(false);
			if(t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	});

	public static void executeRunnable(final Runnable runnable)
	{
		EXECUTOR_SERVICE.execute(runnable);
	}

	public static void executeRunnable(final Runnable runnable, int waitseconds)
	{
		SCHEDULED_EXECUTOR_SERVICE.schedule((Runnable)() -> executeRunnable(runnable), waitseconds, TimeUnit.SECONDS);
	}

	public static void executeRunnable(final Runnable runnable, long milliseconds)
	{
		SCHEDULED_EXECUTOR_SERVICE.schedule((Runnable)() -> executeRunnable(runnable), milliseconds, TimeUnit.MILLISECONDS);
	}

	public static void executeRunnable(final Runnable runnable, int waitseconds, int periodSeconds)
	{
		SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate((Runnable)() -> executeRunnable(runnable), waitseconds, periodSeconds, TimeUnit.SECONDS);
	}

	public static void executeRunnable(final Runnable runnable, long milliSeconds, long periodMilliSeconds)
	{
		SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate((Runnable)() -> executeRunnable(runnable), milliSeconds, periodMilliSeconds, TimeUnit.MILLISECONDS);
	}

	public static void executeRunnable(final Runnable runnable, Timestamp date)
	{
		long initialDelay = Math.max(0L, date.getTime() - TimeUtils.timeMills());
		executeRunnable(runnable, initialDelay);
	}

	public static void executeRunnable(final Runnable runnable, Timestamp date, int periodSeconds)
	{
		long initialDelay = Math.max(0L, date.getTime() - TimeUtils.timeMills());
		executeRunnable(runnable, initialDelay, periodSeconds * 1000L);
	}

	public static void executeRunnable(final Runnable runnable, Timestamp date, long periodMilliSeconds)
	{
		long initialDelay = Math.max(0L, date.getTime() - TimeUtils.timeMills());
		executeRunnable(runnable, initialDelay, periodMilliSeconds);
	}
}
