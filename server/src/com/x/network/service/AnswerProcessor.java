package com.x.network.service;

import com.x.io.CodeException;
import com.x.network.standalone.ServicePath;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 7/7/15 8:35 PM
 */
public interface AnswerProcessor<P, V>
{
	void answer(ServiceWrapper innerService, ServicePath innerReceiver, P sentMessage, V receivedMessage) throws CodeException;

	void timeout(ServiceWrapper innerService, ServicePath innerReceiver, P sentMessage) throws CodeException;
}
