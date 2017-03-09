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

        public State(T content, float total)
        {
            if (content == null)
            {
                Logger.error("State with content == null");
            }
            this.content = content;
            this.timer = new Counter(total);
        }

        public final void resetTimer()
        {
            this.timer.reset();
        }

        public final void redefineTimer(float total)
        {
            this.timer.redefine(total);
        }

        public final void neverTimeOut()
        {
            this.timer.infinity();
        }

        public final boolean isNeverTimeOut()
        {
            return this.timer.isInfinite();
        }

        public final void timeOut()
        {
            this.timer.exceed();
        }

        public final boolean isTimeOut()
        {
            return this.timer.isExceed();
        }

        public final void reset()
        {
            System.out.println(this.getClass().toString() + "::" + getMethodName() + " timer =" + this.timer);

            onReset();
            resetTimer();
        }

        public final State input(Event e)
        {
            System.out.println(this.getClass().toString() + "::" + getMethodName() + " timer =" + this.timer);

            return doEvent(e);
        }

        public final State tick(float delta)
        {
            System.out.println(this.getClass().toString() + "::" + getMethodName() + " timer =" + this.timer);

            this.timer.increase(delta);
            return doTick(delta);
        }

        public final void enter(Event e, State lastState)
        {
            System.out.println(this.getClass().toString() + "::" + getMethodName() + " timer =" + this.timer);

            reset();
            onEnter(e, lastState);
        }

        public final void exit(Event e, State nextState)
        {
            System.out.println(this.getClass().toString() + "::" + getMethodName() + " timer =" + this.timer);

            onExit(e, nextState);
        }

        public final void handle(Object object)
        {
            System.out.println(this.getClass().toString() + "::" + getMethodName() + " timer =" + this.timer);

            onHandle(object);
        }

        protected void onHandle(Object object) {}
        protected void onReset() {}
        protected void onEnter(Event e, State lastState) {}
        protected void onExit(Event e, State nextState) {}
        protected State doEvent(Event e) { return this; }
        protected State doTick(float deltaTime) { return this; }

        // µ˜ ‘”√
        private String getMethodName()
        {
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            StackTraceElement e = stacktrace[2];
            String methodName = e.getMethodName();
            return methodName;
        }
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

    public boolean tick(float delta)
    {
        if (this.currentState != null)
        {
            State caller = this.currentState;
            State nextState = caller.tick(delta);
            if (caller != this.currentState)
            {
                return false;
            }
            if (nextState != this.currentState)
            {
                TickEvent e = new TickEvent(delta);
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
