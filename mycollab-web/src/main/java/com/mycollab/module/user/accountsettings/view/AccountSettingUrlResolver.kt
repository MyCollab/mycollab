/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.view

import com.mycollab.module.user.accountsettings.view.event.ProfileEvent
import com.mycollab.shell.event.ShellEvent
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.vaadin.mvp.UrlResolver
import com.mycollab.vaadin.web.ui.ModuleHelper

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class AccountSettingUrlResolver : UrlResolver() {
    fun build(): UrlResolver {
        this.addSubResolver("preview", ReadUrlResolver())
        this.addSubResolver("billing", BillingUrlResolver())
        this.addSubResolver("user", UserUrlResolver())
        this.addSubResolver("role", RoleUrlResolver())
        this.addSubResolver("setting", SettingUrlResolver())
        return this
    }

    override fun handle(vararg params: String) {
        if (!ModuleHelper.isCurrentAccountModule()) {
            EventBusFactory.getInstance().post(ShellEvent.GotoUserAccountModule(this, params))
        } else {
            super.handle(*params)
        }
    }

    override fun defaultPageErrorHandler() =
            EventBusFactory.getInstance().post(ProfileEvent.GotoProfileView(this))

    private class ReadUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ProfileEvent.GotoProfileView(this))
    }
}