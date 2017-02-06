package com.x.network.service;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 10/9/15 5:56 PM
 */
public interface ServiceIoConfig
{
	int getReadBufferSize();

	int getWriteBufferSize();

	int getIdleTime();
}
