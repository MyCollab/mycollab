package com.mycollab.mobile.module.project.view;

import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class UserProjectListPresenter extends ProjectListPresenter<UserProjectListView, ProjectSearchCriteria, SimpleProject> {
    private static final long serialVersionUID = 35574182873793474L;

    public UserProjectListPresenter() {
        super(UserProjectListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        super.onGo(container, data);
    }
}
