package com.x.config;

import java.lang.reflect.Field;

/**
* 类名: Config </br>
* 描述: xml节点基类 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public abstract class Config<K> implements Cloneable
{
	protected ConfigLoader loader = ConfigLoader.getInstance();

	public abstract K getID();

	public void dump()
	{
		System.out.print(this.getClass().getSimpleName() + ":dump");
		Field[] fields = this.getClass().getFields();
		for(Field field : fields)
		{
			try
			{
				System.out.print(":" + field.getName() + "=" + field.get(this) + "");
			}
			catch(IllegalAccessException ignored)
			{

			}
		}
		System.out.println();
	}

	public final ConfigLoader getLoader()
	{
		return loader;
	}

	public final void invokePreparePostLoad() throws Exception
	{
		this.preparePostLoad();
	}

	public final void invokePostLoad() throws Exception
	{
		this.postLoad();
	}

	public void preparePostLoad() throws Exception
	{

	}

	public void postLoad() throws Exception
	{

	}
}
