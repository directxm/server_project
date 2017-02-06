package com.x.websocket.protocol;

import com.alibaba.fastjson.JSONObject;
import com.x.network.io.Protocol;
import com.x.util.JSONUtils;
import com.x.websocket.io.MarshalException;
import com.x.websocket.io.UnMarshalException;

/**
 * Created by fatum on 2016/11/11.
 */
public abstract class JSONProtocol implements Protocol
{
	protected int protocolType;

	public JSONProtocol(int protocolType)
	{
		this.protocolType = protocolType;
	}

	@Override
	public int getType()
	{
		return protocolType;
	}

	public abstract JSONProtocol clone() throws CloneNotSupportedException;

	public JSONObject marshal() throws MarshalException
	{
		JSONObject jsonObject = new JSONObject();
		//jsonObject.put("protocolType", protocolType);

		return jsonObject;
	}

	public void unmarshal(JSONObject jsonObject) throws UnMarshalException
	{
		if(jsonObject != null)
		{
			this.protocolType = JSONUtils.getInteger(jsonObject, "protocolType");
		}
	}

}
