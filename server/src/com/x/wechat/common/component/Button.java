package com.x.wechat.common.component;

import com.x.wechat.common.JSONSerializable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 类名: Button </br>
 * 描述: 菜单项的基类 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
@Root(strict = false)
public class Button implements JSONSerializable
{
	@Element
	private String name; // 所有一级菜单、二级菜单都共有一个相同的属性，那就是name


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void parseJSON(String str)
	{

	}

	@Override
	public String toJSON()
	{
		return null;
	}
}
