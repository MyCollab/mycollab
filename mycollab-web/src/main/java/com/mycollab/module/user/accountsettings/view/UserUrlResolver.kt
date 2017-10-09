package com.mycollab.module.user.accountsettings.view

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.user.event.UserEvent
import com.mycollab.module.user.service.UserService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class UserUrlResolver : AccountSettingUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class ListUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(UserEvent.GotoList(this, null))
    }

    private class AddUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(UserEvent.GotoAdd(this, null))
    }

    private class EditUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) {
            val username = UrlTokenizer(params[0]).getString()
            val userService = AppContextUtil.getSpringBean(UserService::class.java)
            val user = userService.findUserByUserNameInAccount(username, AppUI.accountId)
            EventBusFactory.getInstance().post(UserEvent.GotoEdit(this, user))
        }
    }

    private class PreviewUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) {
            val username = UrlTokenizer(params[0]).getString()
            EventBusFactory.getInstance().post(UserEvent.GotoRead(this, username))
        }
    }
}