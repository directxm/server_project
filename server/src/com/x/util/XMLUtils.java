package com.x.util;

import org.dom4j.Element;

/**
 * 类名: XMLUtils </br>
 * 描述: xml工具类 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-13 </br>
 * 发布版本: V1.0 </br>
 */
public class XMLUtils
{
	public static String getTextString(Element elementObject, String key)
	{
		if(elementObject != null && key != null)
		{
			try
			{
				Element e = elementObject.element(key);
				return e.getText();
			}
			catch(Exception ignored)
			{

			}
		}
		return "";
	}

	public static long getTextLong(Element elementObject, String key)
	{
		if(elementObject != null && key != null)
		{
			try
			{
				Element e = elementObject.element(key);
				return Long.parseLong(e.getText());
			}
			catch(Exception ignored)
			{

			}
		}
		return 0L;
	}

	public static int getTextInteger(Element elementObject, String key)
	{
		if(elementObject != null && key != null)
		{
			try
			{
				Element e = elementObject.element(key);
				return Integer.decode(e.getText());
			}
			catch(Exception ignored)
			{

			}
		}
		return -1;
	}

	public static float getTextFloat(Element elementObject, String key)
	{
		if(elementObject != null && key != null)
		{
			try
			{
				Element e = elementObject.element(key);
				return Float.valueOf(e.getText());
			}
			catch(Exception ignored)
			{

			}
		}
		return 0.0f;
	}
}
