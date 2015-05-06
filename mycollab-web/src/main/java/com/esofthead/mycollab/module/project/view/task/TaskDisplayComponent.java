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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.form.ProjectItemViewField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class TaskDisplayComponent extends CssLayout {
    private static final long serialVersionUID = 1L;

    private TaskSearchCriteria criteria;
    private TaskListDisplay taskDisplay;
    private Button createTaskBtn;

    private SimpleTaskList taskList;

    public TaskDisplayComponent(SimpleTaskList taskList) {
        this.taskList = taskList;
        this.showTaskGroupInfo();
        this.setSizeFull();

        //Set default search criteria
        criteria = new TaskSearchCriteria();
        criteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        criteria.setTaskListId(new NumberSearchField(taskList.getId()));
        criteria.setStatuses(new SetSearchField<>(SearchField.AND, new String[]{StatusI18nEnum.Open.name(),
                        StatusI18nEnum.Pending.name()}));
    }

    private void showTaskGroupInfo() {
        AdvancedPreviewBeanForm<SimpleTaskList> previewForm = new AdvancedPreviewBeanForm<>();
        previewForm.setWidth("100%");
        previewForm.setFormLayoutFactory(new IFormLayoutFactory() {
            private static final long serialVersionUID = 1L;

            private GridFormLayoutHelper layoutHelper;

            @Override
            public ComponentContainer getLayout() {
                this.layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
                return this.layoutHelper.getLayout();
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if ("description".equals(propertyId)) {
                    layoutHelper.addComponent(field, AppContext
                                    .getMessage(GenericI18Enum.FORM_DESCRIPTION),
                            0, 0, 2, "100%");
                } else if ("owner".equals(propertyId)) {
                    layoutHelper.addComponent(field, AppContext
                            .getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 1);
                } else if ("milestoneid".equals(propertyId)) {
                    layoutHelper.addComponent(field,
                            AppContext.getMessage(TaskGroupI18nEnum.FORM_PHASE_FIELD), 1, 1);
                }
            }
        });
        previewForm.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList>(previewForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if ("description".equals(propertyId)) {
                    return new DefaultViewField(taskList.getDescription(), ContentMode.HTML);
                } else if ("owner".equals(propertyId)) {
                    return new ProjectUserFormLinkField(taskList.getOwner(),
                            taskList.getOwnerAvatarId(),
                            taskList.getOwnerFullName());
                } else if ("milestoneid".equals(propertyId)) {
                    return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, "" + taskList.getMilestoneid(),
                            taskList.getMilestoneName());
                }

                return null;
            }

        });
        this.addComponent(previewForm);
        previewForm.setBean(this.taskList);

        this.taskDisplay = new TaskListDisplay();
        addComponent(taskDisplay);

        this.createTaskBtn = new Button(
                AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        TaskDisplayComponent.this
                                .removeComponent(createTaskBtn.getParent());
                        TaskAddPopup taskAddView = new TaskAddPopup(
                                TaskDisplayComponent.this, taskList);
                        TaskDisplayComponent.this.addComponent(taskAddView);
                    }
                });
        this.createTaskBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.TASKS));
        this.createTaskBtn.setIcon(FontAwesome.PLUS);
        this.createTaskBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

        VerticalLayout taskGroupFooter = new VerticalLayout();
        taskGroupFooter.setMargin(true);
        taskGroupFooter.addStyleName("task-list-footer");
        taskGroupFooter.addComponent(this.createTaskBtn);
        taskGroupFooter.setComponentAlignment(this.createTaskBtn, Alignment.MIDDLE_RIGHT);
        this.addComponent(taskGroupFooter);

        if (CollectionUtils.isNotEmpty(taskList.getSubTasks())) {
            taskDisplay.setCurrentDataList(taskList.getSubTasks());
        } else {
            taskDisplay.setCurrentDataList(new ArrayList<SimpleTask>());
        }
    }

    public void setSearchCriteria(TaskSearchCriteria criteria) {
        this.criteria = criteria;
        taskDisplay.setSearchCriteria(criteria);
    }

    public void saveTaskSuccess(SimpleTask task) {
        taskDisplay.setSearchCriteria(criteria);
    }

    public void closeTaskAdd() {
        final VerticalLayout taskGroupFooter = new VerticalLayout();
        taskGroupFooter.setMargin(true);
        taskGroupFooter.addStyleName("task-list-footer");
        taskGroupFooter.addComponent(this.createTaskBtn);
        taskGroupFooter.setComponentAlignment(this.createTaskBtn,
                Alignment.MIDDLE_RIGHT);
        this.addComponent(taskGroupFooter);

        Iterator<Component> comps = this.iterator();
        while (comps.hasNext()) {
            Component component = comps.next();
            if (component instanceof TaskAddPopup) {
                this.removeComponent(component);
                return;
            }
        }
    }
}
