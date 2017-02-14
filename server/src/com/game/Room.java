package com.game;

import com.x.network.io.Tickable;

/**
 * Created by fatum on 2017/2/14.
 */
public abstract class Room implements Tickable
{
    @Override
    public long elapse()
    {
        return 0;
    }

    @Override
    public void tick()
    {

    }
}
