package com.x.network.io;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类名: ${CLASS_NAME} </br>
 * 描述: 语音消息 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-27 </br>
 * 发布版本: V1.0 </br>
 */
public class Rpc
{
	private static final AtomicInteger XID = new AtomicInteger(0);

	private long cid;
	private int xid;
	private int retCode = 0;
	private String msg;

	private Object content;

	public Rpc(long cid, Object content)
	{
		this.cid = cid;
		this.xid = XID.incrementAndGet();
		this.content = content;
	}


	public int getId()
	{
		return this.xid & 0x7FFFFFFF;
	}

	public boolean isRequest()
	{
		return (this.xid & 0x80000000) != 0;
	}

	public void setRequest()
	{
		this.xid |= 0x80000000;
	}

	public void setResponse()
	{
		this.xid &= 0x7FFFFFFF;
	}

	public long getCid()
	{
		return cid;
	}

	public int getXid()
	{
		return xid;
	}

	public int getRetCode()
	{
		return retCode;
	}

	public void setRetCode(int retCode)
	{
		this.retCode = retCode;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Object getContent()
	{
		return content;
	}

	public void setContent(Object content)
	{
		this.content = content;
	}

}
