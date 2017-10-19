package com.mycollab.module.project.event

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class UpdateNotificationItemReadStatusEvent(val notifyUser: String, val module: String,
                                            val type: String, val typeId: String)