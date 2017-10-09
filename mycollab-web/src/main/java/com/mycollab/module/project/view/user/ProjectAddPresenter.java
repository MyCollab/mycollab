package com.mycollab.module.project.view.user;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.ui.MyCollabSession;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.CREATE_NEW_PROJECT, impliedPermissionVal = BooleanPermissionFlag.TRUE)
public class ProjectAddPresenter extends AbstractPresenter<ProjectAddView> {
    private static final long serialVersionUID = 1L;

    public ProjectAddPresenter() {
        super(ProjectAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<Project>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final Project project) {
                saveProject(project);
                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this,
                        new PageActionChain(new ProjectScreenData.Goto(view.getItem().getId()))));
            }

            @Override
            public void onCancel() {
                if (view.getItem().getId() == null) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(ProjectAddPresenter.this, null));
                } else {
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this,
                            new PageActionChain(new ProjectScreenData.Goto(view.getItem().getId()))));
                }
            }

            @Override
            public void onSaveAndNew(final Project project) {

            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectDashboardContainer projectViewContainer = (ProjectDashboardContainer) container;
        projectViewContainer.setContent(view);

        Project project = (Project) data.getParams();
        view.editItem(project);

        if (project.getId() == null) {
            AppUI.addFragment("project/add", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                    UserUIContext.getMessage(ProjectI18nEnum.SINGLE)));
        }
    }

    private void saveProject(Project project) {
        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        project.setSaccountid(AppUI.getAccountId());

        if (project.getId() == null) {
            projectService.saveWithSession(project, UserUIContext.getUsername());
        } else {
            projectService.updateWithSession(project, UserUIContext.getUsername());
            MyCollabSession.putCurrentUIVariable(MyCollabSession.CURRENT_PROJECT, project);
        }
    }
}
