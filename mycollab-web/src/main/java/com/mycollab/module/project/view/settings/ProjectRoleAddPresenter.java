package com.mycollab.module.project.view.settings;

import com.mycollab.core.SecureAccessException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.ProjectRole;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.event.ProjectRoleEvent;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleAddPresenter extends AbstractPresenter<ProjectRoleAddView> {
    private static final long serialVersionUID = 1L;

    public ProjectRoleAddPresenter() {
        super(ProjectRoleAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<ProjectRole>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final ProjectRole item) {
                save(item);
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(ProjectRole item) {
                save(item);
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, null));
            }
        });
    }

    public void save(ProjectRole item) {
        ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
        item.setSaccountid(AppUI.getAccountId());

        SimpleProject project = CurrentProjectVariables.getProject();
        item.setProjectid(project.getId());

        if (item.getId() == null) {
            roleService.saveWithSession(item, UserUIContext.getUsername());
        } else {
            roleService.updateWithSession(item, UserUIContext.getUsername());
        }

        roleService.savePermission(project.getId(), item.getId(), view.getPermissionMap(), AppUI.getAccountId());

    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.ROLES)) {
            ProjectRoleContainer roleContainer = (ProjectRoleContainer) container;
            roleContainer.removeAllComponents();
            roleContainer.addComponent(view);

            ProjectRole role = (ProjectRole) data.getParams();

            view.editItem(role);
            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (role.getId() == null) {
                breadcrumb.gotoRoleAdd();
            } else {
                breadcrumb.gotoRoleEdit(role);
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
