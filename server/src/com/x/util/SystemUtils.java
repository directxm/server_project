package com.x.util;

import com.x.logging.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 12/30/15 1:05 PM
 */
public class SystemUtils
{
	public static int getPid()
	{
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName(); // format: "pid@hostname"
		try
		{
			return Integer.parseInt(name.substring(0, name.indexOf('@')));
		}
		catch(Exception e)
		{
			Logger.error(SystemUtils.class, "SystemUtils.getPid() caught exception: " + e);
			return 65536;
		}
	}
}
