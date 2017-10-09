package com.mycollab.module.user.accountsettings.customize.view

import com.mycollab.module.user.domain.AccountTheme
import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
interface IThemeCustomizeView : PageView {
    fun customizeTheme(accountTheme: AccountTheme)
}
