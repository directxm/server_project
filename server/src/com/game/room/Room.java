package com.game.room;

import com.game.regulation.Regulation;
import com.x.fsm.FiniteStateMachine;
import com.x.network.io.Tickable;
import com.x.network.io.Ticker;
import com.x.wechat.data.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fatum on 2017/2/14.
 */
public abstract class Room implements Tickable
{

	/*public static class RoomFiniteStateMachine<T extends Room> extends FiniteStateMachine<T>
	{
		public RoomFiniteStateMachine(T content)
		{
			super(content);
		}

		@Override
		public State<T> createEnterState()
		{
			return new NullState(content);
		}
	}*/

	public static class State<T extends Room> extends FiniteStateMachine.State<T>
	{
		public State(T content)
		{
			super(content);
		}

		public State(T content, float total)
		{
			super(content, total);
		}

		// 在这种状态下的处理

		//int getKey();

		// 是否可进入
		// 进入时触发
		// 结束时触发
	}

	/*public static class NullState extends State
	{
		public NullState(Room content)
		{
			super(content);
		}
	}*/

	protected final long uuid;
	protected final Ticker ticker;
	protected final Regulation regulation;
	//protected final FiniteStateMachine fsm;// = new RoomFiniteStateMachine(this);

	protected Map<String, User> playingPlayers = new HashMap<>();
	protected Map<String, User> waitingPlayers = new HashMap<>();

	protected boolean tick = false;
	protected boolean play = false;
	protected long countingDelta = 0L;
	protected State state;

	public Room(long uuid, Ticker ticker, Regulation regulation/*, FiniteStateMachine fsm*/)
	{
		this.uuid = uuid;
		this.ticker = ticker;
		this.regulation = regulation;
		//this.fsm = fsm;
	}

	public final long getUuid()
	{
		return uuid;
	}

	public final boolean isFull()
	{
		return getParticipatingNumber() >= getMaxNumber();
	}

	public final boolean isEmpty()
	{
		return (getParticipatingNumber()) <= 0;
	}

	public final int getPlayingNumber()
	{
		return playingPlayers.size();
	}

	public final int getWaitingNumber()
	{
		return waitingPlayers.size();
	}

	public final int getParticipatingNumber()
	{
		return getPlayingNumber() + getWaitingNumber();
	}

	public final boolean isPlay()
	{
		return play;
	}

	public final void play(boolean play)
	{
		this.play = play;
	}

	public final boolean isTick()
	{
		return this.tick;
	}

	public final boolean enter(User player)
	{
		if(isFull())
			return false;

		this.waitingPlayers.put(player.getKey(), player);

		return true;
	}

	public final void leave(User player)
	{
		if(this.waitingPlayers.containsKey(player.getKey()))
			this.waitingPlayers.remove(player.getKey());

		if(this.playingPlayers.containsKey(player.getKey()))
		{
			if(isPlay())
			{
				over(player);
					//regulation.leave(player, this);
			}
			this.playingPlayers.remove(player.getKey());
		}
	}

	// removeTickable 之后执行
	public void stop()
	{
		this.playingPlayers.clear();
		this.waitingPlayers.clear();

		this.play = false;
		this.countingDelta = 0L;

		this.tick = false;
		ticker.removeTickable(this);

		System.out.println("room id =" + uuid + " stop!");
	}

	// addTickable 之后执行
	public void start()
	{
		//this.play = true;
		this.tick = true;
		ticker.addTickable(this);

		System.out.println("room id =" + uuid + " start!");
	}

	public void start(User player)
	{

	}

	public void over(User player)
	{

	}

	public void boardcast(Object object)
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

	public abstract void onEnter(User player);
	public abstract void onLeave(User player);
	public abstract void onTick(float delta);

	@Override
	public long elapse()
	{
		// 一秒一回
		return 1000L;
	}

	@Override
	public void tick()
	{
		float delta = 1000L;
		//fsm.tick(delta);
		onTick(delta);
	}

	// 调试用
    protected String getMethodName()
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();
        return methodName;
    }
}
