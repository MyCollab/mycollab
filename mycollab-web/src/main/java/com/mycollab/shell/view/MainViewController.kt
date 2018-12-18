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
package com.mycollab.shell.view

import com.google.common.eventbus.Subscribe
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.module.project.view.ProjectModulePresenter
import com.mycollab.module.project.view.parameters.ProjectModuleScreenData
import com.mycollab.module.user.accountsettings.view.AccountModulePresenter
import com.mycollab.module.user.accountsettings.view.parameters.AccountModuleScreenData
import com.mycollab.shell.event.ShellEvent
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PresenterResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MainViewController(val container: MainView) : AbstractController() {
    init {
        this.register(object : ApplicationEventListener<ShellEvent.GotoProjectModule> {
            @Subscribe override fun handle(event: ShellEvent.GotoProjectModule) {
                val prjPresenter = PresenterResolver.getPresenter(ProjectModulePresenter::class.java)
                val screenData = ProjectModuleScreenData.GotoModule(event.data as? Array<String>)
                prjPresenter.go(container, screenData)
            }
        })
        this.register(object : ApplicationEventListener<ShellEvent.GotoUserAccountModule> {
            @Subscribe override fun handle(event: ShellEvent.GotoUserAccountModule) {
                val presenter = PresenterResolver.getPresenter(AccountModulePresenter::class.java)
                presenter.go(container, AccountModuleScreenData.GotoModule(event.data as? Array<String>))
            }
        })
    }
}