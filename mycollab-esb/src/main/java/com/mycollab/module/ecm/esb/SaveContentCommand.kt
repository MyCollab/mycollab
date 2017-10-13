/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
class SaveContentCommand(private val driveInfoService: DriveInfoService,
                         private val rawContentService: RawContentService) : GenericCommand() {
    companion object {
        val LOG = LoggerFactory.getLogger(SaveContentCommand::class.java)
    }

    @AllowConcurrentEvents
    @Subscribe
    fun saveContent(event: SaveContentEvent) {
        LOG.debug("Save content ${event.content} by ${event.createdUser}")
        if (event.sAccountId == null) {
            return
        }
        val lock = DistributionLockUtil.getLock("ecm-${event.sAccountId}")
        var totalSize = event.content.size
        if (StringUtils.isNotBlank(event.content.thumbnail)) {
            totalSize += rawContentService.getSize(event.content.thumbnail)
        }
        try {
            if (lock.tryLock(1, TimeUnit.HOURS)) {
                val driveInfo = driveInfoService.getDriveInfo(event.sAccountId)
                if (driveInfo.usedvolume == null) {
                    driveInfo.usedvolume = totalSize
                } else {
                    driveInfo.usedvolume = totalSize + driveInfo.usedvolume
                }
                driveInfoService.saveOrUpdateDriveInfo(driveInfo)
            }
        } catch (e: Exception) {
            LOG.error("Error while save content ${event.content}", e)
        } finally {
            DistributionLockUtil.removeLock("ecm-${event.sAccountId}")
            lock.unlock()
        }
    }
}