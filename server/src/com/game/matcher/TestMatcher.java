package com.game.matcher;


import com.game.room.*;
import com.x.network.ServiceThreadPool;
import com.x.network.service.ServiceSystem;
import com.x.util.Identity;
import com.x.util.SystemUtils;
import com.x.wechat.data.User;

import java.util.ArrayList;
import java.util.List;

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
	public Room allocate()
	{
		return new PokerBullRoom(Identity.id(), this, null);
	}

	/*public static void addTickable(Tickable t)
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
	}*/

	public static void main(String[] args)
	{
		try
		{
			System.out.println("测试匹配房间");

			// 小心这个陷阱
			Identity.setLocalId(100);

			ServiceThreadPool serviceThreadPool = new ServiceThreadPool("test matcher", 1);
			ServiceSystem test = ServiceSystem.createInstance("test matcher", SystemUtils.getPid(), serviceThreadPool);
			TestMatcher matcher = new TestMatcher();
			ServiceSystem.getInstance().addTickable(matcher);

			test.start();

			// 6名玩家
			List<User> users = new ArrayList<>();
			for(int i = 0; i < 10000; ++i)
			{
				users.add(User.test("test" + i));
			}

			boolean in = true;

			while(true)
			{

				if(in)
				{
					// 人全部进人
					for(User user : users)
					{
						matcher.add(user);
					}

					in = false;
				}

				Thread.sleep(900000L);

				if(!in)
				{
					for(User user : users)
					{
						matcher.remove(user);
						matcher.add(user);
					}

					in = true;
				}
			}

			// 一定时间离开一人，直到全部离开
			/*for(int i = 0; i < 8; ++i)
			{
				int idx = Random.randomInt(100) % users.size();
				User user = users.get(idx);
				users.remove(idx);

				Thread.sleep(8000L);
			}*/

			//Thread.sleep(Long.MAX_VALUE);

			//Thread.sleep(10000L);

			//System.out.println("MAIN OVER");

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(4);
		}

	}
}
