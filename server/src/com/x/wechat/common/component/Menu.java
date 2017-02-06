package com.x.wechat.common.component;

import com.x.wechat.common.JSONSerializable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: Menu </br>
 * 描述: 整个菜单对象的封装 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
@Root(strict = false)
public class Menu implements JSONSerializable
{
	@ElementList(inline = true, entry = "button")
	private List<Button> buttons = new ArrayList<>();


	public List<Button> getButtons()
	{
		return buttons;
	}

	public void setButtons(List<Button> buttons)
	{
		this.buttons = buttons;
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
