package com.x.util;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 7/23/15 6:21 PM
 */
public class CompareUtils
{
	public static int compare(byte[] a, byte[] b)
	{
		if(a == null && b == null) return 0;
		if(a == b) return 0;
		if(a == null) return -1;
		if(b == null) return 1;
		int c = a.length - b.length;
		if(c != 0) return c;
		for(int i = 0; i < a.length; ++i)
		{
			c = a[i] - b[i];
			if(c != 0)
				return c;
		}
		return c;
	}
}
