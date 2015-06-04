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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectModule;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;

import java.util.Iterator;

/**
 *
 * @author MyCollab Ltd.
 * @since 4.0
 *
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class GanttChartViewPresenter extends AbstractPresenter<GanttChartView> {
    private static final long serialVersionUID = 1L;

    public GanttChartViewPresenter() {
        super(GanttChartView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.TASKS)) {
            ProjectModule prjView = getRoot(container, ProjectModule.class);
            prjView.removeAllComponents();
            prjView.addComponent(view.getWidget());
            view.displayGanttChart();

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoGanttView();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private static <T> T getRoot(Component container, Class<T> type) {
        HasComponents parent = container.getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass())) {
                return (T) parent;
            } else {
                parent = parent.getParent();
            }
        }
        return null;
    }

    private static void removeChildComponent(ComponentContainer container, Class<?> type) {
        Iterator componentsIt = container.iterator();
        while (componentsIt.hasNext()) {
            Object comp = componentsIt.next();
            if (type.isAssignableFrom(comp.getClass())) {
                container.removeComponent((Component)comp);
                return;
            }
        }
    }
}

