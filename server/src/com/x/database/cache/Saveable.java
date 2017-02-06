package com.x.database.cache;

import java.io.Serializable;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/17/16 2:17 PM
 */
public interface Saveable extends Serializable
{
	Object key();
}
