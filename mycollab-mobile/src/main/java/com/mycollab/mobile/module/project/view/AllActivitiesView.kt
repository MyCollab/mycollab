package com.mycollab.mobile.module.project.view

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.mobile.ui.IListView
import com.mycollab.module.project.domain.ProjectActivityStream

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
interface AllActivitiesView : IListView<ActivityStreamSearchCriteria, ProjectActivityStream>
