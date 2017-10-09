package com.mycollab.mobile.module.project.view;

import com.mycollab.common.GenericLinkUtils;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class ProjectActivitiesViewImpl extends AbstractListPageView<ActivityStreamSearchCriteria, ProjectActivityStream> implements ProjectActivitiesView {
    private static final long serialVersionUID = 6930154745425180819L;

    public ProjectActivitiesViewImpl() {
        this.setCaption(UserUIContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES));
    }

    @Override
    protected AbstractPagedBeanList<ActivityStreamSearchCriteria, ProjectActivityStream> createBeanList() {
        return new ProjectActivitiesStreamListDisplay();
    }

    @Override
    protected SearchInputField<ActivityStreamSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected Component buildRightComponent() {
        return null;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment("project/activities/" + GenericLinkUtils.encodeParam(CurrentProjectVariables.getProjectId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES));
    }
}
