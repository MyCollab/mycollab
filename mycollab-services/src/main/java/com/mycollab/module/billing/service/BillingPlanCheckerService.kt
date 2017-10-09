package com.mycollab.module.billing.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.billing.UsageExceedBillingPlanException

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@IgnoreCacheClass
interface BillingPlanCheckerService : IService {
    @Throws(UsageExceedBillingPlanException::class)
    fun validateAccountCanCreateMoreProject(sAccountId: Int?)

    @Throws(UsageExceedBillingPlanException::class)
    fun validateAccountCanCreateNewUser(sAccountId: Int?)

    @Throws(UsageExceedBillingPlanException::class)
    fun validateAccountCanUploadMoreFiles(sAccountId: Int?, extraBytes: Long)
}
