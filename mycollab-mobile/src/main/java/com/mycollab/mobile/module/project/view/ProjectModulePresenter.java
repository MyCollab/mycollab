package com.mycollab.mobile.module.project.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.mobile.module.project.event.ProjectEvent;
import com.mycollab.mobile.shell.ModuleHelper;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class ProjectModulePresenter extends AbstractProjectPresenter<ProjectModule> {
    private static final long serialVersionUID = 6940806138148601147L;

    public ProjectModulePresenter() {
        super(ProjectModule.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        ModuleHelper.setCurrentModule(getView());
        UserUIContext.updateLastModuleVisit(ModuleNameConstants.PRJ);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
            AppUI.addFragment("project", UserUIContext.getMessage(ProjectI18nEnum.SINGLE));
        } else {
            MobileApplication.rootUrlResolver.getSubResolver("project").handle(params);
        }
    }
}
