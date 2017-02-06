package com.x.wechat.common.request;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: LocationMessage </br>
* 描述: 请求消息之地理位置消息 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class LocationMessage extends BaseMessage
{
    // 地理位置维度
    private float locationX;
    // 地理位置经度
    private float locationY;
    // 地图缩放大小
    private int scale;
    // 地理位置信息
    private String label;


	public float getLocationX()
	{
		return locationX;
	}

	public void setLocationX(float locationX)
	{
		this.locationX = locationX;
	}

	public float getLocationY()
	{
		return locationY;
	}

	public void setLocationY(float locationY)
	{
		this.locationY = locationY;
	}

	public int getScale()
	{
		return scale;
	}

	public void setScale(int scale)
	{
		this.scale = scale;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	@Override
	public void parseXML(Element element)
	{
		this.locationX = XMLUtils.getTextFloat(element, "Location_X");
		this.locationY = XMLUtils.getTextFloat(element, "Location_Y");
		this.scale = XMLUtils.getTextInteger(element, "Scale");
		this.label = XMLUtils.getTextString(element, "Label");

		super.parseXML(element);
	}
}