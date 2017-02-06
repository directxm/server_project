package com.x.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collection;

/**
* 类名: OSUtils </br>
* 描述: json工具 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class JSONUtils
{
	private static final SerializerFeature[] serializerFeatures = new SerializerFeature[]{
			SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.WriteNullNumberAsZero,
			SerializerFeature.WriteNullBooleanAsFalse,
			SerializerFeature.WriteEnumUsingName,
			SerializerFeature.IgnoreNonFieldGetter,
	};
	private static final Feature[] deserializerFeatures = new Feature[]{
			Feature.IgnoreNotMatch,
			Feature.SupportArrayToBean,
	};

	private static final SerializeConfig serializeConfig = new SerializeConfig();

	@SuppressWarnings("unchecked")
	public static <T> T parseObject(String str, Class<T> tClass)
	{
		if(str == null || tClass == null) return null;
		if(Collection.class.isAssignableFrom(tClass))
		{
			throw new UnsupportedOperationException("因为类型擦除, 所以不支持直接解析容器, 请用类将其包裹起来.");
		}
		return JSON.parseObject(str, tClass, deserializerFeatures);
	}

	public static <T> T parseObject(byte[] bytes, Class<T> tClass)
	{
		if(bytes == null || tClass == null) return null;
		if(Collection.class.isAssignableFrom(tClass))
		{
			throw new UnsupportedOperationException("因为类型擦除, 所以不支持直接解析容器, 请用类将其包裹起来.");
		}
		return JSON.parseObject(bytes, tClass, deserializerFeatures);
	}

	@SuppressWarnings("unchecked")
	public static Object parse(String str)
	{
		return JSON.parse(str, deserializerFeatures);
	}

	public static byte[] toJSONBytes(Object object)
	{
		return JSON.toJSONBytes(object, serializeConfig, serializerFeatures);
	}

	public static String toJSONString(Object object)
	{
		return JSON.toJSONString(object, serializeConfig, serializerFeatures);
	}

	public static boolean containsKey(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			return jsonObject.containsKey(key);
		}

		return false;
	}

	public static boolean getBoolean(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return Boolean.parseBoolean(jsonObject.get(key).toString());
			}
			catch(Exception ignored)
			{

			}
		}
		return false;
	}

	public static byte getByte(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return Byte.parseByte(jsonObject.get(key).toString());
			}
			catch(Exception ignored)
			{

			}
		}
		return 0;
	}

	public static int getInteger(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return Integer.parseInt(jsonObject.get(key).toString());
			}
			catch(Exception ignored)
			{

			}
		}
		return 0;
	}

	public static long getLong(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return Long.parseLong(jsonObject.get(key).toString());
			}
			catch(Exception ignored)
			{

			}
		}
		return 0;
	}

	public static float getFloat(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return Float.parseFloat(jsonObject.get(key).toString());
			}
			catch(Exception ignored)
			{

			}
		}
		return 0;
	}

	public static String getString(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return jsonObject.get(key).toString();
			}
			catch(Exception ignored)
			{

			}
		}
		return null;
	}

	public static JSONArray getJSONArray(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return (JSONArray)jsonObject.get(key);
			}
			catch(Exception ignored)
			{

			}
		}
		return null;
	}

	public static JSONObject getJSONObject(JSONObject jsonObject, String key)
	{
		if(jsonObject != null && key != null)
		{
			try
			{
				return (JSONObject)jsonObject.get(key);
			}
			catch(Exception ignored)
			{

			}
		}
		return null;
	}
}
