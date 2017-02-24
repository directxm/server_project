package com.game;

import com.x.network.io.Tickable;
import com.x.wechat.data.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fatum on 2017/2/14.
 */
public abstract class Room implements Tickable
{
	protected long uuid;
	protected Map<String, User> players = new HashMap<>();
	protected Map<String, User> watingPlayers = new HashMap<>();

	public long getUuid()
	{
		return uuid;
	}

	public boolean isFull()
	{
		return false;
	}

	public boolean isEmpty()
	{
		return false;
	}

	public boolean enter(User player)
	{
		return true;
	}

	public void leave(User player)
	{

	}

	protected boolean addPlayer(User player)
	{
		if(this.players.containsKey(player.getOpenId()))
			return false;

		this.players.put(player.getOpenId(), player);
		return true;
	}

	protected void removePlayer(User player)
	{
		this.players.remove(player.getOpenId());
	}

	// 房间可容纳人数
	public abstract int getMaxNumber();

	public abstract void clear();
	public abstract void onEnter(User player);
	public abstract void onLeave(User player);
}
