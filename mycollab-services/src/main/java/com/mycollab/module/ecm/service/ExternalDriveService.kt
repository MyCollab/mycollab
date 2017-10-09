package com.mycollab.module.ecm.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.ICrudService
import com.mycollab.module.ecm.domain.ExternalDrive

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface ExternalDriveService : ICrudService<Int, ExternalDrive> {

    fun getExternalDrivesOfUser(username: String): List<ExternalDrive>
}
