package com.game.matcher;

import com.game.Room;
import com.x.network.io.Tickable;
import com.x.wechat.data.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by fatum on 2017/2/16
 * changing room is executed immediately by protocol.
 * matching room is executed on time.
 */
public abstract class Matcher implements Tickable
{
	//protected Room roomType
	// id
	protected Map<String, User> players = new HashMap<>();
	// queued player
	protected Map<String, User> queuedPlayers = new HashMap<>();
	protected Map<String, Room> playerInRooms = new HashMap<>();
	// idle room
	protected List<Room> recyclableRooms = new LinkedList<>();
	protected Map<Long, Room> rooms = new HashMap<>();
	// emergent room
	protected List<Room> emergentRooms = new LinkedList<>();
	// playing room
	protected Map<Long, Room> fullRooms = new HashMap<>();

	public void add(User player)
	{
		this.queuedPlayers.put(player.getKey(), player);
		this.players.put(player.getKey(), player);
	}

	public void remove(User player)
	{
		if(this.queuedPlayers.containsKey(player.getKey()))
			this.queuedPlayers.remove(player.getKey());
		else
			leave(player);
	}

	protected void match(User player)
	{
		Room room = null;
		if(this.emergentRooms.size() <= 0)
		{
			if(!this.recyclableRooms.isEmpty())
				room = this.recyclableRooms.get(0);
			if(room == null)
			{
				room = allocate();
				this.rooms.put(room.getUuid(), room);
				this.recyclableRooms.add(room);
			}
		}
		else
		{
			room = this.emergentRooms.get(0);
		}

		onMatch(player, room);

		//if(room == null)
		//	throw new Exception();
		enter(player, room);

	}

	protected boolean enter(User player, Room room)
	{
		if(room.enter(player))
		{
			this.playerInRooms.put(player.getKey(), room);
			onEnter(player);
			return true;
		}

		return false;
	}

	protected void leave(User player)
	{
		onLeave(player);
		if(this.playerInRooms.containsKey(player.getKey()))
		{
			Room room = this.playerInRooms.get(player.getKey());
			if(room != null)
			{
				room.leave(player);
				this.playerInRooms.remove(player.getKey());

				if(room.isEmpty())
				{
					this.recyclableRooms.add(room);
				}
				else
				{
					this.emergentRooms.add(room);
				}

				this.fullRooms.remove(room.getUuid());
			}
		}
	}

	@Override
	public void tick()
	{
		int hello = 1;
	}

	public abstract Room allocate();

	public abstract void onMatch(User player, Room room);
	public abstract void onEnter(User player);
	public abstract void onLeave(User player);
}
