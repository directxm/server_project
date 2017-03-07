package com.x.lang;

/**
 * Created by fatum on 2017/3/6.
 */
public class Counter
{
	protected float total;
	protected float current;

    public Counter(float total)
    {
        redefine(total);
    }

    public void reset()
    {
        this.current = 0.0f;
    }

    public void redefine(float total)
    {
        if (total < 0.0f)
        {
            this.total = Float.POSITIVE_INFINITY;
        }
        else
        {
            this.total = total;
        }
        reset();
    }

	public void delay(float extra)
	{
		if(extra > 0)
		{
			this.total += extra;
		}
	}

    public void zero()
    {
        this.total = 0.0f;
        reset();
    }

    public void infinity()
    {
        this.total = Float.POSITIVE_INFINITY;
        reset();
    }

    public void exceed()
    {
        this.current = total;
    }

    public boolean isZero()
    {
        return this.total == 0.0f;
    }

    public boolean isInfinite()
    {
        return Float.isInfinite(this.total);
    }

    public boolean isFinite()
    {
        return Float.isFinite(this.total);
    }

    public boolean isPositiveFinite()
    {
        return 0.0f < this.total && this.total < Float.POSITIVE_INFINITY;
    }

    public boolean isExceed()
    {
        return this.current >= this.total;
    }

    public boolean isNotExceed()
    {
        return this.current < this.total;
    }

    public boolean increase(float delta)
    {
        if (this.current + delta < this.total)
        {
            this.current += delta;
        }
        else
        {
            this.current = this.total;
        }
        return isExceed();
    }

    public float getCurrentNormalized()
    {
        if (isPositiveFinite())
        {
            return Math.min(this.total, this.current) / this.total;
        }
        return 1.0f;
    }

    public float getRemain()
    {
        if (isPositiveFinite())
        {
            return Math.max(0.0f, this.total - this.current);
        }
        return 0.0f;
    }

    public float getRemainNormalized()
    {
        if (isPositiveFinite())
        {
            return getRemain() / this.total;
        }
        return 0.0f;
    }

    @Override
    public String toString()
    {
        return "Counter{" +
                "total=" + total +
                ", current=" + current +
                '}';
    }
}
