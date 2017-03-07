package com.game.room;

import com.game.regulation.Regulation;
import com.x.network.io.Tickable;
import com.x.wechat.data.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fatum on 2017/2/14.
 */
public abstract class Room implements Tickable
{
	public interface State
	{
		int getKey();

		// 是否可进入
		// 进入时触发
		// 结束时触发
	}

	protected final long uuid;
	protected final Regulation regulation;

	protected Map<String, User> players = new HashMap<>();
	protected Map<String, User> waitingPlayers = new HashMap<>();

	protected boolean play = false;
	protected long countingDelta = 0L;
	protected State state;

	public Room(long uuid, Regulation regulation)
	{
		this.uuid = uuid;
		this.regulation = regulation;
	}

	public long getUuid()
	{
		return uuid;
	}

	public boolean isFull()
	{
		return getParticipatingNumber() >= getMaxNumber();
	}

	public boolean isEmpty()
	{
		return (getParticipatingNumber()) <= 0;
	}

	public int getPlayingNumber()
	{
		return players.size();
	}

	public int getWaitingNumber()
	{
		return waitingPlayers.size();
	}

	public int getParticipatingNumber()
	{
		return getPlayingNumber() + getWaitingNumber();
	}

	public boolean enter(User player)
	{
		if(isFull())
			return false;

		this.waitingPlayers.put(player.getKey(), player);

		return true;
	}

	public void leave(User player)
	{
		if(this.waitingPlayers.containsKey(player.getKey()))
			this.waitingPlayers.remove(player.getKey());

		if(this.players.containsKey(player.getKey()))
		{
			if(isPlay())
			{
				over(player);
					//regulation.leave(player, this);
			}
			this.players.remove(player.getKey());
		}
	}

	public void start(User player)
	{

	}

	public void over(User player)
	{

	}

	/*protected boolean addPlayer(User player)
	{
		if(this.players.containsKey(player.getOpenId()))
			return false;

		this.players.put(player.getOpenId(), player);
		return true;
	}

	protected void removePlayer(User player)
	{
		this.players.remove(player.getOpenId());
	}*/

	// 房间可容纳人数
	public abstract int getMaxNumber();

	public abstract boolean canPlay();

	public boolean isPlay()
	{
		return play;
	}

	public void clear()
	{
		this.players.clear();
		this.waitingPlayers.clear();

		this.play = false;
		this.countingDelta = 0L;

	}
	public abstract void onEnter(User player);
	public abstract void onLeave(User player);
}
