package com.mycollab.vaadin.mvp

import com.mycollab.db.arguments.SearchCriteria

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class ScreenData<out P>(val params: P?) {

    class Add<out P>(params: P?) : ScreenData<P>(params)

    class Edit<out P>(params: P?) : ScreenData<P>(params)

    class Preview<out P>(params: P?) : ScreenData<P>(params)

    class Search<out P : SearchCriteria>(params: P?) : ScreenData<P>(params)
} 