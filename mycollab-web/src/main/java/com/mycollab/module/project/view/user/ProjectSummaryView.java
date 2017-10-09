package com.mycollab.module.project.view.user;

import com.mycollab.vaadin.mvp.LazyPageView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectSummaryView extends LazyPageView {
    void displaySearchResult(String value);
}
