package com.mycollab.module.crm.view.opportunity

import com.mycollab.module.crm.domain.SimpleOpportunity
import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
interface ContactRoleEditView : PageView {
    fun display(opportunity: SimpleOpportunity)
}
