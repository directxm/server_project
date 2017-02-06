package com.x.util;

import com.x.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 10/28/15 3:02 PM
 */
public class ResourceUtils
{
	public static List<String> findResources(String path, String suffix)
	{
		List<String> filePaths = new LinkedList<>();
		Enumeration<URL> dirs;
		try
		{
			dirs = ResourceUtils.class.getClassLoader().getResources(path);
			while(dirs.hasMoreElements())
			{
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if("file".equals(protocol))
				{
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findFiles(path, filePath, suffix, true, filePaths);
				}
				else if("jar".equals(protocol))
				{
					JarFile jar = null;
					try
					{
						JarURLConnection jarURLConnection = ((JarURLConnection)url.openConnection());
						jarURLConnection.setUseCaches(false);
						jar = jarURLConnection.getJarFile();
						Enumeration<JarEntry> entries = jar.entries();
						while(entries.hasMoreElements())
						{
							JarEntry entry = entries.nextElement();
							if(!entry.isDirectory())
							{
								String name = entry.getName();
								if(name.endsWith(suffix))
								{
									if(name.charAt(0) == '/')
									{
										name = name.substring(1);
									}
									if(name.startsWith(path))
									{
										filePaths.add(entry.getName());
									}
								}
							}
						}
					}
					catch(IOException ignored)
					{
					}
					finally
					{
						if(jar != null)
						{
							jar.close();
						}
					}
				}
			}
		}
		catch(IOException ignored)
		{

		}
		return filePaths;
	}

	private static void findFiles(String path, String realPath, final String suffix, final boolean recursive, List<String> filePaths)
	{
		if(realPath == null || path == null) return;
		File dir = new File(realPath);
		if(!dir.exists() || !dir.isDirectory())
		{
			Logger.warn(ResourceUtils.class, "RealPath: " + realPath + " does not exist or is not a directory.");
			return;
		}
		File[] files = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(suffix)));
		for(File file : files)
		{
			if(file.isDirectory())
			{
				findFiles(path + '/' + file.getName(), file.getAbsolutePath(), suffix, recursive, filePaths);
			}
			else
			{
				filePaths.add(path + '/' + file.getName());
			}
		}
	}
}
