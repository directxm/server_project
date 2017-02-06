package com.x.database.cache;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 4/1/16 4:56 PM
 */
public abstract class VersionSaveable implements Saveable
{
	private int version = 0;

	public synchronized final int getVersion()
	{
		return version;
	}

	public synchronized final void setVersion(int version)
	{
		this.version = version;
	}
}
