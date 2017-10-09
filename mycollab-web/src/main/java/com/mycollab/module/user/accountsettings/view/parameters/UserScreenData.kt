package com.mycollab.module.user.accountsettings.view.parameters

import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.domain.User
import com.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object UserScreenData {
    class Read(params: String) : ScreenData<String>(params)

    class Add(params: SimpleUser) : ScreenData<SimpleUser>(params)

    class Edit(params: User) : ScreenData<User>(params)

    class Search(params: UserSearchCriteria) : ScreenData<UserSearchCriteria>(params)
}