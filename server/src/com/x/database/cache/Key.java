package com.x.database.cache;

import java.io.Serializable;

/**
 *
 * @author yanfengbing
 * @version 1.0
 * @since 2/11/14 6:10 PM
 */
/**
* 类名: Key </br>
* 描述: 多主键使用 同时必须实现hashCode和equals </br>
* 开发人员: fatum </br>
* 创建时间: 16-10-10 </br>
* 发布版本: V1.0 </br>
 */
public interface Key extends Serializable, Cloneable
{
	/**
	 *
	 * @param bound 必须大于0
	 * @return 必须大于等于0 小于bound
	 */
	int hash(int bound);
}
