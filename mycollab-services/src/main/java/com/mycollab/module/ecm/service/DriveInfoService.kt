package com.mycollab.module.ecm.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.ICrudService
import com.mycollab.module.ecm.domain.DriveInfo

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface DriveInfoService : ICrudService<Int, DriveInfo> {

    @CacheEvict
    fun saveOrUpdateDriveInfo(@CacheKey driveInfo: DriveInfo)

    @Cacheable
    fun getDriveInfo(@CacheKey sAccountId: Int?): DriveInfo

    @Cacheable
    fun getUsedStorageVolume(@CacheKey sAccountId: Int?): Long?
}
