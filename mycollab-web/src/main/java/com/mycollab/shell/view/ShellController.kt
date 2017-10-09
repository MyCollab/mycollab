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
                container.setStyleName("mainView")
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