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

import com.esofthead.mycollab.core.utils.{BeanUtility, StringUtils}
import com.esofthead.mycollab.lock.DistributionLockUtil
import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.ecm.domain.DriveInfo
import com.esofthead.mycollab.module.ecm.esb.SaveContentEvent
import com.esofthead.mycollab.module.ecm.service.DriveInfoService
import com.esofthead.mycollab.module.file.service.RawContentService
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd.
  * @since 1.0
  */
object SaveContentCommandImpl {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[SaveContentCommandImpl])
}

@Component("saveContentCommand") class SaveContentCommandImpl extends GenericCommand {
  @Autowired private val driveInfoService: DriveInfoService = null
  @Autowired private val rawContentService: RawContentService = null

  @AllowConcurrentEvents
  @Subscribe
  def saveContent(event: SaveContentEvent): Unit = {
    SaveContentCommandImpl.LOG.debug("Save content {} by {}", Array(BeanUtility.printBeanObj(event.content),
      event.createdUser))
    if (event.sAccountId == null) {
      return
    }
    val lock = DistributionLockUtil.getLock("ecm-" + event.sAccountId)
    var totalSize = event.content.getSize
    if (StringUtils.isNotBlank(event.content.getThumbnail)) {
      totalSize += rawContentService.getSize(event.content.getThumbnail)
    }
    try {
      if (lock.tryLock(1, TimeUnit.HOURS)) {
        val driveInfo = driveInfoService.getDriveInfo(event.sAccountId)
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
      case e: Exception => SaveContentCommandImpl.LOG.error(String.format("Error while save content %s",
        BeanUtility.printBeanObj(event.content)), e)
    } finally {
      DistributionLockUtil.removeLock("ecm-" + event.sAccountId)
      lock.unlock
    }
  }
}