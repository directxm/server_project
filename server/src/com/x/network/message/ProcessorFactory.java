package com.x.network.message;

import com.x.logging.Logger;
import com.x.util.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.HashMap;

/**
 * 类名: ProcessorFactory </br>
 * 描述: 处理对象工厂类 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-12 </br>
 * 发布版本: V1.0 </br>
 */
public abstract class ProcessorFactory<T>
{
	//private static RequestProcessorFactory INSTANCE;

	protected final HashMap<String, Class<? extends T>> processors = new HashMap<>();

	/**
	 * 这个方法使用了单例模式的懒惰初始化方法（Double-Check）同时也避免了地址正确但是对象并未初始化完成时的一些问题。
	 * 但是这个做法是一个Anti-Pattern的实现方式，并不推荐使用，在这里使用也是没有办法。
	 * 如果仅仅写单例模式的话，推荐使用嵌套类的Holder来做，或者直接使用静态变量，JVM会保证只加载一次，并且会保证线程安全。
	 * 但是我们这里不光是要new对象出来，还要调用init方法，所以，只能采用这样的写法。
	 *
	 * @return 静态的ProcessorFactory
	 */
	public static ProcessorFactory defaultInstance()
	{
		ProcessorFactory INSTANCE = null;

		if(INSTANCE == null)
		{
			synchronized(ProcessorFactory.class)
			{
				if(INSTANCE == null)
				{
					INSTANCE = new ProcessorFactory()
					{
						@Override
						public ProcessorFactory init()
						{
							init("/META-INF/processors.xml");
							return this;
						}
					}.init();
				}
			}
		}
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	protected final void init(String filename)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			URL processors_xml = this.getClass().getResource(filename);
			Document document = builder.parse(processors_xml.openStream());
			Element root = document.getDocumentElement();
			String defaultPackage = root.getAttribute("package");
			NodeList list = root.getElementsByTagName("process");
			for(int i = 0; i < list.getLength(); ++i)
			{
				Element node = (Element)list.item(i);
				this.processors.put(node.getAttribute("ref"), ((Class<T>)Class.forName(defaultPackage + "." + node.getAttribute("ref") + "Processor")));
			}
		}
		catch(Exception e)
		{
			Logger.warn(ProcessorFactory.class, "无法加载 {}, {}, {}", filename, ExceptionUtils.getExceptionMessage(e), ExceptionUtils.getExceptionStackTraceFileLineAndMethod(e));
		}
	}

	public abstract ProcessorFactory init();

	public T create(String simpleName)
	{
		try
		{
			return processors.get(simpleName).newInstance();
		}
		catch(Exception ignored)
		{
		}
		return null;
	}
}
