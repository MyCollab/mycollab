package com.mycollab.mobile.module.project.view.message

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.project.domain.SimpleMessage
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
interface MessageListView : IListView<MessageSearchCriteria, SimpleMessage>
