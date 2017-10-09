package com.mycollab.module.crm.view.account

import com.mycollab.module.crm.domain.SimpleAccount
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface AccountListView : IListView<AccountSearchCriteria, SimpleAccount>
