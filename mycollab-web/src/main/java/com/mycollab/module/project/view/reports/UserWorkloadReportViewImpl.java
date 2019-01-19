package com.mycollab.module.project.view.reports;

import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
@ViewComponent
public class UserWorkloadReportViewImpl extends AbstractVerticalPageView implements UserWorkloadReportView {
    @Override
    public void display() {
        removeAllComponents();

    }
}
