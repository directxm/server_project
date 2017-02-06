package com.x.process;

import com.x.io.CodeException;

/**
 * 类名: Processor </br>
 * 描述: 处理接口 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 */
@Deprecated
public interface Processor<T>
{
	String process(T request) throws CodeException;
}
