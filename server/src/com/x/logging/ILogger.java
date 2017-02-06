package com.x.logging;

/**
* 类名: ILogger </br>
* 描述: log基类接口 </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-8 </br>
* 发布版本: V1.0 </br>
 */
public interface ILogger
{
	void trace(String msg);

	void trace(String msg, Object... objects);

	void trace(String msg, Throwable throwable);

	void debug(String msg);

	void debug(String msg, Object... objects);

	void debug(String msg, Throwable throwable);

	void info(String msg);

	void info(String msg, Object... objects);

	void info(String msg, Throwable throwable);

	void warn(String msg);

	void warn(String msg, Object... objects);

	void warn(String msg, Throwable throwable);

	void error(String msg);

	void error(String msg, Object... objects);

	void error(String msg, Throwable throwable);

	void trace(Class c, String msg);

	void trace(Class c, String msg, Object... objects);

	void trace(Class c, String msg, Throwable throwable);

	void debug(Class c, String msg);

	void debug(Class c, String msg, Object... objects);

	void debug(Class c, String msg, Throwable throwable);

	void info(Class c, String msg);

	void info(Class c, String msg, Object... objects);

	void info(Class c, String msg, Throwable throwable);

	void warn(Class c, String msg);

	void warn(Class c, String msg, Object... objects);

	void warn(Class c, String msg, Throwable throwable);

	void error(Class c, String msg);

	void error(Class c, String msg, Object... objects);

	void error(Class c, String msg, Throwable throwable);
}
