package com.mycollab.module.project.view

import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria
import com.mycollab.vaadin.web.ui.IListView
import com.mycollab.vaadin.web.ui.InitializingView

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
interface ProjectListView : IListView<ProjectSearchCriteria, SimpleProject>, InitializingView
