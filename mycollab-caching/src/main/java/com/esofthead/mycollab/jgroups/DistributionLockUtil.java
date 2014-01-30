/**
 * This file is part of mycollab-caching.
 *
 * mycollab-caching is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-caching is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-caching.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.jgroups;

import java.util.concurrent.locks.Lock;

import org.infinispan.CacheImpl;
import org.infinispan.remoting.transport.Transport;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.Channel;
import org.jgroups.blocks.locking.LockService;
import org.jgroups.fork.ForkChannel;
import org.jgroups.protocols.CENTRAL_LOCK;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.ProtocolStack;

import com.esofthead.mycollab.cache.LocalCacheManager;
import com.esofthead.mycollab.core.MyCollabException;

public class DistributionLockUtil {
	private static ForkChannel getChannel() {
		CacheImpl<Object, Object> cache = (CacheImpl<Object, Object>) LocalCacheManager
				.getCache();
		Transport tp;
		ForkChannel fork_ch;
		tp = cache.getAdvancedCache().getRpcManager().getTransport();
		Channel main_ch = ((JGroupsTransport) tp).getChannel();
		try {
			fork_ch = new ForkChannel(main_ch, "hijack-stack", "lead-hijacker",
					true, ProtocolStack.ABOVE, CENTRAL_LOCK.class);
			return fork_ch;
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	public static Lock getLock(String lockName) {
		ForkChannel channel = getChannel();

		ProtocolStack protocolStack = channel.getProtocolStack();
		Protocol tmp = protocolStack.getDownProtocol();
		while (tmp != null) {
			tmp = tmp.getDownProtocol();
		}

		LockService lock_service = new LockService(channel);
		Lock lock = lock_service.getLock(lockName);
		return lock;
	}
}
