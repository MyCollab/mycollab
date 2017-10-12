/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugComponentEvent;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.vaadin.reporting.FormReportLayout;
import com.mycollab.vaadin.reporting.PrintButton;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class ComponentReadPresenter extends AbstractPresenter<ComponentReadView> {

    private static final long serialVersionUID = 1L;

    public ComponentReadPresenter() {
        super(ComponentReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleComponent>() {
            @Override
            public void onEdit(SimpleComponent data) {
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleComponent data) {
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(SimpleComponent data) {
                ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
                componentService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoList(this, null));
            }

            @Override
            public void onClone(SimpleComponent data) {
                Component cloneData = (Component) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoList(this, null));
            }

            @Override
            public void onPrint(Object source, SimpleComponent data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(ProjectTypeConstants.BUG_COMPONENT, Component.Field.name.name(),
                        ComponentDefaultFormLayoutFactory.getForm(), Component.Field.id.name()));
            }

            @Override
            public void gotoNext(SimpleComponent data) {
                ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
                ComponentSearchCriteria criteria = new ComponentSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                Integer nextId = componentService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleComponent data) {
                ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
                ComponentSearchCriteria criteria = new ComponentSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN));
                Integer nextId = componentService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.COMPONENTS)) {
            if (data.getParams() instanceof Integer) {
                ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
                SimpleComponent component = componentService.findById((Integer) data.getParams(), AppUI.getAccountId());
                if (component != null) {
                    ProjectComponentContainer componentContainer = (ProjectComponentContainer) container;
                    componentContainer.removeAllComponents();
                    componentContainer.addComponent(view);
                    view.previewItem(component);

                    ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                    breadcrumb.gotoComponentRead(component);
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            } else {
                throw new MyCollabException("Unhandle this case yet");
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}