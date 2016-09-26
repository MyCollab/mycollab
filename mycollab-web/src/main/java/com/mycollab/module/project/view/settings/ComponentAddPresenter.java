/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.event.BugComponentEvent;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.bug.BugComponentContainer;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class ComponentAddPresenter extends AbstractPresenter<ComponentAddView> {
    private static final long serialVersionUID = 1L;

    public ComponentAddPresenter() {
        super(ComponentAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<Component>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final Component item) {
                save(item);
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoList(this, null));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(final Component item) {
                save(item);
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoAdd(this, null));
            }
        });
    }

    private void save(Component item) {
        ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);

        SimpleProject project = CurrentProjectVariables.getProject();
        item.setSaccountid(MyCollabUI.getAccountId());
        item.setProjectid(project.getId());
        item.setStatus("Open");

        if (item.getId() == null) {
            item.setCreateduser(UserUIContext.getUsername());
            componentService.saveWithSession(item, UserUIContext.getUsername());
        } else {
            componentService.updateWithSession(item, UserUIContext.getUsername());
        }
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.COMPONENTS)) {
            BugComponentContainer componentContainer = (BugComponentContainer) container;
            componentContainer.removeAllComponents();
            componentContainer.addComponent(view);

            Component component = (Component) data.getParams();
            view.editItem(component);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);

            if (component.getId() == null) {
                breadcrumb.gotoComponentAdd();
            } else {
                breadcrumb.gotoComponentEdit(component);
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
