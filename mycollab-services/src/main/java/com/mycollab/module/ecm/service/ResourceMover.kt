package com.mycollab.module.ecm.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.ecm.domain.Resource

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface ResourceMover : IService {
    fun moveResource(scrRes: Resource, descRes: Resource, userMove: String, sAccountId: Int?)
}
