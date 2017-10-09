package com.mycollab.module.ecm.esb

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DeleteResourcesEvent(val paths: Array<String>, val userDelete: String, val isUpdateDriveInfo: Boolean, val sAccountId: Int?)