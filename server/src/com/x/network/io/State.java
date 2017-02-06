package com.x.network.io;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanfengbing
 * @since 13-7-2 AM10:47
 */
public class State
{
	private static final ConcurrentHashMap<String, State> __STATES__ = new ConcurrentHashMap<String, State>();

	public static State getState(String name)
	{
		return __STATES__.get(name.toUpperCase());
	}

	public static State putState(State state)
	{
		return __STATES__.put(state.name.toUpperCase(), state);
	}

	protected final String name;
	protected final int idleTime;
	protected final boolean allowAnyMessage;
	protected final HashSet<Object> messageKeys = new HashSet<>();

	public State(String name, int timeout, boolean allowAnyMessage)
	{
		this.name = name;
		this.idleTime = timeout;
		this.allowAnyMessage = allowAnyMessage;
	}

	public State addMessageKey(Object key)
	{
		messageKeys.add(key);
		return this;
	}

	public boolean hasMessageKey(Object key)
	{
		return allowAnyMessage || messageKeys.contains(key);
	}

	@Override
	public String toString()
	{
		return "State{" +
				"name='" + name + '\'' +
				", idleTime=" + idleTime / 1000 +
				", allowAnyMessage=" + allowAnyMessage +
				", messageKeys=" + messageKeys +
				'}';
	}
}
