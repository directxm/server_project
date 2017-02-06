package com.x.network.service;

import com.x.io.CodeException;
import com.offbynull.coroutines.user.Continuation;
import com.x.network.standalone.ServicePath;

/**
 * @author yanfengbing
 * @since 13-6-27 上午10:29
 */
public interface Processor<P>
{
	void process(Continuation c, ServiceWrapper service, ServicePath sender, P protocol) throws CodeException;
}
