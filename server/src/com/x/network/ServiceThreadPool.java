package com.x.network;

import com.x.logging.Logger;
import com.x.util.ExceptionUtils;
import com.x.util.TimeUtils;

import java.sql.Timestamp;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/19/16 3:28 PM
 */
public class ServiceThreadPool
{
	private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = (t, e) -> Logger.warn(ServiceThreadPool.class, "ServiceThreadPool thread {} caught exception: {} {}", t.getName(), e.toString(), ExceptionUtils.getExceptionStackTraceFileLineAndMethod(e));

	private final String name;
	private final int threadCount;
	final ExecutorService executorService;
	final ScheduledExecutorService scheduledExecutorService;

	public ServiceThreadPool(String name, int threadCount)
	{
		this.name = name;
		this.threadCount = Math.max(1, threadCount);

		executorService = Executors.newFixedThreadPool(threadCount, new ThreadFactory()
		{
			private final SecurityManager s = System.getSecurityManager();
			private final ThreadGroup group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			private final AtomicInteger threadNumber = new AtomicInteger(1);
			private final String namePrefix = "" + ServiceThreadPool.this.name + "(" + ServiceThreadPool.this.threadCount + ")-";

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
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
		{
			private final SecurityManager s = System.getSecurityManager();
			private final ThreadGroup group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			private final AtomicInteger threadNumber = new AtomicInteger(1);
			private final String namePrefix = ServiceThreadPool.this.name + "-Timer";

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
	}

	public String getName()
	{
		return name;
	}

	public int getThreadCount()
	{
		return threadCount;
	}

	public void execute(Runnable command)
	{
		executorService.execute(command);
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
	{
		return scheduledExecutorService.scheduleWithFixedDelay(() -> executeRunnable(command), initialDelay, delay, unit);
	}

	public void executeRunnable(final Runnable runnable)
	{
		executorService.execute(runnable);
	}

	public void executeRunnable(final Runnable runnable, int waitseconds)
	{
		scheduledExecutorService.schedule(() -> executeRunnable(runnable), waitseconds, TimeUnit.SECONDS);
	}

	public void executeRunnable(final Runnable runnable, long milliseconds)
	{
		scheduledExecutorService.schedule((Runnable)() -> executeRunnable(runnable), milliseconds, TimeUnit.MILLISECONDS);
	}

	public void executeRunnable(final Runnable runnable, int waitseconds, int periodSeconds)
	{
		scheduledExecutorService.scheduleAtFixedRate((Runnable)() -> executeRunnable(runnable), waitseconds, periodSeconds, TimeUnit.SECONDS);
	}

	public void executeRunnable(final Runnable runnable, long milliSeconds, long periodMilliSeconds)
	{
		scheduledExecutorService.scheduleAtFixedRate((Runnable)() -> executeRunnable(runnable), milliSeconds, periodMilliSeconds, TimeUnit.MILLISECONDS);
	}

	public void executeRunnable(final Runnable runnable, Timestamp date)
	{
		long initialDelay = Math.max(0L, date.getTime() - TimeUtils.timeMills());
		executeRunnable(runnable, initialDelay);
	}

	public void executeRunnable(final Runnable runnable, Timestamp date, int periodSeconds)
	{
		long initialDelay = Math.max(0L, date.getTime() - TimeUtils.timeMills());
		executeRunnable(runnable, initialDelay, periodSeconds * 1000L);
	}

	public void executeRunnable(final Runnable runnable, Timestamp date, long periodMilliSeconds)
	{
		long initialDelay = Math.max(0L, date.getTime() - TimeUtils.timeMills());
		executeRunnable(runnable, initialDelay, periodMilliSeconds);
	}

	public void shutdown()
	{
		executorService.shutdown();
		scheduledExecutorService.shutdown();
	}
}
