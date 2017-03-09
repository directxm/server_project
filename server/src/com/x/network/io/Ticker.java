package com.x.network.io;

/**
 * Created by fatum on 2017/3/9.
 */
public interface Ticker
{
	void addTickable(Tickable t);
	Tickable removeTickable(Tickable t);
}
