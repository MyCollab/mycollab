package com.mycollab.mobile.module.crm.view.contact

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.crm.domain.SimpleContact
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
interface ContactListView : IListView<ContactSearchCriteria, SimpleContact>
