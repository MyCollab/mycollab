/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.shell.view

import com.google.common.eventbus.Subscribe
import com.mycollab.core.utils.ScalaUtils
import com.mycollab.eventmanager.ApplicationEventListener
import com.mycollab.mobile.MobileApplication
import com.mycollab.mobile.module.crm.view.CrmModulePresenter
import com.mycollab.mobile.module.project.ProjectModuleScreenData
import com.mycollab.mobile.module.project.view.ProjectModulePresenter
import com.mycollab.mobile.module.user.view.LoginPresenter
import com.mycollab.mobile.shell.events.ShellEvent
import com.mycollab.mobile.ui.IMobileView
import com.mycollab.vaadin.mvp.{AbstractController, PresenterResolver}
import com.vaadin.addon.touchkit.ui.NavigationManager
import com.vaadin.ui.{Component, UI}

/**
  * @author MyCollab Ltd
  * @since 5.2.5
  */
class ShellController(val mainNav: NavigationManager) extends AbstractController {
  bind()

  private def bind(): Unit = {
    this.register(new ApplicationEventListener[ShellEvent.GotoLoginView]() {
      @Subscribe def handle(event: ShellEvent.GotoLoginView) {
        val presenter = PresenterResolver.getPresenter(classOf[LoginPresenter])
        presenter.go(mainNav, null)
      }
    })

    this.register(new ApplicationEventListener[ShellEvent.LogOut]() {
      @Subscribe def handle(event: ShellEvent.LogOut) {
        (UI.getCurrent.asInstanceOf[MobileApplication]).redirectToLoginView()
      }
    })

    this.register(new ApplicationEventListener[ShellEvent.GotoMainPage]() {
      @Subscribe def handle(event: ShellEvent.GotoMainPage) {
        val presenter = PresenterResolver.getPresenter(classOf[MainViewPresenter])
        presenter.go(mainNav, null)
      }
    })
    this.register(new ApplicationEventListener[ShellEvent.GotoCrmModule]() {
      @Subscribe def handle(event: ShellEvent.GotoCrmModule) {
        val presenter = PresenterResolver.getPresenter(classOf[CrmModulePresenter])
        presenter.go(mainNav, null)
      }
    })
    this.register(new ApplicationEventListener[ShellEvent.GotoProjectModule]() {
      @Subscribe def handle(event: ShellEvent.GotoProjectModule) {
        val presenter = PresenterResolver.getPresenter(classOf[ProjectModulePresenter])
        val screenData = new ProjectModuleScreenData.GotoModule(ScalaUtils.stringConvertSeqToArray(event.getData))
        presenter.go(mainNav, screenData)
      }
    })

    this.register(new ApplicationEventListener[ShellEvent.PushView]() {
      @Subscribe def handle(event: ShellEvent.PushView) {
        if (event.getData.isInstanceOf[Component]) {
          if (event.getData.isInstanceOf[IMobileView]) {
            (event.getData.asInstanceOf[IMobileView]).setPreviousComponent(mainNav.getCurrentComponent)
          }
          mainNav.navigateTo(event.getData.asInstanceOf[Component])
        }
      }
    })

    this.register(new ApplicationEventListener[ShellEvent.NavigateBack]() {
      @Subscribe def handle(event: ShellEvent.NavigateBack) {
        mainNav.navigateBack()
      }
    })
  }
}
