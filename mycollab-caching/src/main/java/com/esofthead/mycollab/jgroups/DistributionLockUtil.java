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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
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

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class DistributionLockUtil {
	private static ForkChannel getChannel() {
		CacheImpl<Object, Object> cache = (CacheImpl<Object, Object>) LocalCacheManager
				.getCache();
		Transport tp;
		ForkChannel fork_ch;
		try {
			tp = cache.getAdvancedCache().getRpcManager().getTransport();
		} catch (Exception e) {
			return null;
		}

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
		if (channel != null) {
			ProtocolStack protocolStack = channel.getProtocolStack();
			Protocol tmp = protocolStack.getDownProtocol();
			while (tmp != null) {
				tmp = tmp.getDownProtocol();
			}

			LockService lock_service = new LockService(channel);
			Lock lock = lock_service.getLock(lockName);
			return lock;
		} else {
			return new DummyLock();
		}
	}

	private static class DummyLock implements Lock {

		@Override
		public void lock() {
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {

		}

		@Override
		public boolean tryLock() {
			return false;
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit)
				throws InterruptedException {
			return false;
		}

		@Override
		public void unlock() {

		}

		@Override
		public Condition newCondition() {
			return null;
		}

	}
}
