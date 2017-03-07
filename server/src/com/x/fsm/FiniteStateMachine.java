package com.x.fsm;

import com.x.lang.Counter;
import com.x.logging.Logger;

/**
 * Created by fatum on 2017/3/6.
 */
public abstract class FiniteStateMachine<T>
{
	protected T content;
	protected State<? extends T> currentState;

	public static class State<T>
    {
        protected final T content;
        protected Counter timer;

        public State(T content)
        {
            if (content == null)
            {
                Logger.error("State with content == null");
            }
            this.content = content;
            this.timer = new Counter(Float.POSITIVE_INFINITY);
        }

        public void resetTimer()
        {
            this.timer.reset();
        }

        public void redefineTimer(float total)
        {
            this.timer.redefine(total);
        }

        public void neverTimeOut()
        {
            this.timer.infinity();
        }

        public boolean isNeverTimeOut()
        {
            return this.timer.isInfinite();
        }

        public void timeOut()
        {
            this.timer.exceed();
        }

        public boolean isTimeOut()
        {
            return this.timer.isExceed();
        }

        public void reset()
        {
            onReset();
            resetTimer();
        }

        public State input(Event e)
        {
            return doEvent(e);
        }

        public State tick(float deltaTime)
        {
            this.timer.increase(deltaTime);
            return doTick(deltaTime);
        }

        public void enter(Event e, State lastState)
        {
            reset();
            onEnter(e, lastState);
        }

        public void exit(Event e, State nextState)
        {
            onExit(e, nextState);
        }

        protected void onReset() {}
        protected void onEnter(Event e, State lastState) {}
        protected void onExit(Event e, State nextState) {}
        protected State doEvent(Event e) { return this; }
        protected State doTick(float deltaTime) { return this; }
    }

    public static class Event
    {
        protected final int id;

        public Event(int id)
        {
            this.id = id;
        }
    }

    public static class TickEvent extends Event
    {
        protected float deltaTime;
        public TickEvent(float deltaTime)
        {
            super(-1);
            this.deltaTime = deltaTime;
        }
    }

	public static class ResetEvent extends Event
	{
		public ResetEvent()
        {
            super(-1);
        }
	}

	public static class FiniteEvent extends Event
	{
		public FiniteEvent()
		{
            super(-1);
		}

		private static FiniteEvent instance = new FiniteEvent();
		public static FiniteEvent getInstance() { return instance; }
	}

	public FiniteStateMachine(T content)
    {
        if (content == null)
        {
            Logger.error(this.getClass(), "FiniteStateMachine with content == null");
        }
        this.content = content;
    }

    public boolean input(Event e)
    {
        if (this.currentState == null)
        {
            return false;
        }

        if (this.currentState != null)
        {
            State caller = this.currentState;
            State nextState = this.currentState.input(e);
            if (caller != this.currentState)
            {
                return false;
            }
            if (nextState != this.currentState)
            {
                return changeCurrentState(nextState, e);
            }
        }
        else
        {
            Logger.error("FiniteStateMachine current state == null");
            reset();
        }
        return false;
    }

    public boolean tick(float deltaTime)
    {
        if (this.currentState != null)
        {
            State caller = this.currentState;
            State nextState = caller.tick(deltaTime);
            if (caller != this.currentState)
            {
                return false;
            }
            if (nextState != this.currentState)
            {
                TickEvent e = new TickEvent(deltaTime);
                return changeCurrentState(nextState, e);
            }
        }
        else
        {
            Logger.error("FiniteStateMachine current state == null");
            reset();
        }
        return false;
    }

    public void reset()
    {
        State startState = createEnterState();
        ResetEvent e = new ResetEvent();
        changeCurrentState(startState, e);
    }

    public void stop()
    {
        changeCurrentState(null, FiniteEvent.getInstance());
    }

    protected boolean changeCurrentState(State<? extends T> nextState, Event e)
    {
        if (this.currentState != null)
        {
            onPreStateChange(this.currentState, nextState);
            this.currentState.exit(e, nextState);
        }

        State lastState = this.currentState;
        this.currentState = nextState;

        if (nextState != null)
        {
            this.currentState.enter(e, lastState);
            onPostStateChange(this.currentState, lastState);
        }
        return true;
    }

    protected void onPreStateChange(State<? extends T> currentState, State nextState)
    {

    }
    protected void onPostStateChange(State<? extends T> currentState, State lastState)
    {

    }

    public abstract State<? extends T> createEnterState();
}
