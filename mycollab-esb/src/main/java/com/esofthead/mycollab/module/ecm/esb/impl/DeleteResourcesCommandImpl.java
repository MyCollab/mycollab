/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.ecm.esb.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.lock.DistributionLockUtil;
import com.esofthead.mycollab.module.ecm.domain.DriveInfo;
import com.esofthead.mycollab.module.ecm.esb.DeleteResourcesCommand;
import com.esofthead.mycollab.module.ecm.service.DriveInfoService;
import com.esofthead.mycollab.module.file.service.RawContentService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class DeleteResourcesCommandImpl implements DeleteResourcesCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(DeleteResourcesCommandImpl.class);

	@Autowired
	private RawContentService rawContentService;

	@Autowired
	private DriveInfoService driveInfoService;

	@Override
	public void removeResource(String[] paths, String userDelete,
			Integer sAccountId) {

		Lock lock = DistributionLockUtil.getLock("ecm-" + sAccountId);
		if (sAccountId == null) {
			return;
		}
		try {
			if (lock.tryLock(1, TimeUnit.HOURS)) {
				long totalSize = 0;
				DriveInfo driveInfo = driveInfoService.getDriveInfo(sAccountId);

				for (String path : paths) {
					if (StringUtils.isBlank(path)) {
						continue;
					}
					totalSize += rawContentService.getSize(path);
					rawContentService.removePath(path);
				}

				if (driveInfo.getUsedvolume() == null
						|| (driveInfo.getUsedvolume() < totalSize)) {
					LOG.error(
							"Inconsistent storage volumne site of account {}, used storage is less than removed storage ",
							sAccountId);
					driveInfo.setUsedvolume(0L);
				} else {
					driveInfo.setUsedvolume(driveInfo.getUsedvolume()
							- totalSize);
				}

				driveInfoService.saveOrUpdateDriveInfo(driveInfo);
			}
		} catch (Exception e) {
			LOG.error("Error while delete content " + paths, e);
		} finally {
			lock.unlock();
		}
	}
}
