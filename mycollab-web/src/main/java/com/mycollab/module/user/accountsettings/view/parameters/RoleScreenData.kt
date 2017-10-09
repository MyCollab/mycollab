package com.mycollab.module.user.accountsettings.view.parameters

import com.mycollab.module.user.domain.Role
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object RoleScreenData {
    class Read(params: Int) : ScreenData<Int>(params)

    class Add(params: Role) : ScreenData<Role>(params)

    class Edit(params: Role) : ScreenData<Role>(params)

    class Search(params: RoleSearchCriteria) : ScreenData<RoleSearchCriteria>(params)
}