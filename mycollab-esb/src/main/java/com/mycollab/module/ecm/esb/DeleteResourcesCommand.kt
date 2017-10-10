/**
 * mycollab-esb - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.concurrent.DistributionLockUtil
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.ecm.service.DriveInfoService
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.file.service.RawContentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteResourcesCommand(private val rawContentService: RawContentService,
                             private val driveInfoService: DriveInfoService)  : GenericCommand() {

    companion object {
        val LOG = LoggerFactory.getLogger(DeleteResourcesCommand::class.java)
    }

    @AllowConcurrentEvents
    @Subscribe
    fun removeResource(event: DeleteResourcesEvent) {
        if (event.sAccountId == null) {
            event.paths.forEach {
                if (StringUtils.isNotBlank(it)) {
                    rawContentService.removePath(it)
                }
            }
            return
        }
        if (event.isUpdateDriveInfo) {
            val lock = DistributionLockUtil.getLock("ecm-" + event.sAccountId)
            try {
                if (lock.tryLock(1, TimeUnit.HOURS)) {
                    var totalSize = 0L
                    val driveInfo = driveInfoService.getDriveInfo(event.sAccountId)
                   event.paths.forEach {
                       if (StringUtils.isNotBlank(it)) {
                           totalSize += rawContentService.getSize(it)
                           rawContentService.removePath(it)
                       }
                   }
                    if (driveInfo.usedvolume == null || (driveInfo.usedvolume < totalSize)) {
                        driveInfo.usedvolume = 0L
                    } else {
                        driveInfo.usedvolume = driveInfo.usedvolume - totalSize
                    }
                    driveInfoService.saveOrUpdateDriveInfo(driveInfo)
                }
            }
            catch(e:Exception) {
                LOG.error("Error while delete content ${event.paths}", e)
            } finally {
                DistributionLockUtil.removeLock("ecm-" + event.sAccountId)
                lock.unlock()
            }
        }
    }
}