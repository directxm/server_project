package com.x.wechat.common.component;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 类名: CommonButton </br>
 * 描述: 子菜单项: 没有子菜单的菜单项, 有可能是二级菜单项, 也有可能是不含二级菜单的一级菜单 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
@Root(strict = false)
public class CommonButton extends Button
{
	@Element
	private String type;
	@Element
    private String key;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}
}
