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
package com.esofthead.mycollab.jgroups.service;

import java.util.concurrent.locks.Lock;

import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public class DistributionLockUtil {
	public static Lock getLock(String lockName) {
		try {
			DistributionLockService lockService = ApplicationContextUtil
					.getSpringBean(DistributionLockService.class);
			return lockService.getLock(lockName);
		} catch (Exception e) {
			return new DummyLock();
		}
	}
}
