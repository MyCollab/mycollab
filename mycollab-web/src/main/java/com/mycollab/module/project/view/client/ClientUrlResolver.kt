package com.mycollab.module.project.view.client

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ClientEvent
import com.mycollab.module.project.view.ProjectUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ClientUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ClientEvent.GotoList(this, null))
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val clientId = token.getInt()
            EventBusFactory.getInstance().post(ClientEvent.GotoRead(this, clientId))
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(ClientEvent.GotoAdd(this, null))
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val clientId = token.getInt()
            EventBusFactory.getInstance().post(ClientEvent.GotoEdit(this, clientId))
        }
    }

    override fun handlePage(vararg params: String) {
        EventBusFactory.getInstance().post(ClientEvent.GotoList(this, null))
    }
}