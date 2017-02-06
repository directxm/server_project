package com.x.config;

import com.x.lang.TimeSpan;
import org.simpleframework.xml.transform.Transform;

/**
* 类名: DateFormatTransformer </br>
* 描述: TimeSpan xml格式解析 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-10 </br>
* 发布版本: V1.0 </br>
 */
public class TimeSpanTransformer implements Transform<TimeSpan>
{
	@Override
	public TimeSpan read(String s) throws Exception
	{
		return TimeSpan.parse(s);
	}

	@Override
	public String write(TimeSpan timeSpan) throws Exception
	{
		return timeSpan.format();
	}
}
