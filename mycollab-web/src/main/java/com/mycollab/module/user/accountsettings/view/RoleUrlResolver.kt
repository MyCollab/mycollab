package com.mycollab.module.user.accountsettings.view

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.user.event.RoleEvent
import com.mycollab.module.user.service.RoleService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class RoleUrlResolver : AccountSettingUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class ListUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(RoleEvent.GotoList(this, null))
    }

    private class AddUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(RoleEvent.GotoAdd(this, null))
    }

    private class EditUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) {
            val roleId = UrlTokenizer(params[0]).getInt()
            val roleService = AppContextUtil.getSpringBean(RoleService::class.java)
            val role = roleService.findById(roleId, AppUI.accountId)
            EventBusFactory.getInstance().post(RoleEvent.GotoEdit(this, role))
        }
    }

    private class PreviewUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) {
            val roleId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(RoleEvent.GotoRead(this, roleId))
        }
    }
}