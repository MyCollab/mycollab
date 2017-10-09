package com.mycollab.module.project.view.parameters

import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ClientScreenData {
    class Read(params: Int) : ScreenData<Int>(params)

    class Add(param: Account) : ScreenData<Account>(param)

    class Search(param: AccountSearchCriteria) : ScreenData<AccountSearchCriteria>(param)
}