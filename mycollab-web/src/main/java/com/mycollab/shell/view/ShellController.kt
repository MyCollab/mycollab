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
import com.mycollab.shell.event.ShellEvent
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PresenterResolver
import com.mycollab.web.DesktopApplication
import com.vaadin.ui.UI

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ShellController(container: MainWindowContainer) : AbstractController() {
    init {
        this.register(object : ApplicationEventListener<ShellEvent.GotoMainPage> {
            @Subscribe override fun handle(event: ShellEvent.GotoMainPage) {
                val mainViewPresenter = PresenterResolver.getPresenter(MainViewPresenter::class.java)
                val mainView = mainViewPresenter.getView()
                container.setContent(mainView)
                container.styleName = "mainView"
                mainViewPresenter.go(container, null)
            }
        })
        this.register(object : ApplicationEventListener<ShellEvent.LogOut> {
            @Subscribe override fun handle(event: ShellEvent.LogOut) {
                (UI.getCurrent() as DesktopApplication).redirectToLoginView()
            }
        })
        this.register(object : ApplicationEventListener<ShellEvent.GotoForgotPasswordPage> {
            @Subscribe override fun handle(event: ShellEvent.GotoForgotPasswordPage) {
                val presenter = PresenterResolver.getPresenter(ForgotPasswordPresenter::class.java)
                presenter.go(container, null)
            }
        })
    }
}