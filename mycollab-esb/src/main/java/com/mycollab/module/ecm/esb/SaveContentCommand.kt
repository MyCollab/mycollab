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
        val lock = DistributionLockUtil.getLock("ecm-" + event.sAccountId)
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
            DistributionLockUtil.removeLock("ecm-" + event.sAccountId)
            lock.unlock()
        }
    }
}