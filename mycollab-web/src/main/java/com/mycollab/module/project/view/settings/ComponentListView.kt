package com.mycollab.module.project.view.settings

import com.mycollab.module.tracker.domain.SimpleComponent
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ComponentListView : IListView<ComponentSearchCriteria, SimpleComponent>
