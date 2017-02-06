package com.x.network.service;

import com.x.io.CodeException;
import com.offbynull.coroutines.user.Continuation;
import com.x.network.standalone.ServicePath;
import com.x.network.io.Protocol;
import com.x.network.standalone.ServiceProvider;

/**
 * 此类坚决不能实现自己的hashCode和equals.
 *
 * @author yanfengbing
 * @version 1.0
 * @since 7/3/15 5:49 PM
 */
public class ServiceWrapper
{
	private ServiceSystem system;
	private ServiceProvider provider;
	private long pid;
	private ServiceCoroutine coroutine;

	protected ServiceWrapper(ServiceSystem system, ServiceProvider provider, long pid, ServiceCoroutine coroutine)
	{
		this.system = system;
		this.provider = provider;
		this.pid = pid;
		this.coroutine = coroutine;
	}

	public long getPid()
	{
		return pid;
	}

	public long getCid()
	{
		return coroutine.getCid();
	}

	public ServiceSystem getSystem()
	{
		return system;
	}

	public ServiceSelection find(String name)
	{
		return system.find(name);
	}

	public ServiceProvider getProvider()
	{
		return provider;
	}

	public void removeProvider(String abbr)
	{
		system.removeServiceProvider(abbr);
	}

	public ServicePipe getServicePipe()
	{
		return system.getServicePipe(pid);
	}

	public void tell(Object message)
	{
		ServicePipe pipe = system.getServicePipe(pid);
		if(pipe != null)
		{
			coroutine.tell(pipe.getRemotePath(), message);
		}
	}

	public void tell(ServicePath receiver, Object message)
	{
		coroutine.tell(receiver, message);
	}

	public void tell(ServiceSelection selection, Object message)
	{
		coroutine.tell(selection, message);
	}

	public void answer(ServicePath receiver, Protocol request, Protocol response)
	{
		//response.setSerial(request.getSerial());
		coroutine.tell(receiver, response);
	}

	public <P extends Protocol, V extends Protocol>
	V ask(Continuation c, ServicePath receiver, P sendMessage, Class<V> receivedMessageClass, int timeout) throws CodeException
	{
		return coroutine.ask(c, receiver, sendMessage, timeout, receivedMessageClass);
	}

	public <P extends Protocol, V extends Protocol>
	V ask(Continuation c, ServiceSelection receiver, P sendMessage, Class<V> receivedMessageClass, int timeout) throws CodeException
	{
		return coroutine.ask(c, receiver, sendMessage, timeout, receivedMessageClass);
	}

	/*public String doHttpGet(Continuation c, String url, Map<String, String> params, int timeout) throws CodeException
	{
		return coroutine.doHttpGet(c, pid, url, params, timeout);
	}

	public String doHttpPost(Continuation c, String url, String param, int timeout) throws CodeException
	{
		return coroutine.doHttpPost(c, pid, url, param, timeout);
	}*/

	public boolean isRpc()
	{
		return coroutine.isRpc();
	}

	@Override
	public String toString()
	{
		return "ServiceWrapper{" +
				"" + pid +
				"," + coroutine.getCid() +
				'}';
	}
}
