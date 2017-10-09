package com.mycollab.mobile.module.crm.view.cases

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CaseEvent
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.CaseWithBLOBs

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
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this,
                    CrmModuleScreenData.GotoModule(arrayOf())))
        }
    }

    class CaseAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CaseEvent.GotoAdd(this, CaseWithBLOBs()))
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