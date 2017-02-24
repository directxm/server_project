package com.game.matcher;


import com.game.Room;
import com.x.network.ServiceThreadPool;
import com.x.network.io.Tickable;
import com.x.network.service.ServiceSystem;
import com.x.util.SystemUtils;
import com.x.wechat.data.User;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by fatum on 2017/2/18.
 */
public class TestMatcher extends Matcher
{


	@Override
	public void onEnter(User player)
	{
		//this.players.put(player.getKey(), player);
		//this.queuedPlayers.put(player.getKey(), player);
	}

	@Override
	public void onLeave(User player)
	{
		/*if(this.playerInRooms.containsKey(player.getKey()))
		{
			Room room = this.playerInRooms.get(player.getKey());
			if(room != null)
			{
				room.onLeave(player);
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
		}*/
	}

	@Override
	public long elapse()
	{
		return 1000L;
	}

	@Override
	public void tick()
	{
		int hello = 1;
	}

	@Override
	public Room allocate()
	{
		return null;
	}

	@Override
	public void onMatch(User player, Room room)
	{

	}

	public static void addTickable(Tickable t)
	{

	}

	public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
	{
		Runnable runnable = () -> executeRunnable(command);
		//return scheduledExecutorService.scheduleWithFixedDelay(() -> executeRunnable(command), initialDelay, delay, unit);
		return null;
	}

	public static void executeRunnable(final Runnable runnable)
	{
		//executorService.execute(runnable);
	}

	public static void main(String[] args)
	{
		try
		{
			ServiceThreadPool serviceThreadPool = new ServiceThreadPool("test matcher", 1);
			ServiceSystem test = ServiceSystem.createInstance("test matcher", SystemUtils.getPid(), serviceThreadPool);
			TestMatcher matcher = new TestMatcher();
			ServiceSystem.getInstance().addTickable(matcher);

			test.start();

			for(int i = 0; i < 10; ++i)
			{

			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(4);
		}

	}
}
