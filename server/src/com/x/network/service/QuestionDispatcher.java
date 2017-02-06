package com.x.network.service;

import com.x.network.io.Rpc;
import com.x.logging.Logger;
import com.x.network.io.ResponseWatcher;
import com.x.network.io.Protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/24/15 2:32 PM
 */
final class QuestionDispatcher
{
	private final ConcurrentHashMap<Long, Map<Integer, AskResponseWatcher>> __askMap__ = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Map<Integer, HttpResponseWatcher>> __httpMap__ = new ConcurrentHashMap<>();
	private final LinkedList<ResponseWatcher> list = new LinkedList<>();

	protected QuestionDispatcher()
	{
	}

	public AskResponseWatcher call(ServiceCoroutine coroutine, long pid, Protocol sendMessage, int timeout)
	{
		AskResponseWatcher watcher = new AskResponseWatcher(coroutine, pid, sendMessage, timeout);
		watcher.getRpc().setRequest();
		synchronized(__askMap__)
		{
			Map<Integer, AskResponseWatcher> map = __askMap__.get(pid);
			if(map == null)
			{
				map = new HashMap<>();
				__askMap__.put(pid, map);
			}
			map.put(watcher.getRpc().getId(), watcher);
		}
		return watcher;
	}

	public BlockedAskResponseWatcher call(long pid, Protocol sendMessage, int timeout)
	{
		BlockedAskResponseWatcher watcher = new BlockedAskResponseWatcher(pid, sendMessage, timeout);
		watcher.getRpc().setRequest();
		synchronized(__askMap__)
		{
			Map<Integer, AskResponseWatcher> map = __askMap__.get(pid);
			if(map == null)
			{
				map = new HashMap<>();
				__askMap__.put(pid, map);
			}
			map.put(watcher.getRpc().getId(), watcher);
		}
		return watcher;
	}

	public HttpResponseWatcher call(ServiceCoroutine coroutine, long pid, String url, int timeout)
	{
		HttpResponseWatcher watcher = new HttpResponseWatcher(coroutine, pid, url, timeout);
		synchronized(__httpMap__)
		{
			Map<Integer, HttpResponseWatcher> map = __httpMap__.get(url);
			if(map == null)
			{
				map = new HashMap<>();
				__httpMap__.put(url, map);
			}
			map.put(watcher.getId(), watcher);
		}
		return watcher;
	}

	public boolean dispatch(String url, int id, Object v)
	{
		if(__httpMap__.size() > 0)
		{
			HttpResponseWatcher watcher = null;
			synchronized(__httpMap__)
			{
				Map<Integer, HttpResponseWatcher> map = __httpMap__.get(url);
				if(map != null)
				{
					watcher = map.remove(id);
				}
				if(watcher != null)
				{
					watcher.setResponse(v);
					return true;
				}
				Logger.debug(QuestionDispatcher.class, "Unknown Url {} response: {}", url, v);
			}
		}
		return false;
	}

	public boolean dispatch(long pid, Object v)
	{
		if(v instanceof Rpc)
		{
			Rpc rpc = (Rpc)v;
			if(!rpc.isRequest())
			{
				if(__askMap__.size() > 0)
				{
					int xid = rpc.getId();
					AskResponseWatcher watcher = null;
					synchronized(__askMap__)
					{
						Map<Integer, AskResponseWatcher> map = __askMap__.get(pid);
						if(map != null)
						{
							watcher = map.remove(xid);
						}
					}
					if(watcher != null)
					{
						watcher.answer(rpc);
						return true;
					}
				}
				Logger.debug(QuestionDispatcher.class, "Unknown Rpc: {}", rpc);
				return true;
			}
		}
		return false;
	}

	public void update()
	{
		synchronized(__askMap__)
		{
			Iterator<Map.Entry<Long, Map<Integer, AskResponseWatcher>>> it1 = __askMap__.entrySet().iterator();
			while(it1.hasNext())
			{
				Map<Integer, AskResponseWatcher> map = it1.next().getValue();
				Iterator<Map.Entry<Integer, AskResponseWatcher>> it2 = map.entrySet().iterator();
				while(it2.hasNext())
				{
					AskResponseWatcher watcher = it2.next().getValue();
					if(watcher.isTimeout())
					{
						synchronized(list)
						{
							list.add(watcher);
						}
						it2.remove();
					}
				}
				if(map.size() == 0)
				{
					it1.remove();
				}
			}
		}
		synchronized(__httpMap__)
		{
			Iterator<Map.Entry<String, Map<Integer, HttpResponseWatcher>>> it1 = __httpMap__.entrySet().iterator();
			while(it1.hasNext())
			{
				Map<Integer, HttpResponseWatcher> map = it1.next().getValue();
				Iterator<Map.Entry<Integer, HttpResponseWatcher>> it2 = map.entrySet().iterator();
				while(it2.hasNext())
				{
					HttpResponseWatcher watcher = it2.next().getValue();
					if(watcher.isTimeout())
					{
						synchronized(list)
						{
							list.add(watcher);
						}
						it2.remove();
					}
				}
				if(map.size() == 0)
				{
					it1.remove();
				}
			}
		}
		synchronized(list)
		{
			for(ResponseWatcher watcher : list)
			{
				watcher.timeout();
			}
			list.clear();
		}
	}

	public void clear(long pid)
	{
		synchronized(__askMap__)
		{
			Map<Integer, AskResponseWatcher> map = __askMap__.remove(pid);
			if(map != null)
			{
				for(AskResponseWatcher watcher : map.values())
				{
					watcher.timeout();
				}
				map.clear();
			}
		}
	}

	public void clear()
	{
		__askMap__.clear();
	}
}
