package com.mycollab.mobile.module.project.view;

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class AllActivitiesStreamPresenter extends ProjectListPresenter<AllActivitiesView, ActivityStreamSearchCriteria, ProjectActivityStream> {
    private static final long serialVersionUID = -2089284900326846089L;

    public AllActivitiesStreamPresenter() {
        super(AllActivitiesView.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        super.onGo(navigator, data);
    }
}
