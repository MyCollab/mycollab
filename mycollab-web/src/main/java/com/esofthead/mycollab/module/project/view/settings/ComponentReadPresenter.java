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

package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

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
                ComponentService componentService = ApplicationContextUtil.getSpringBean(ComponentService.class);
                componentService.removeWithSession(data, AppContext.getUsername(), AppContext.getAccountId());
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
            public void gotoNext(SimpleComponent data) {
                ComponentService componentService = ApplicationContextUtil.getSpringBean(ComponentService.class);
                ComponentSearchCriteria criteria = new ComponentSearchCriteria();
                criteria.setProjectid(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));
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
                ComponentService componentService = ApplicationContextUtil.getSpringBean(ComponentService.class);
                ComponentSearchCriteria criteria = new ComponentSearchCriteria();
                criteria.setProjectid(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
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
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.COMPONENTS)) {
            if (data.getParams() instanceof Integer) {
                ComponentService componentService = ApplicationContextUtil.getSpringBean(ComponentService.class);
                SimpleComponent component = componentService.findById((Integer) data.getParams(), AppContext.getAccountId());
                if (component != null) {
                    ComponentContainer componentContainer = container;
                    componentContainer.removeAllComponents();
                    componentContainer.addComponent(view.getWidget());
                    view.previewItem(component);

                    ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                    breadcrumb.gotoComponentRead(component);
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            } else {
                throw new MyCollabException("Unhanddle this case yet");
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}