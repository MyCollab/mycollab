package com.mycollab.community.module.project.view.settings;

import com.mycollab.module.project.domain.ProjectNotificationSetting;
import com.mycollab.module.project.view.settings.ProjectNotificationSettingViewComponent;
import com.mycollab.module.project.view.settings.ProjectSettingView;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ProjectSettingViewImpl extends AbstractVerticalPageView implements ProjectSettingView {
    private static final long serialVersionUID = 1L;

    private final MHorizontalLayout mainBody;

    public ProjectSettingViewImpl() {
        this.setWidth("100%");
        this.setSpacing(true);
        this.addStyleName("readview-layout");

        mainBody = new MHorizontalLayout().withMargin(true).withFullWidth();
        this.addComponent(mainBody);
    }

    @Override
    public void showNotificationSettings(ProjectNotificationSetting notification) {
        mainBody.removeAllComponents();
        ProjectNotificationSettingViewComponent component = new ProjectNotificationSettingViewComponent(notification);
        mainBody.addComponent(component);
    }
}
