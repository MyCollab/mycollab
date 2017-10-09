package com.mycollab.module.user.accountsettings.team.view

import com.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface UserListView : PageView {
    fun setSearchCriteria(searchCriteria: UserSearchCriteria)
}
