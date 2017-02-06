package com.x.io;

/**
 * 类名: ILoader </br>
 * 描述: key,value 的加载模板接口 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-11 </br>
 * 发布版本: V1.0 </br>
 */
public interface Loader<K, V>
{
	Loader<K, V> init();

	V load(K t);
}
