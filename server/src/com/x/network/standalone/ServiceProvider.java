package com.x.network.standalone;

import com.x.io.CodeException;
import com.x.logging.Logger;
import com.offbynull.coroutines.user.Continuation;
import com.x.network.io.Address;
import com.x.network.message.ProcessorFactory;
import com.x.network.service.*;
import com.x.network.io.Protocol;

import java.io.Serializable;
import java.lang.Object;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 10/9/15 4:20 PM
 */
public class ServiceProvider implements Serializable, Comparable<ServiceProvider>
{
	//private final ServiceSystem system;
	private final String name;
	private final Address address;
	private final ServiceIo serviceIo;

	private boolean dumpMessage = false;
	private boolean started = false;

	protected com.x.network.message.ProcessorFactory<Processor> processorFactory;
	protected final ConcurrentHashMap<String, Processor> processors = new ConcurrentHashMap<String, Processor>();

	public ServiceProvider(String name, Address address, ServiceIo serviceIo)
	{
		//this.system = system;
		this.name = name;
		this.address = address;
		this.serviceIo = serviceIo;
		this.dumpMessage = System.getProperty("DUMP_MESSAGE") != null;
	}

	public void start() throws Exception
	{
		synchronized(this)
		{
			if(!started)
			{
				if(processorFactory == null)
				{
					processorFactory = ProcessorFactory.defaultInstance();
				}
				if(serviceIo != null)
				{
					serviceIo.start();
					serviceIo.addServiceProvider(this);
				}

				started = true;
				Logger.debug(this.getClass(), "" + toString() + " started!");
			}
		}
	}

	public void stop()
	{
		synchronized(this)
		{
			if(started)
			{
				if(serviceIo != null)
					serviceIo.removeServiceProvider(this);
				started = false;
				Logger.debug(this.getClass(), "" + toString() + " stopped!");
			}
		}
	}

	public final String getName()
	{
		return name;
	}

	public final Address getAddress()
	{
		return address;
	}

	/*public final ServiceSystem getSystem()
	{
		return system;
	}*/

	public final ServiceIo getServiceIo()
	{
		return serviceIo;
	}

	public void setProcessorFactory(ProcessorFactory processorFactory)
	{
		this.processorFactory = processorFactory;
	}

	protected final Processor getProcessor(Protocol protocol)
	{
		Processor processor = processors.get(protocol.getClass().getSimpleName());
		if(processor == null)
		{
			if(processorFactory != null)
			{
				//noinspection SynchronizeOnNonFinalField
				synchronized(processorFactory)
				{
					processor = processorFactory.create(protocol.getClass().getSimpleName());
					if(processor != null)
					{
						processors.put(protocol.getClass().getSimpleName(), processor);
					}
				}
			}
		}
		return processor;
	}

	public final boolean isDumpMessage()
	{
		return dumpMessage;
	}

	public boolean isDumpReceivedMessage()
	{
		return false;
	}

	public boolean isDumpSentMessage()
	{
		return false;
	}

	public void pipeOpened(ServicePipe pipe)
	{

	}

	public void pipeClosed(ServicePipe pipe)
	{

	}

	public void pipeCaughtException(ServicePipe pipe, Throwable cause)
	{

	}

	public void pipeIdled(ServicePipe pipe, Object status)
	{

	}

	public void pipeSentMessage(ServicePipe pipe, Object message)
	{

	}

	public long calculateServiceCoroutineId(ServicePipe pipe, Object message)
	{
		return pipe.getPid();
	}

	@SuppressWarnings("unchecked")
	public final void process(Continuation c, ServiceWrapper service, ServicePath sender, Protocol protocol) throws CodeException
	{
		Processor processor = getProcessor(protocol);
		if(processor == null)
		{
			processUnknownProtocol(c, service, sender, protocol);
		}
		else
		{
			processor.process(c, service, sender, protocol);
		}
	}

	public void processUnknownProtocol(Continuation c, ServiceWrapper service, ServicePath sender, Protocol protocol) throws CodeException
	{
		Logger.warn(this.getClass(), "没有找到此协议的处理器，所以仅仅打印出来：{}, {}", protocol.toString(), toString());
	}

	public void processUnknownObject(Continuation c, ServiceWrapper service, ServicePath sender, Object object) throws CodeException
	{

	}

	public void processExceptionCaught(Continuation c, ServiceWrapper service, ServicePath sender, Object message, Throwable throwable)
	{

	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		ServiceProvider that = (ServiceProvider)o;

		//if(system != null ? !system.equals(that.system) : that.system != null) return false;
		if(name != null ? !name.equals(that.name) : that.name != null) return false;
		if(address != null ? !address.equals(that.address) : that.address != null) return false;
		return !(serviceIo != null ? !serviceIo.equals(that.serviceIo) : that.serviceIo != null);

	}

	@Override
	public int hashCode()
	{
		//int result = system != null ? system.hashCode() : 0;
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (serviceIo != null ? serviceIo.hashCode() : 0);
		return result;
	}

	@Override
	public String toString()
	{
		return "ServiceProvider{" +
				//"system=" + system.getName() +
				name != null ? ", name=" + name : "" +
				address != null ? ", address=" + address : "" +
				serviceIo != null ? ", serviceIo=" + serviceIo : "" +
				'}';
	}

	@Override
	public int compareTo(ServiceProvider o)
	{
		if(o == null) throw new NullPointerException("ServiceProvider compare to null!");
		if(this == o) return 0;
		if(this.equals(o)) return 0;
		int ret = name != null ? name.compareTo(o.name) : 0;
		if(ret == 0)
		{
			ret = address != null ? address.compareTo(o.address) : 0;
			if(ret == 0)
			{
				if(serviceIo == null || this.serviceIo.equals(o.serviceIo)) return 0;
				ret = serviceIo != null ? this.serviceIo.hashCode() - o.serviceIo.hashCode() : 0;
			}
		}
		return ret;
	}
}
