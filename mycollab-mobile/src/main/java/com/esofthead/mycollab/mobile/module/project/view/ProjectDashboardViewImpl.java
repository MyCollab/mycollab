package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.ui.AbstractMobileSwipeView;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */

@ViewComponent
public class ProjectDashboardViewImpl extends AbstractMobileSwipeView implements ProjectDashboardView {
    private final Label welcomeTextLbl;
    public ProjectDashboardViewImpl() {
        super();
        this.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));
        welcomeTextLbl = new Label();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        this.setContent(mainLayout);
        mainLayout.addComponent(welcomeTextLbl);
    }

    @Override
    public void displayDashboard() {
        SimpleProject currentProject = CurrentProjectVariables.getProject();
        this.welcomeTextLbl.setValue(currentProject.getName());
    }
}
