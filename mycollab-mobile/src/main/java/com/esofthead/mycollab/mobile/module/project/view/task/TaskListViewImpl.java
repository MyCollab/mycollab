/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.module.project.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.PreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */

@ViewComponent
public class TaskListViewImpl extends AbstractListViewComp<TaskSearchCriteria, SimpleTask> implements TaskListView {
    private static final long serialVersionUID = -3705209608075399509L;

    private SimpleTaskList currentTaskList;
    private Set<PreviewFormHandler<SimpleTaskList>> handlers;

    public TaskListViewImpl() {
        this.addStyleName("task-list-view");
        this.setToggleButton(false);
    }

    @Override
    protected AbstractPagedBeanList<TaskSearchCriteria, SimpleTask> createBeanTable() {
        return new TaskListDisplay();
    }

    @Override
    protected Component createRightComponent() {
        NavigationBarQuickMenu editBtn = new NavigationBarQuickMenu();
        editBtn.setButtonCaption(null);
        editBtn.setStyleName("edit-btn");

        ProjectPreviewFormControlsGenerator<SimpleTaskList> controlsGenerator = new ProjectPreviewFormControlsGenerator<>(this);
        VerticalLayout menuContent = controlsGenerator.createButtonControls(ProjectRolePermissionCollections.TASKS);
        Button viewTaskList = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_VIEW), new Button.ClickListener() {
            private static final long serialVersionUID = 150675475815367481L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoListView(this, currentTaskList.getId()));
            }
        });
        viewTaskList.setWidth("100%");

        viewTaskList.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

        Button addNewTask = new Button(AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK), new Button.ClickListener() {
            private static final long serialVersionUID = -8074297964143853121L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(this, currentTaskList.getId()));
            }
        });
        addNewTask.setWidth("100%");
        addNewTask.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

        Button closeBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                currentTaskList.setStatus(StatusI18nEnum.Closed.name());
                final ProjectTaskListService taskListService = ApplicationContextUtil.getSpringBean(ProjectTaskListService.class);
                taskListService.updateWithSession(currentTaskList, AppContext.getUsername());
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }
        });
        closeBtn.setWidth("100%");
        closeBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

        controlsGenerator.insertToControlBlock(closeBtn);
        controlsGenerator.insertToControlBlock(viewTaskList);
        controlsGenerator.insertToControlBlock(addNewTask);
        editBtn.setContent(menuContent);

        return editBtn;
    }

    @Override
    public void addFormHandler(PreviewFormHandler<SimpleTaskList> handler) {
        if (handlers == null) {
            handlers = new HashSet<>();
        }

        handlers.add(handler);
    }

    @Override
    public SimpleTaskList getBean() {
        return this.currentTaskList;
    }

    @Override
    public void setBean(SimpleTaskList bean) {
        this.currentTaskList = bean;
    }

    @Override
    public void fireAssignForm(SimpleTaskList bean) {
        if (handlers != null) {
            for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
                handler.onAssign(bean);
            }
        }
    }

    @Override
    public void fireEditForm(SimpleTaskList bean) {
        if (handlers != null) {
            for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
                handler.onEdit(bean);
            }
        }
    }

    @Override
    public void showHistory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void fireCancelForm(SimpleTaskList bean) {
        if (handlers != null) {
            for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
                handler.onCancel();
            }
        }
    }

    @Override
    public void fireDeleteForm(SimpleTaskList bean) {
        if (handlers != null) {
            for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
                handler.onDelete(bean);
            }
        }
    }

    @Override
    public void fireCloneForm(SimpleTaskList bean) {
        if (handlers != null) {
            for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
                handler.onClone(bean);
            }
        }
    }

    @Override
    public void fireExtraAction(String action, SimpleTaskList bean) {
        if (handlers != null) {
            for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
                handler.onExtraAction(action, bean);
            }
        }
    }

}
