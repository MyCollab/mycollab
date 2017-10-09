package com.mycollab.module.billing.esb

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class AccountCreatedEvent(val accountId: Int, val initialUser: String, val createSampleData: Boolean)