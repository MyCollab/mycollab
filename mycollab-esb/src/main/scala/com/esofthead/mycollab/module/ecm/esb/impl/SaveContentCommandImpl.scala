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
package com.esofthead.mycollab.module.ecm.esb.impl

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.esofthead.mycollab.core.utils.BeanUtility
import com.esofthead.mycollab.lock.DistributionLockUtil
import com.esofthead.mycollab.module.ecm.domain.Content
import com.esofthead.mycollab.module.ecm.domain.DriveInfo
import com.esofthead.mycollab.module.ecm.esb.SaveContentCommand
import com.esofthead.mycollab.module.ecm.service.DriveInfoService
import com.esofthead.mycollab.module.file.service.RawContentService

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@Component("saveContentCommand") object SaveContentCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[SaveContentCommandImpl])
}

@Component("saveContentCommand") class SaveContentCommandImpl extends SaveContentCommand {
    @Autowired private val driveInfoService: DriveInfoService = null
    @Autowired private val rawContentService: RawContentService = null

    def saveContent(content: Content, createdUser: String, sAccountId: Integer) {
        SaveContentCommandImpl.LOG.debug("Save content {} by {}", Array(BeanUtility.printBeanObj(content), createdUser))
        if (sAccountId == null) {
            return
        }
        val lock: Lock = DistributionLockUtil.getLock("ecm-" + sAccountId)
        var totalSize: Long = content.getSize
        if (StringUtils.isNotBlank(content.getThumbnail)) {
            totalSize += rawContentService.getSize(content.getThumbnail)
        }
        try {
            if (lock.tryLock(1, TimeUnit.HOURS)) {
                val driveInfo: DriveInfo = driveInfoService.getDriveInfo(sAccountId)
                if (driveInfo.getUsedvolume == null) {
                    driveInfo.setUsedvolume(totalSize)
                }
                else {
                    driveInfo.setUsedvolume(totalSize + driveInfo.getUsedvolume)
                }
                driveInfoService.saveOrUpdateDriveInfo(driveInfo)
            }
        }
        catch {
            case e: Exception => {
                SaveContentCommandImpl.LOG.error(String.format("Error while save content %s", BeanUtility.printBeanObj(content)), e)
            }
        } finally {
            lock.unlock
        }
    }
}