package com.test;

import com.x.fsm.FiniteStateMachine;
import com.x.network.ServiceThreadPool;
import com.x.network.service.ServiceSystem;
import com.x.util.SystemUtils;

/**
 * Created by fatum on 2017/3/7.
 */
public class FSMTest extends FiniteStateMachine<FSMTest.FSMContent>
{
	public static class FSMContent
	{
		public FSMContent()
		{
		}
	}

	public static class StateDefault extends FiniteStateMachine.State<FSMContent>
	{
		public StateDefault(FSMContent content)
		{
			super(content);
		}

		@Override
		protected void onEnter(FiniteStateMachine.Event e, State lastState)
		{
			System.out.println(this.getClass().toString() + "::onEnter timer =" + this.timer);
		}

		@Override
		protected void onExit(FiniteStateMachine.Event e, State nextState)
		{
			System.out.println(this.getClass().toString() + "::onExit timer =" + this.timer);
		}

		@Override
		protected State doEvent(FiniteStateMachine.Event e)
		{
			System.out.println(this.getClass().toString() + "::doEvent timer =" + this.timer);

			if (e instanceof StateOne.Event)
            {
                return new StateOne(content);
            }
			return this;
		}

		@Override
		protected State doTick(float deltaTime)
		{
			System.out.println(this.getClass().toString() + "::doTick timer =" + this.timer);
			return this;
		}
	}

	public static class StateOne extends FiniteStateMachine.State<FSMContent>
	{
		public static class Event extends FiniteStateMachine.Event
		{

			public Event(int id)
			{
				super(id);
			}
		}

		public StateOne(FSMContent content)
		{
			super(content);
		}

		@Override
		protected void onEnter(FiniteStateMachine.Event e, State lastState)
		{
			System.out.println(this.getClass().toString() + "::onEnter timer =" + this.timer);
		}

		@Override
		protected void onExit(FiniteStateMachine.Event e, State nextState)
		{
			System.out.println(this.getClass().toString() + "::onExit timer =" + this.timer);
		}

		@Override
		protected State doEvent(FiniteStateMachine.Event e)
		{
			System.out.println(this.getClass().toString() + "::doEvent timer =" + this.timer);
			return this;
		}

		@Override
		protected State doTick(float deltaTime)
		{
			System.out.println(this.getClass().toString() + "::doTick timer =" + this.timer);
			return this;
		}
	}


	public FSMTest(FSMContent content)
	{
		super(content);
	}

	/*public void tick()
	{
		tick(100L);
	}*/

	@Override
	public State<? extends FSMContent> createEnterState()
	{
		return new StateDefault(content);
	}

	public static void main(String[] args)
	{
		try
		{
			ServiceThreadPool serviceThreadPool = new ServiceThreadPool("test fsm", 1);
			ServiceSystem test = ServiceSystem.createInstance("test fsm", SystemUtils.getPid(), serviceThreadPool);

			test.start();

			FSMTest.FSMContent content = new FSMTest.FSMContent();
			FSMTest fsm = new FSMTest(content);
			fsm.reset();
			ServiceSystem.getInstance().executeRunnable(() -> fsm.tick(1000L), 1000L, 1000L);

			Thread.sleep(3000L);
			fsm.input(new StateOne.Event(1));
			Thread.sleep(10000L);

			System.out.println("MAIN OVER");

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(4);
		}

	}
}
