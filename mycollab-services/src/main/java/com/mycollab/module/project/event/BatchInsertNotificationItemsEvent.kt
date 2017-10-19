package com.mycollab.module.project.event

import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class BatchInsertNotificationItemsEvent(val notifyUsers: List<String>, val module: String,
                                        val type: String, val typeId: String, val messages: List<String>,
                                        val sAccountId: Int)