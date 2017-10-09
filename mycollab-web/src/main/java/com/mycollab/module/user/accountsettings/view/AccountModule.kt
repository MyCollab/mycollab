package com.mycollab.module.user.accountsettings.view

import com.mycollab.web.IDesktopModule

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
interface AccountModule : IDesktopModule {
    fun gotoSubView(viewName: String)

    fun gotoUserProfilePage()
}
