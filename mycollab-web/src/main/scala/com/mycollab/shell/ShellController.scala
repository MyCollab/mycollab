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
package com.mycollab.shell

import com.mycollab.eventmanager.ApplicationEventListener
import com.mycollab.shell.events.ShellEvent
import com.mycollab.shell.view.{ForgotPasswordPresenter, MainViewPresenter, MainWindowContainer}
import com.mycollab.vaadin.mvp.{AbstractController, PresenterResolver}
import com.mycollab.web.DesktopApplication
import com.google.common.eventbus.Subscribe
import com.vaadin.ui.UI

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class ShellController(container: MainWindowContainer) extends AbstractController {
  bind()

  private def bind() {
    this.register(new ApplicationEventListener[ShellEvent.GotoMainPage]() {
      @Subscribe def handle(event: ShellEvent.GotoMainPage) {
        val mainViewPresenter = PresenterResolver.getPresenter(classOf[MainViewPresenter])
        val mainView = mainViewPresenter.getView
        container.setContent(mainView)
        container.setStyleName("mainView")
        mainViewPresenter.go(container, null)
      }
    })
    this.register(new ApplicationEventListener[ShellEvent.LogOut]() {
      @Subscribe def handle(event: ShellEvent.LogOut) {
        (UI.getCurrent.asInstanceOf[DesktopApplication]).redirectToLoginView()
      }
    })
    this.register(new ApplicationEventListener[ShellEvent.GotoForgotPasswordPage]() {
      @Subscribe def handle(event: ShellEvent.GotoForgotPasswordPage) {
        val presenter = PresenterResolver.getPresenter(classOf[ForgotPasswordPresenter])
        presenter.go(container, null)
      }
    })
  }
}
