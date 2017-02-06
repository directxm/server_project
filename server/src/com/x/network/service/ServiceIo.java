package com.x.network.service;

import com.x.network.standalone.ServiceProvider;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 10/10/15 5:39 PM
 */
public interface ServiceIo
{
	void start() throws Exception;

	void stop();

	void addServiceProvider(ServiceProvider serviceProvider) throws Exception;

	void removeServiceProvider(ServiceProvider serviceProvider);
}
