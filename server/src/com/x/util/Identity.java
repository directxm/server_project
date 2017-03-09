package com.x.util;


/**
 * @author yanfengbing
 * @since 13-10-12 PM4:36
 */
public class Identity
{
	private static final long __INIT_SECOND = 1458209551282L; //2016年3月17日18:13:00

	private static long __LAST_MILLI_SECOND = 0;
	private static int __INC = 0;

	private static boolean initial = false;
	private static int LOCAL_ID = 0;

	public synchronized static long id()
	{
		if(!initial)
		{
			throw new RuntimeException("Identity does not supported at this server!");
		}
		do
		{
			long NOW_SECOND = System.currentTimeMillis() - __INIT_SECOND;
			if(NOW_SECOND != __LAST_MILLI_SECOND)
			{
				__INC = 0;
			}
			else
			{
				++__INC;
				if(__INC >= 0xFFF)
				{
					continue;
				}
			}
			__LAST_MILLI_SECOND = NOW_SECOND;
			return Math.abs(NOW_SECOND << 22 | LOCAL_ID << 12 | __INC);
		}
		while(true);
	}

	public synchronized static boolean isLocal(long id)
	{
		return ((id & 0x003FF000L) >>> 12) == LOCAL_ID;
	}

	public synchronized static void setLocalId(int localId)
	{
		LOCAL_ID = localId;
		initial = true;
	}
}
