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
package com.esofthead.mycollab.lock;

import com.esofthead.mycollab.spring.ApplicationContextUtil;
import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class DistributionLockUtil {
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map map = Collections.synchronizedMap(new ReferenceMap(AbstractReferenceMap.WEAK, AbstractReferenceMap.WEAK));

    public static Lock getLock(String lockName) {
        try {
            DistributionLockService lockService = ApplicationContextUtil.getSpringBean(DistributionLockService.class);
            Lock lock = lockService.getLock(lockName);
            if (lock == null) {
                return getStaticDefaultLock(lockName);
            } else {
                return lock;
            }
        } catch (Exception e) {
            return getStaticDefaultLock(lockName);
        }
    }

    public static void removeLock(String lockName) {
        map.remove(lockName);
    }

    @SuppressWarnings("unchecked")
    private static Lock getStaticDefaultLock(String lockName) {
        synchronized (map) {
            Lock lock = (Lock) map.get(lockName);
            if (lock == null) {
                lock = new ReentrantLock();
                map.put(lockName, lock);
            }
            return lock;
        }
    }
}
