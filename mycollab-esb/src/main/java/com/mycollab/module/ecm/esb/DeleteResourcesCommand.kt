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