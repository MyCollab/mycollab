package com.mycollab.mobile.module.crm.view.account

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.crm.domain.SimpleAccount
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
interface AccountListView : IListView<AccountSearchCriteria, SimpleAccount>
