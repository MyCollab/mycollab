/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.shell.view

import com.esofthead.mycollab.core.utils.ScalaUtils
import com.esofthead.mycollab.eventmanager.ApplicationEventListener
import com.esofthead.mycollab.module.crm.view.{CrmModuleScreenData, CrmModulePresenter}
import com.esofthead.mycollab.module.file.view.{FileModuleScreenData, IFileModulePresenter}
import com.esofthead.mycollab.module.project.view.ProjectModulePresenter
import com.esofthead.mycollab.module.project.view.parameters.ProjectModuleScreenData
import com.esofthead.mycollab.module.user.accountsettings.view.{AccountModuleScreenData, AccountModulePresenter}
import com.esofthead.mycollab.shell.events.ShellEvent
import com.esofthead.mycollab.vaadin.mvp.{PresenterResolver, AbstractController}
import com.google.common.eventbus.Subscribe

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class MainViewController(val container: MainView) extends AbstractController {
    bind()

    private def bind(): Unit = {
        this.register(new ApplicationEventListener[ShellEvent.GotoCrmModule]() {
            @Subscribe def handle(event: ShellEvent.GotoCrmModule) {
                val crmModulePresenter: CrmModulePresenter = PresenterResolver.getPresenter(classOf[CrmModulePresenter])
                val screenData: CrmModuleScreenData.GotoModule = new CrmModuleScreenData.GotoModule(ScalaUtils.stringConvertSeqToArray(event.getData))
                crmModulePresenter.go(container, screenData)
            }
        })
        this.register(new ApplicationEventListener[ShellEvent.GotoProjectModule]() {
            @Subscribe def handle(event: ShellEvent.GotoProjectModule) {
                val prjPresenter: ProjectModulePresenter = PresenterResolver.getPresenter(classOf[ProjectModulePresenter])
                val screenData: ProjectModuleScreenData.GotoModule = new ProjectModuleScreenData.GotoModule (ScalaUtils.stringConvertSeqToArray(event.getData))
                prjPresenter.go(container, screenData)
            }
        })
        this.register(new ApplicationEventListener[ShellEvent.GotoUserAccountModule]() {
            @Subscribe def handle(event: ShellEvent.GotoUserAccountModule) {
                val presenter: AccountModulePresenter = PresenterResolver.getPresenter(classOf[AccountModulePresenter])
                presenter.go(container, new AccountModuleScreenData.GotoModule(ScalaUtils.stringConvertSeqToArray(event.getData)))
            }
        })
        this.register(new ApplicationEventListener[ShellEvent.GotoFileModule]() {
            @Subscribe def handle(event: ShellEvent.GotoFileModule) {
                val fileModulePresenter: IFileModulePresenter = PresenterResolver.getPresenter(classOf[IFileModulePresenter])
                val screenData: FileModuleScreenData.GotoModule = new FileModuleScreenData.GotoModule(ScalaUtils.stringConvertSeqToArray(event.getData))
                fileModulePresenter.go(container, screenData)
            }
        })
    }
}
