package com.x.config;

import com.x.logging.Logger;

import javax.swing.*;
import java.io.File;

/**
* 类名: ConfigLoader </br>
* 描述: xml文件验证器 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-10 </br>
* 发布版本: V1.0 </br>
 */
public class ConfigValidator
{
	public static void main(String[] args)
	{
		File file = new File("./config");
		if(args.length == 1)
		{
			file = new File(args[0]);
		}
		if(!file.isDirectory())
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("请选择配置文件所在目录，一般是叫config的目录");
			chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			int ret = chooser.showDialog(chooser, "冲吧，少年！");
			if(ret == JFileChooser.APPROVE_OPTION)
			{
				file = chooser.getSelectedFile();
				if(file == null || !file.isDirectory())
				{
					Logger.error(ConfigValidator.class, "要选择目录啊，少年");
					System.exit(1);
				}
			}
			else
			{
				System.exit(1);
			}
		}
		else
		{
			Logger.debug(ConfigValidator.class, "读取目录: " + file.getAbsolutePath());
		}
		String configPath = file.getAbsolutePath();
		try
		{
			ConfigFactory factory = (ConfigFactory)Class.forName(System.getProperty("config.factory")).newInstance();
			ConfigLoader.loadAll(factory, configPath, false);
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException e)
		{
			Logger.error(ConfigValidator.class, "Java启动 -Dconfig.factory 参数的值不对, 生成不了相应的ConfigFactory, 这个找服务器程序员.", e);
			error();
		}
		catch(ConfigException e)
		{
			String configName = e.getConfigName();
			if(configName == null)
			{
				Logger.error(ConfigValidator.class, "有奇怪的错误,请联系任何一个服务器程序员,给他看下面的输出内容:", e);
			}
			else
			{
				String fileName = getFileName(configPath, configName);
				if(fileName == null)
				{
					Logger.error(ConfigValidator.class, "{} 出错了, 具体错误参看上面输出, 找不到它的配置文件, 应该是配置文件被删掉了", configName);
					fileName = configPath;
				}
				else
				{
					Logger.error(ConfigValidator.class, "{} 出错了, 具体错误参看上面输出, 配置文件在这里 {}", configName, fileName);
				}
				try
				{
					dumpSubversionInfo(fileName);
				}
				catch(Exception se)
				{
					Logger.error(ConfigValidator.class, "本来想试图看看是谁应该接这个锅的,但是无奈有错误发生了,所以看不到了", se);
				}
			}

			error();
		}
		catch(Exception e)
		{
			Logger.error(ConfigValidator.class, "有奇怪的错误,请联系任何一个服务器程序员,给他看下面的输出内容:", e);
			error();
		}

		Logger.debug(ConfigValidator.class, "校验配置成功，可以尝试上传脚本了。");
	}

	private static void error()
	{
		Logger.error(ConfigValidator.class, "校验配置失败，以上是错误代码。");
		Logger.error(ConfigValidator.class, "校验配置失败，以上是错误代码。");
		Logger.error(ConfigValidator.class, "校验配置失败，以上是错误代码。");

		System.exit(2);
	}

	private static String getFileName(String configPath, String configName)
	{
		String filePrefix = configPath + File.separator + configName.toLowerCase();
		File is = new File(filePrefix + ".xml");
		if(is.exists() && is.isFile())
		{
			return is.getAbsolutePath();
		}
		else
		{
			is = new File(filePrefix);
			if(is.exists() && is.isDirectory())
			{
				return is.getAbsolutePath();
			}
		}
		return null;
	}

	private static void dumpSubversionInfo(String filePath) throws Exception
	{
		Process process = Runtime.getRuntime().exec(new String[]{"svn", "info", filePath});

		process.waitFor();

		int ret = process.exitValue();
		if(ret != 0)
		{
			java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()));
			String str = "";
			String line;
			while((line = reader.readLine()) != null)
			{
				str += line + System.getProperty("line.separator", "\n");
			}
			throw new Exception(str);
		}
		else
		{
			java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
			String line;
			while((line = reader.readLine()) != null)
			{
				if(line.contains("Last Changed Author: "))
				{
					String user = line.substring(line.indexOf("Last Changed Author: ") + "Last Changed Author: ".length());
					Logger.error(ConfigValidator.class, "背锅侠: {} -> {}", user, user.substring(user.lastIndexOf('_') + 1));
					Logger.error(ConfigValidator.class, "背锅侠: {} -> {}", user, user.substring(user.lastIndexOf('_') + 1));
					Logger.error(ConfigValidator.class, "背锅侠: {} -> {}", user, user.substring(user.lastIndexOf('_') + 1));
					Logger.error(ConfigValidator.class, "重要的事情说三遍!!!");
				}
				else if(line.contains("Last Changed Date: "))
				{
					Logger.error(ConfigValidator.class, "产锅时间: " + line.substring(line.indexOf("Last Changed Date: ") + "Last Changed Date: ".length()));
				}
			}
		}
	}
}
