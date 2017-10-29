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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.service.impl

import com.mycollab.core.cache.CacheKey
import com.mycollab.core.utils.BeanUtility
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.concurrent.DistributionLockUtil
import com.mycollab.module.ecm.dao.DriveInfoMapper
import com.mycollab.module.ecm.domain.DriveInfo
import com.mycollab.module.ecm.domain.DriveInfoExample
import com.mycollab.module.ecm.service.DriveInfoService
import org.apache.commons.collections.CollectionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

@Service
class DriveInfoServiceImpl(private val driveInfoMapper: DriveInfoMapper) : DefaultCrudService<Int, DriveInfo>(), DriveInfoService {

    override val crudMapper: ICrudGenericDAO<Int, DriveInfo>
        get() = driveInfoMapper as ICrudGenericDAO<Int, DriveInfo>

    override fun saveOrUpdateDriveInfo(@CacheKey driveInfo: DriveInfo) {
        val sAccountId = driveInfo.saccountid
        val ex = DriveInfoExample()
        ex.createCriteria().andSaccountidEqualTo(sAccountId)
        val lock = DistributionLockUtil.getLock("ecm-service$sAccountId")
        try {
            if (lock.tryLock(15, TimeUnit.SECONDS)) {
                if (driveInfoMapper.countByExample(ex) > 0) {
                    driveInfo.id = null
                    driveInfoMapper.updateByExampleSelective(driveInfo, ex)
                } else {
                    driveInfoMapper.insert(driveInfo)
                }
            }
        } catch (e: Exception) {
            LOG.error("Error while save drive info ${BeanUtility.printBeanObj(driveInfo)}", e)
        } finally {
            DistributionLockUtil.removeLock("ecm-service$sAccountId")
            lock.unlock()
        }
    }

    override fun getDriveInfo(@CacheKey sAccountId: Int?): DriveInfo {
        val ex = DriveInfoExample()
        ex.createCriteria().andSaccountidEqualTo(sAccountId)
        val driveInfos = driveInfoMapper.selectByExample(ex)
        if (CollectionUtils.isNotEmpty(driveInfos)) {
            return driveInfos[0]
        } else {
            val driveInfo = DriveInfo()
            driveInfo.usedvolume = 0L
            driveInfo.saccountid = sAccountId
            return driveInfo
        }
    }

    override fun getUsedStorageVolume(@CacheKey sAccountId: Int?): Long? {
        val driveInfo = getDriveInfo(sAccountId)
        return if (driveInfo.usedvolume == null) java.lang.Long.valueOf(0L) else driveInfo.usedvolume
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DriveInfoServiceImpl::class.java)
    }
}
