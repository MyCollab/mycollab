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