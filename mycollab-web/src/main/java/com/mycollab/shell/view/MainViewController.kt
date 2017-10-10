/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.shell.view

import com.google.common.eventbus.Subscribe
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.module.crm.view.CrmModulePresenter
import com.mycollab.module.crm.view.CrmModuleScreenData
import com.mycollab.module.file.view.IFileModulePresenter
import com.mycollab.module.file.view.parameters.FileModuleScreenData
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
        this.register(object : ApplicationEventListener<ShellEvent.GotoCrmModule> {
            @Subscribe override fun handle(event: ShellEvent.GotoCrmModule) {
                val crmModulePresenter = PresenterResolver.getPresenter(CrmModulePresenter::class.java)
                val screenData = CrmModuleScreenData.GotoModule(event.data as? Array<String>)
                crmModulePresenter.go(container, screenData)
            }
        })
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
        this.register(object : ApplicationEventListener<ShellEvent.GotoFileModule> {
            @Subscribe override fun handle(event: ShellEvent.GotoFileModule) {
                val fileModulePresenter = PresenterResolver.getPresenter(IFileModulePresenter::class.java)
                val screenData = FileModuleScreenData.GotoModule(event.data as? Array<String>)
                fileModulePresenter.go(container, screenData)
            }
        })
    }
}