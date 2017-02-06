package com.x.wechat.common.event;

import com.x.util.XMLUtils;
import org.dom4j.Element;

/**
* 类名: LocationEvent </br>
* 描述: 上报地理位置事件 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public class LocationEvent extends BaseEvent
{
	// 地理位置纬度
	private String latitude;
	// 地理位置经度
	private String longitude;
	// 地理位置精度
	private String precision;


	public String getLatitude()
	{
		return latitude;
	}

	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	public String getLongitude()
	{
		return longitude;
	}

	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

	public String getPrecision()
	{
		return precision;
	}

	public void setPrecision(String precision)
	{
		this.precision = precision;
	}

	@Override
	public void parseXML(Element element)
	{
		this.latitude = XMLUtils.getTextString(element, "Latitude");
		this.longitude = XMLUtils.getTextString(element, "Longitude");
		this.precision = XMLUtils.getTextString(element, "Precision");

		super.parseXML(element);
	}
}
