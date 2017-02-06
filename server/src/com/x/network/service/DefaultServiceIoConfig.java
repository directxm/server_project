package com.x.network.service;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 10/10/15 3:29 PM
 */
public class DefaultServiceIoConfig implements ServiceIoConfig
{
	@Override
	public int getReadBufferSize()
	{
		return 65536;
	}

	@Override
	public int getWriteBufferSize()
	{
		return 65536;
	}

	@Override
	public int getIdleTime()
	{
		return 86400;
	}
}
