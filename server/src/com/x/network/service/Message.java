package com.x.network.service;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 6/30/15 5:04 PM
 */
enum MessageType
{
	PIPE_CLOSED,
	PIPE_RESPONSE_CONTINUE,
	PIPE_OPENED,
	PIPE_CAUGHT_EXCEPTION,
	PIPE_RECEIVED_MESSAGE,
	PIPE_IDLED,
}

public class Message implements Serializable, Comparable<Message>
{
	private static final AtomicLong TIMESTAMP = new AtomicLong(0);

	private long timestamp = 0;
	private ServicePipe servicePipe;
	private MessageType messageType;
	private Object content;

	protected Message(ServicePipe servicePipe, MessageType messageType, Object content)
	{
		timestamp = TIMESTAMP.incrementAndGet();
		this.servicePipe = servicePipe;
		this.messageType = messageType;
		this.content = content;
	}

	public ServicePipe getServicePipe()
	{
		return servicePipe;
	}

	public MessageType getMessageType()
	{
		return messageType;
	}

	public Object getContent()
	{
		return content;
	}

	@Override
	public String toString()
	{
		return "Message{" +
				"servicePipe=" + servicePipe +
				", messageType=" + messageType +
				", content=" + content +
				'}';
	}

	@Override
	public int compareTo(Message o)
	{
		long ret = messageType.ordinal() - o.messageType.ordinal();
		if(ret == 0)
		{
			ret = timestamp - o.timestamp;
		}
		if(ret > 0)
		{
			return 1;
		}
		else if(ret < 0)
		{
			return -1;
		}
		return 0;
	}
}
