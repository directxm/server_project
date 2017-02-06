package com.game;

import com.x.logging.Logger;
import com.x.network.ServiceThreadPool;
import com.x.network.io.State;
import com.x.network.service.ServicePipe;
import com.x.network.standalone.ServiceProvider;
import com.x.network.service.ServiceSystem;
import com.x.util.ExceptionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fatum on 2016/11/7.
 */
@ServerEndpoint("/city")
public class UserEndpoint
{
    /*static
    {
        ServiceSystem.createInstance("test", 0, new ServiceThreadPool("test", 10));
    }*/
    protected static ConcurrentHashMap<Long, ServicePipe> pipes = new ConcurrentHashMap<>();

    private ServiceSystem serviceSystem = ServiceSystem.getInstance();
    private long pid = 0;

	@OnOpen
    public void onOpen(Session session)
	{
        pid = serviceSystem.nextId();
        ServicePipe pipe = new ServicePipe(serviceSystem, new ServiceProvider("test", null, null), pid, null, null)
        {
            private final Session innerSession = session;

            @Override
            public boolean isOpened()
            {
                synchronized(innerSession)
                {
                    return innerSession.isOpen();
                }
            }

            @Override
            public void close()
            {
                synchronized(innerSession)
                {
                    try
                    {
                        innerSession.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                        Logger.error(this.getClass(), e.toString());
                    }
                }
            }

            @Override
            protected void innerTell(Object message)
            {
                synchronized(innerSession)
                {
                    if(innerSession.isOpen())
                    {
                        try
                        {
                            // ������Ϣ
                            innerSession.getBasicRemote().sendText(message.toString());
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                            Logger.error(this.getClass(), "��Ϣ����ʧ��: " + e.toString());
                        }
                    }
                }
            }

            @Override
            public void secureInput(byte[] bytes)
            {

            }

            @Override
            public void secureOutput(byte[] bytes)
            {

            }

            @Override
            public void onSetState(State state)
            {

            }
        };
        pipes.put(pipe.getPid(), pipe);
        serviceSystem.addServicePipe(pipe);
        pipe.pipeOpened();
	}

	@OnMessage
    public void onMessage(String message, Session session) throws IOException
    {
        Logger.debug(this.getClass(), "UserEndpoint::onMessage message =" + message + " thread =" + Thread.currentThread().getId());
        ServicePipe pipe = pipes.remove(pid);
        if(pipe != null)
        {
            pipe.pipeReceivedMessage(message);
        }
    }

	@OnError
    public void onError(Throwable throwable)
    {
        ServicePipe pipe = pipes.remove(pid);
        if(pipe != null)
        {
            pipe.pipeCaughtException(throwable);
        }
    }

	@OnClose
    public void onClose(Session session)
    {
        ServicePipe pipe = pipes.remove(pid);
        if(pipe != null)
        {
            serviceSystem.removeServicePipe(pipe);
            try
            {
                pipe.pipeClosed();
            }
            catch(Exception e)
            {
                Logger.warn(this.getClass(), "{} close caught exception {}{}", pipe.toString(), e.toString(), ExceptionUtils.getExceptionErrorCode(e));
            }
        }

    }
}
