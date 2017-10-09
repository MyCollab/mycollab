package com.mycollab.module.crm.view.cases

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.event.CaseEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CaseUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", CaseListUrlResolver())
        this.addSubResolver("add", CaseAddUrlResolver())
        this.addSubResolver("edit", CaseEditUrlResolver())
        this.addSubResolver("preview", CasePreviewUrlResolver())
    }

    class CaseListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CaseEvent.GotoList(this, null))
    }

    class CaseAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CaseEvent.GotoAdd(this, Account()))
    }

    class CaseEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val caseId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CaseEvent.GotoEdit(this, caseId))
        }
    }

    class CasePreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val caseId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CaseEvent.GotoRead(this, caseId))
        }
    }
}