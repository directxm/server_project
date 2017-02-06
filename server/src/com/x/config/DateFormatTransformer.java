package com.x.config;

import org.simpleframework.xml.transform.Transform;

import java.text.DateFormat;
import java.util.Date;

/**
* 类名: DateFormatTransformer </br>
* 描述: Data xml格式解析 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-10 </br>
* 发布版本: V1.0 </br>
 */
public class DateFormatTransformer implements Transform<Date>
{
	private DateFormat dateFormat;

	public DateFormatTransformer(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	@Override
	public Date read(String s) throws Exception
	{
		return dateFormat.parse(s);
	}

	@Override
	public String write(Date date) throws Exception
	{
		return dateFormat.format(date);
	}
}
