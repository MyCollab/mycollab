package com.mycollab.mobile.shell.view

import com.google.common.eventbus.Subscribe
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.mobile.MobileApplication
import com.mycollab.mobile.module.crm.view.CrmModulePresenter
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.project.view.ProjectModulePresenter
import com.mycollab.mobile.module.project.view.parameters.ProjectModuleScreenData
import com.mycollab.mobile.module.user.view.LoginPresenter
import com.mycollab.mobile.shell.event.ShellEvent
import com.mycollab.mobile.ui.IMobileView
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PresenterResolver
import com.vaadin.addon.touchkit.ui.NavigationManager
import com.vaadin.ui.Component
import com.vaadin.ui.UI

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ShellController(val mainNav: NavigationManager) : AbstractController() {
    init {
        this.register(object : ApplicationEventListener<ShellEvent.GotoLoginView> {
            @Subscribe override fun handle(event: ShellEvent.GotoLoginView) {
                val presenter = PresenterResolver.getPresenter(LoginPresenter::class.java)
                presenter.go(mainNav, null)
            }
        })

        this.register(object : ApplicationEventListener<ShellEvent.LogOut> {
            @Subscribe override fun handle(event: ShellEvent.LogOut) {
                (UI.getCurrent() as MobileApplication).redirectToLoginView()
            }
        })

        this.register(object : ApplicationEventListener<ShellEvent.GotoMainPage> {
            @Subscribe override fun handle(event: ShellEvent.GotoMainPage) {
                val presenter = PresenterResolver.getPresenter(MainViewPresenter::class.java)
                presenter.go(mainNav, null)
            }
        })
        this.register(object : ApplicationEventListener<ShellEvent.GotoCrmModule> {
            @Subscribe override fun handle(event: ShellEvent.GotoCrmModule) {
                val presenter = PresenterResolver.getPresenter(CrmModulePresenter::class.java)
                val screenData = CrmModuleScreenData.GotoModule(event.data as? Array<String>)
                presenter.go(mainNav, screenData)
            }
        })
        this.register(object : ApplicationEventListener<ShellEvent.GotoProjectModule> {
            @Subscribe override fun handle(event: ShellEvent.GotoProjectModule) {
                val presenter = PresenterResolver.getPresenter(ProjectModulePresenter::class.java)
                val screenData = ProjectModuleScreenData.GotoModule(event.data as? Array<String>)
                presenter.go(mainNav, screenData)
            }
        })

        this.register(object : ApplicationEventListener<ShellEvent.PushView> {
            @Subscribe override fun handle(event: ShellEvent.PushView) {
                if (event.data is Component) {
                    if (event.data is IMobileView) {
                        (event.data as IMobileView).setPreviousComponent(mainNav.currentComponent)
                    }
                    mainNav.navigateTo(event.data)
                }
            }
        })

        this.register(object : ApplicationEventListener<ShellEvent.NavigateBack> {
            @Subscribe override fun handle(event: ShellEvent.NavigateBack) {
                mainNav.navigateBack()
            }
        })
    }
}