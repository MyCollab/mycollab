package com.mycollab.community.module.billing.service.impl

import com.mycollab.module.billing.UsageExceedBillingPlanException
import com.mycollab.module.billing.service.BillingPlanCheckerService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@Service
class BillingPlanCheckerServiceImpl : BillingPlanCheckerService {

    @Throws(UsageExceedBillingPlanException::class)
    override fun validateAccountCanCreateMoreProject(sAccountId: Int?) {

    }

    @Throws(UsageExceedBillingPlanException::class)
    override fun validateAccountCanCreateNewUser(sAccountId: Int?) {
    }

    @Throws(UsageExceedBillingPlanException::class)
    override fun validateAccountCanUploadMoreFiles(sAccountId: Int?, extraBytes: Long) {

    }

}
