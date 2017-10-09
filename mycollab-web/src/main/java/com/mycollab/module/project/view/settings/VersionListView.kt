package com.mycollab.module.project.view.settings

import com.mycollab.module.tracker.domain.SimpleVersion
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface VersionListView : IListView<VersionSearchCriteria, SimpleVersion>
