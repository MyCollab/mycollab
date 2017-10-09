package com.mycollab.mobile.module.project.view.milestone

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import com.mycollab.module.project.i18n.OptionI18nEnum

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
interface MilestoneListView : IListView<MilestoneSearchCriteria, SimpleMilestone> {

    fun displayStatus(status: OptionI18nEnum.MilestoneStatus)
}
