package com.mycollab.module.project.view.parameters

import com.mycollab.module.tracker.domain.Version
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object VersionScreenData {
    class Read(param: Int) : ScreenData<Int>(param)

    class Add(param: Version) : ScreenData<Version>(param)

    class Edit(param: Version) : ScreenData<Version>(param)

    class Search(param: VersionSearchCriteria) : ScreenData<VersionSearchCriteria>(param)
}