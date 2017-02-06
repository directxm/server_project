package com.x.websocket.codec;

import com.alibaba.fastjson.JSONObject;
import com.x.logging.Logger;
import com.x.websocket.protocol.JSONProtocol;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * 类名: JSONProtocolDecoder </br>
 * 描述: websocket 的每个 endpoint encoder </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-11-11 </br>
 * 发布版本: V1.0 </br>
 */
public class JSONProtocolEncoder implements Encoder.Text<JSONProtocol>
{
	@Override
	public String encode(JSONProtocol protocol) throws EncodeException
	{
		if(protocol != null)
		{
			JSONObject jsonObject = protocol.marshal();
			if(jsonObject != null)
				return jsonObject.toJSONString();
		}
		return null;
	}

	@Override
	public void init(EndpointConfig endpointConfig)
	{
		//Map<String, Object> properties = endpointConfig.getUserProperties();
		Logger.debug(this.getClass(), "JSONProtocolEncoder initialized");
	}

	@Override
	public void destroy()
	{

	}
}
