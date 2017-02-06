package com.x.websocket.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.x.io.Loader;
import com.x.util.JSONUtils;
import com.x.websocket.protocol.JSONProtocol;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Map;

/**
 * 类名: JSONProtocolDecoder </br>
 * 描述: websocket 的每个 endpoint 都会创建一个decoder </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-11-11 </br>
 * 发布版本: V1.0 </br>
 */
public class JSONProtocolDecoder implements Decoder.Text<JSONProtocol>
{
	protected Loader<Integer, JSONProtocol> protocolLoader = null;

	public JSONProtocolDecoder()
	{
		super();
	}

	@Override
	public JSONProtocol decode(String s) throws DecodeException
	{
		JSONObject jsonObject = JSON.parseObject(s);
		if(jsonObject == null)
			throw new DecodeException(s, "json parse object is null!");

		int type = JSONUtils.getInteger(jsonObject, "protocolType");
		JSONProtocol protocol = protocolLoader.load(type);
		if(protocol == null)
			throw new DecodeException(s, "protocol type not exists!");

		protocol.unmarshal(jsonObject);
		return protocol;
	}

	@Override
	public boolean willDecode(String s)
	{
		return (s != null);
	}

	@Override
	public void init(EndpointConfig endpointConfig)
	{
		Map<String, Object> properties = endpointConfig.getUserProperties();
		if(properties.containsKey("ProtocolLoader"))
			protocolLoader = (Loader<Integer, JSONProtocol>)properties.get("ProtocolLoader");
		// do nothing
	}

	@Override
	public void destroy()
	{
		// do nothing
	}
}
