package com.x.wechat;

import com.x.logging.Logger;
import com.x.wechat.request.WeChatRequest;
import com.x.wechat.request.RequestLoader;
import com.x.util.ResourceUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 类名: WeChatRequestLoader </br>
 * 描述: 微信的请求数据预加载器 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 *           废弃
 */
public class WeChatRequestLoader extends RequestLoader
{
	protected boolean initialized = false;

	private static final WeChatRequestLoader INSTANCE = new WeChatRequestLoader();
	static
	{
		INSTANCE.init();
	}

	public static WeChatRequestLoader getInstance()
	{
		return INSTANCE;
	}

	@Override
	public synchronized WeChatRequestLoader init()
	{
		if(initialized)
			return this;
		List<String> paths = ResourceUtils.findResources("META-INF/requests/", ".xml");
		Logger.debug(this.getClass(), "RequestPaths: {}", paths);

		for(String file : paths)
		{
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(file))
				{
					Document document = builder.parse(is);
					Element root = document.getDocumentElement();
					String default_package = root.getAttribute("package");
					NodeList list = root.getElementsByTagName("request");
					for(int i = 0; i < list.getLength(); ++i)
					{
						Element node = (Element)list.item(i);
						try
						{
							requests.put(node.getAttribute("type"), (WeChatRequest)Class.forName(default_package + "." + node.getAttribute("name")).newInstance());
						}
						catch(Exception e)
						{
							Logger.error(this.getClass(), "init", e);
							throw new RuntimeException("此类" + default_package + "." + node.getAttribute("name") + "无法加载，文件是: " + file, e);
						}
					}
				}
				Logger.debug(this.getClass(), "Load {} finished.", file);
			}
			catch(ParserConfigurationException | SAXException | IOException e)
			{
				Logger.error(this.getClass(), "init", e);
				throw new RuntimeException("无法加载 " + file, e);
			}
		}
		initialized = true;
		return this;
	}
}
