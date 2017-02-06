package com.x.network.io;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 1/6/15 4:28 PM
 */
public interface Tickable
{
	long elapse();

	void tick();
}
