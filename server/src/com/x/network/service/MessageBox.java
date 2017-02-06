package com.x.network.service;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 6/30/15 5:02 PM
 */
public class MessageBox
{
	private PriorityBlockingQueue<Message> messages = new PriorityBlockingQueue<Message>();

	public Message poll()
	{
		return messages.poll();
	}

	public boolean offer(Message message)
	{
		return messages.offer(message);
	}

	public int size()
	{
		return messages.size();
	}
}
