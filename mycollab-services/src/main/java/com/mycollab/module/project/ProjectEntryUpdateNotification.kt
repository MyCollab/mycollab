package com.mycollab.module.project

import com.mycollab.core.AbstractNotification

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ProjectEntryUpdateNotification(val type:String, val typeId:String, val message: String) : AbstractNotification(AbstractNotification.SCOPE_USER,AbstractNotification.WARNING)
