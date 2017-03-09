package com.game.matcher;

import com.game.room.Room;
import com.x.network.io.Tickable;
import com.x.network.io.Ticker;
import com.x.network.service.ServiceSystem;
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
public abstract class Matcher implements Tickable, Ticker
{
	//protected Room roomType
	// id
	protected Map<String, User> players = new HashMap<>();
	// queued player
	//protected Map<String, User> queuedPlayers = new HashMap<>();
	protected List<String> queuedPlayers = new LinkedList<>();
	protected Map<String, Room> playerInRooms = new HashMap<>();
	// idle room
	protected List<Room> recyclableRooms = new LinkedList<>();
	protected Map<Long, Room> rooms = new HashMap<>();
	// emergent room
	protected List<Room> emergentRooms = new LinkedList<>();
	// playing room
	protected Map<Long, Room> fullRooms = new HashMap<>();
	// previous room
	protected Room previousRoom = null;

	public void add(User player)
	{
		//this.queuedPlayers.put(player.getKey(), player);
		this.queuedPlayers.add(player.getKey());
		this.players.put(player.getKey(), player);
	}

	public void remove(User player)
	{
		//if(this.queuedPlayers.containsKey(player.getKey()))
		if(this.queuedPlayers.contains(player.getKey()))
			this.queuedPlayers.remove(player.getKey());
		else
			leave(player);
	}

	protected void match()
	{
		// 匹配房间
		this.previousRoom = this.previousRoom == null ? matchRoom() : this.previousRoom;
		if(this.previousRoom == null)
		{
			// error
		}

		while(!this.queuedPlayers.isEmpty())
		{
			// 玩家操作
			String key = this.queuedPlayers.get(0);

			if(key != null && this.players.containsKey(key))
			{
				User p = this.players.get(key);
				// 终止匹配
				if(p != null && onMatch(p, this.previousRoom))
				{
					this.queuedPlayers.remove(0);
				}
				else
				{
					// 当前人未被匹配到,进入下一轮
					break;
				}
			}
			else
			{
				// mechanism of protection
				this.queuedPlayers.remove(0);
			}
		}

		if(this.previousRoom.canPlay() && !this.previousRoom.isTick())
		{
			this.previousRoom.start();
		}

		if(this.previousRoom.isFull())
		{
			//this.emergentRooms.remove(this.previousRoom);
			this.fullRooms.put(this.previousRoom.getUuid(), this.previousRoom);
			this.previousRoom = null;
		}
	}

	protected Room matchRoom()
	{
		Room room = null;
		if(this.emergentRooms.size() <= 0)
		{
			if(!this.recyclableRooms.isEmpty())
			{
				room = this.recyclableRooms.get(0);
				this.recyclableRooms.remove(0);
			}
			if(room == null)
			{
				room = allocate();
				this.rooms.put(room.getUuid(), room);
				//this.emergentRooms.add(room);
			}

			//this.emergentRooms.add(room);
			// ??? addTickable(room);
		}
		else
		{
			room = this.emergentRooms.get(0);
			this.emergentRooms.remove(0);
		}

		return room;
	}

	/*protected void match(User player)
	{
		Room room = null;
		if(this.emergentRooms.size() <= 0)
		{
			if(!this.recyclableRooms.isEmpty())
			{
				room = this.recyclableRooms.get(0);
				this.recyclableRooms.remove(0);
			}
			if(room == null)
			{
				room = allocate();
				this.rooms.put(room.getUuid(), room);
				//this.emergentRooms.add(room);
			}

			//this.emergentRooms.add(room);
			addTickable(room);
		}
		else
		{
			room = this.emergentRooms.get(0);
			this.emergentRooms.remove(0);
		}

		//onMatch(player, room);

		//if(room == null)
		//	throw new Exception();
		enter(player, room);

	}*/

	/**
	 *
	 * @param player
	 * @param room
	 * @return false:停止匹配 true:继续匹配下个人
	 */
	protected boolean onMatch(User player, Room room)
	{
		if(room.isFull())
			return false;

		return enter(player, room);
	}

	protected boolean enter(User player, Room room)
	{
		if(room.enter(player))
		{
			this.playerInRooms.put(player.getKey(), room);
			//onEnter(player); // 感觉这个多余了???

			/*if(room.isFull())
			{
				this.fullRooms.put(room.getUuid(), room);
			}
			else
			{
				this.emergentRooms.add(room);
			}*/

			return true;
		}

		return false;
	}

	protected void leave(User player)
	{
		//onLeave(player);
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
					room.stop();
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
	public void addTickable(Tickable t)
	{
		ServiceSystem.getInstance().addTickable(t);
	}

	@Override
	public Tickable removeTickable(Tickable t)
	{
		return ServiceSystem.getInstance().removeTickable(t);
	}

	@Override
	public long elapse()
	{
		return 1000L;
	}

	@Override
	public void tick()
	{
		match();
		/*if(!this.queuedPlayers.isEmpty())
		{
			String key = this.queuedPlayers.get(0);
			this.queuedPlayers.remove(0);
			if(key != null && this.players.containsKey(key))
				match(this.players.get(key));
		}*/
	}

	public abstract Room allocate();

	public abstract void onEnter(User player);
	public abstract void onLeave(User player);
}
