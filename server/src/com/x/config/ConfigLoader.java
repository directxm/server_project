package com.x.config;

import com.x.logging.impl.CommonLogger;
import com.x.lang.TimeSpan;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
* 类名: ConfigLoader </br>
* 描述: xml文件加载器 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-10 </br>
* 发布版本: V1.0 </br>
 */
public class ConfigLoader
{
	private static final ConfigLoader CONFIG_LOADER = new ConfigLoader();

	public static String getConfigVersion()
	{
		return CONFIG_LOADER.getVersion();
	}

	public static ConfigLoader getInstance()
	{
		return CONFIG_LOADER;
	}

	public static <T> T get(Class<T> tClass)
	{
		return CONFIG_LOADER.find(tClass);
	}

	public static void addConfigListener(ConfigListener listener)
	{
		CONFIG_LOADER.addListener(listener);
	}

	public static void addConfigMatcher(Matcher matcher)
	{
		CONFIG_LOADER.addMatcher(matcher);
	}

	public static <T> void registerConfig(Class<T> tClass, boolean load)
	{
		if(load)
		{
			CONFIG_LOADER.addLoadConfig(tClass);
		}
		else
		{
			CONFIG_LOADER.addUnloadConfig(tClass);
		}
	}

	public static void loadAll(ConfigFactory factory, String path) throws ConfigException
	{
		loadAll(factory, path, true, new ConfigExceptionHandler(new CommonLogger()));
	}

	public static void loadAll(ConfigFactory factory, String path, boolean watch) throws ConfigException
	{
		loadAll(factory, path, watch, new ConfigExceptionHandler(new CommonLogger()));
	}

	public static void loadAll(ConfigFactory factory, String path, boolean watch, ConfigExceptionHandler exceptionHandler) throws ConfigException
	{
		try
		{
			CONFIG_LOADER.setPath(path);
			CONFIG_LOADER.setWatch(watch);
			CONFIG_LOADER.setExceptionHandler(exceptionHandler);

			factory.registerConfigs();

			CONFIG_LOADER.reload();
		}
		catch(Exception e)
		{
			throw CONFIG_LOADER.handleException(e);
		}
	}

	protected boolean initialized = false;
	protected String path;
	protected final Set<Class<?>> loadSet = new LinkedHashSet<>();
	protected final Set<Class<?>> unloadSet = new LinkedHashSet<>();
	protected Map<Class<?>, Object> configs = new LinkedHashMap<>();
	protected final Matcher defaultMatcher = new Matcher()
	{
		final DateFormatTransformer dateFormatTransformer = new DateFormatTransformer(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
		final TimeSpanTransformer timeSpanTransformer = new TimeSpanTransformer();

		@Override
		public Transform match(Class aClass) throws Exception
		{
			if(aClass == Date.class) return dateFormatTransformer;
			if(aClass == TimeSpan.class) return timeSpanTransformer;
			return null;
		}
	};
	protected final List<Matcher> matcherList = new LinkedList<>();
	protected long configVersion = 0;
	protected final List<ConfigListener> listeners = new LinkedList<>();
	protected boolean watch = false;
	protected final AtomicLong watchSerial = new AtomicLong(0);
	protected ConfigExceptionHandler exceptionHandler = null;

	private ConfigLoader()
	{
		Worker worker = new Worker();
		worker.setName("ConfigLoaderWatcher");
		worker.setDaemon(true);
		worker.setPriority(Thread.MIN_PRIORITY);
		worker.start();
	}

	public String getPath()
	{
		synchronized(this)
		{
			return path;
		}
	}

	public void setPath(String path)
	{
		synchronized(this)
		{
			this.path = path;
		}
	}

	public void setWatch(boolean watch)
	{
		synchronized(this)
		{
			this.watch = watch;
			this.notifyAll();
		}
	}

	public ConfigException handleException(Exception e)
	{
		synchronized(this)
		{
			if(exceptionHandler != null)
			{
				return exceptionHandler.handleException(e);
			}
		}
		return new ConfigException(null, e);
	}

	public void setExceptionHandler(ConfigExceptionHandler exceptionHandler)
	{
		synchronized(this)
		{
			this.exceptionHandler = exceptionHandler;
		}
	}

	public String getVersion()
	{
		synchronized(this)
		{
			return String.valueOf(configVersion);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T find(Class<T> tClass)
	{
		T t;
		synchronized(this)
		{
			t = (T)configs.get(tClass);
		}
		if(t != null)
		{
			return t;
		}
		throw new RuntimeException("没找到需要的配置: " + tClass);
	}

	public void addListener(ConfigListener listener)
	{
		synchronized(listeners)
		{
			listeners.add(listener);
		}
	}

	public void addMatcher(Matcher matcher)
	{
		synchronized(matcherList)
		{
			matcherList.add(matcher);
		}
	}

	public <T> void addUnloadConfig(Class<T> tClass)
	{
		synchronized(unloadSet)
		{
			unloadSet.add(tClass);
		}
	}

	public <T> void addLoadConfig(Class<T> tClass)
	{
		synchronized(loadSet)
		{
			loadSet.add(tClass);
		}
	}

	public void reload() throws ConfigException
	{
		LoggerFactory.getLogger(this.getClass()).info("" + this.getClass().getSimpleName() + " reloading all!");
		File file = new File(getPath());
		long lastModified = file.lastModified();
		Map<Class<?>, Object> map = new LinkedHashMap<>();
		Matcher matcher = type -> {
			synchronized(matcherList)
			{
				for(Matcher m : matcherList)
				{
					Transform t = m.match(type);
					if(t != null)
					{
						return t;
					}
				}
			}
			return defaultMatcher.match(type);
		};
		synchronized(loadSet)
		{
			for(Class<?> tClass : loadSet)
			{
				Object t = load(tClass, matcher);
				map.put(tClass, t);
			}
		}
		synchronized(unloadSet)
		{
			for(Class<?> tClass : unloadSet)
			{
				if(map.get(tClass) == null)
				{
					try
					{
						map.put(tClass, tClass.newInstance());
					}
					catch(InstantiationException | IllegalAccessException e)
					{
						throw new ConfigException(tClass.getSimpleName(), "Class " + tClass.getSimpleName() + " has no public constructor.");
					}
				}
			}
		}
		synchronized(this)
		{
			Map<Class<?>, Object> temp_configs = configs;
			try
			{
				configs = map;
				for(Map.Entry<Class<?>, Object> entry : map.entrySet())
				{
					Class<?> tClass = entry.getKey();
					Object o = entry.getValue();
					try
					{
						tClass.getMethod("invokePreparePostLoad").invoke(o);
						//tClass.getMethod("dump").invoke(o);
					}
					catch(NoSuchMethodException | IllegalAccessException e)
					{
						LoggerFactory.getLogger(entry.getKey()).warn("[FATAL] method invokePreparePostLoad is not found or can't be access");
						throw new ConfigException(tClass.getSimpleName(), e);
					}
					catch(Exception e)
					{
						throw new ConfigException(tClass.getSimpleName(), e);
					}
				}
				for(Map.Entry<Class<?>, Object> entry : map.entrySet())
				{
					Class<?> tClass = entry.getKey();
					Object o = entry.getValue();
					try
					{
						tClass.getMethod("invokePostLoad").invoke(o);
						//tClass.getMethod("dump").invoke(o);
					}
					catch(NoSuchMethodException | IllegalAccessException ignored)
					{
						LoggerFactory.getLogger(entry.getKey()).warn("[Ignored] method invokePostLoad is not found or can't be access");
					}
					catch(Exception e)
					{
						throw new ConfigException(tClass.getSimpleName(), e);
					}
				}
				configVersion = System.currentTimeMillis();
				initialized = true;
				this.notifyAll();
			}
			catch(ConfigException e)
			{
				configs = temp_configs;
				throw e;
			}
		}
		watchSerial.set(lastModified);
		LoggerFactory.getLogger(this.getClass()).info("" + this.getClass().getSimpleName() + " reload all finish!");
	}

	private void updateListeners()
	{
		synchronized(listeners)
		{
			for(ConfigListener listener : listeners)
			{
				listener.onUpdate(getVersion());
			}
		}
	}

	private <T> T load(T t, Matcher matcher, File is) throws ConfigException
	{
		Serializer serializer = matcher == null ? new Persister() : new Persister(matcher);
		try
		{
			t = serializer.read(t, is);
			return t;
		}
		catch(Exception e)
		{
			throw new ConfigException(t.getClass().getSimpleName(), e);
		}
	}

	private <T> T load(Class<? extends T> type, Matcher matcher, File is) throws ConfigException
	{
		Serializer serializer = matcher == null ? new Persister() : new Persister(matcher);

		try
		{
			return serializer.read(type, is);
		}
		catch(Exception e)
		{
			throw new ConfigException(type.getSimpleName(), e);
		}
	}

	private <T> T load(Class<? extends T> type, Matcher matcher) throws ConfigException
	{
		String filePrefix = getPath() + File.separator + type.getSimpleName().toLowerCase();
		File is = new File(filePrefix + ".xml");
		if(is.exists() && is.isFile())
		{
			return load(type, matcher, is);
		}
		else
		{
			is = new File(filePrefix);
			if(is.exists() && is.isDirectory())
			{
				try
				{
					T t = type.newInstance();
					for(File file : is.listFiles(file1 -> file1.isFile() && file1.getName().endsWith(".xml")))
					{
						t = load(t, matcher, file);
					}
					return t;
				}
				catch(IllegalAccessException | InstantiationException e)
				{
					throw new ConfigException(type.getSimpleName(), "Class " + type.getSimpleName() + " has no public constructor.");
				}
			}
		}
		throw new ConfigException(type.getSimpleName(), "Can't find class " + type.getSimpleName() + "'s xml file or directory at [" + getPath() + "] .");
	}

	private class Worker extends Thread
	{
		private boolean isWatch = true;

		@Override
		public void run()
		{
			while(isWatch)
			{
				/// 判断是否初始化了
				synchronized(ConfigLoader.this)
				{
					while(!initialized)
					{
						try
						{
							ConfigLoader.this.wait(5000);
						}
						catch(InterruptedException ignored)
						{
						}
					}
				}
				synchronized(ConfigLoader.this)
				{
					while(!watch)
					{
						try
						{
							ConfigLoader.this.wait();
						}
						catch(InterruptedException ignored)
						{
						}
					}
				}
				{
					/// 判断是否要更新
					File file = new File(getPath());
					if(file.exists())
					{
						boolean updatable = false;
						if(file.lastModified() != watchSerial.get())
						{
							updatable = true;
						}
						if(updatable)
						{
							try
							{
								reload();
								updateListeners();
							}
							catch(Exception e)
							{
								LoggerFactory.getLogger(this.getClass()).warn("ConfigLoader reload", handleException(e));
							}
						}
					}

					/// 等待
					try
					{
						Thread.sleep(60 * 1000);
					}
					catch(InterruptedException e)
					{
						LoggerFactory.getLogger(this.getClass()).warn(e.getMessage(), e);
					}
				}
			}
		}
	}
}
