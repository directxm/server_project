package com.x.wechat.common.component;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: ComplexButton </br>
 * 描述: 父菜单项: 包含有二级菜单项的一级菜单. 这类菜单项包含有二个属性: name和subButtons, 而subButtons以是一个子菜单项数组 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
@Root(strict = false)
public class ComplexButton extends Button
{
	@ElementList(inline = true, entry = "button")
	private List<Button> subButtons = new ArrayList<>();


	public List<Button> getSubButtons()
	{
		return subButtons;
	}

	public void setSubButtons(List<Button> subButtons)
	{
		this.subButtons = subButtons;
	}
}
