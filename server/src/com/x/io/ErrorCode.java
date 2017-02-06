package com.x.io;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yanfengbing
 * @since 13-12-7 PM5:02
 */
public interface ErrorCode
{
	Map<Integer, ErrorCode> AllCodes = new HashMap<Integer, ErrorCode>();

	int code();

	String message();
}
