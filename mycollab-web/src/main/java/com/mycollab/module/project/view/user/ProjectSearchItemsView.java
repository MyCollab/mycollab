package com.mycollab.module.project.view.user;

import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public interface ProjectSearchItemsView extends PageView {
    void displayResults(String value);
}
