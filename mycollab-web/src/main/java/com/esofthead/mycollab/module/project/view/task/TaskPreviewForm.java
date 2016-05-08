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
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.utils.HumanTime;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.ui.form.ProjectItemViewField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.project.view.task.components.TaskSearchPanel;
import com.esofthead.mycollab.module.project.view.task.components.ToggleTaskSummaryWithParentRelationshipField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.field.DateViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.I18nFormViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.RichTextViewField;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class TaskPreviewForm extends AdvancedPreviewBeanForm<SimpleTask> {
    @Override
    public void setBean(SimpleTask bean) {
        this.setFormLayoutFactory(new DynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getForm(),
                Task.Field.taskname.name()));
        this.setBeanFormFieldFactory(new PreviewFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
        private static final long serialVersionUID = 1L;

        public PreviewFormFieldFactory(GenericBeanForm<SimpleTask> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleTask beanItem = attachForm.getBean();
            if (Task.Field.assignuser.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getAssignuser(),
                        beanItem.getAssignUserAvatarId(), beanItem.getAssignUserFullName());
            } else if (Task.Field.startdate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getStartdate());
            } else if (Task.Field.enddate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getEnddate());
            } else if (Task.Field.deadline.equalTo(propertyId)) {
                return new DateViewField(beanItem.getDeadline());
            } else if (Task.Field.milestoneid.equalTo(propertyId)) {
                return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid() + "", beanItem.getMilestoneName());
            } else if (Task.Field.id.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(beanItem.getProjectid(), ProjectTypeConstants.TASK, beanItem.getId());
            } else if (Task.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    FontAwesome fontPriority = ProjectAssetsManager.getTaskPriority(beanItem.getPriority());
                    String priorityLbl = fontPriority.getHtml() + " " + AppContext.getMessage(OptionI18nEnum.TaskPriority.class, beanItem.getPriority());
                    DefaultViewField field = new DefaultViewField(priorityLbl, ContentMode.HTML);
                    field.addStyleName("task-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (Task.Field.isestimated.equalTo(propertyId)) {
                return new DefaultViewField(Boolean.TRUE.equals(beanItem.getIsestimated()) ? "Yes" : "No");
            } else if (Task.Field.duration.equalTo(propertyId)) {
                if (beanItem.getDuration() != null) {
                    HumanTime humanTime = new HumanTime(beanItem.getDuration().longValue());
                    return new DefaultViewField(humanTime.getExactly());
                }
            } else if (Task.Field.notes.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getNotes());
            } else if (Task.Field.parenttaskid.equalTo(propertyId)) {
                return new SubTasksComp(beanItem);
            } else if (Task.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class)
                        .withStyleName(UIConstants.FIELD_NOTE);
            }
            return null;
        }
    }

    private static class SubTasksComp extends CustomField {
        private static final long serialVersionUID = 1L;

        private ApplicationEventListener<TaskEvent.NewTaskAdded> newTaskAddedHandler = new
                ApplicationEventListener<TaskEvent.NewTaskAdded>() {
                    @Override
                    @Subscribe
                    public void handle(TaskEvent.NewTaskAdded event) {
                        final ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                        SimpleTask task = projectTaskService.findById((Integer) event.getData(), AppContext.getAccountId());
                        if (task != null && tasksLayout != null) {
                            tasksLayout.addComponent(generateSubTaskContent(task), 0);
                        }
                    }
                };

        private MVerticalLayout tasksLayout;
        private SimpleTask beanItem;

        SubTasksComp(SimpleTask task) {
            this.beanItem = task;
        }

        @Override
        public void attach() {
            EventBusFactory.getInstance().register(newTaskAddedHandler);
            super.attach();
        }

        @Override
        public void detach() {
            EventBusFactory.getInstance().unregister(newTaskAddedHandler);
            super.detach();
        }

        @Override
        protected Component initContent() {
            MHorizontalLayout contentLayout = new MHorizontalLayout().withWidth("100%");
            tasksLayout = new VerticalRemoveInlineComponentMarker().withWidth("100%").withMargin(new MarginInfo(false, true, true, false));
            contentLayout.with(tasksLayout).expand(tasksLayout);

            Button addNewTaskBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    SimpleTask task = new SimpleTask();
                    task.setMilestoneid(beanItem.getMilestoneid());
                    task.setParenttaskid(beanItem.getId());
                    task.setPriority(OptionI18nEnum.TaskPriority.Medium.name());
                    task.setProjectid(beanItem.getProjectid());
                    task.setSaccountid(beanItem.getSaccountid());
                    UI.getCurrent().addWindow(new TaskAddWindow(task));
                }
            });
            addNewTaskBtn.setStyleName(UIConstants.BUTTON_ACTION);
            addNewTaskBtn.setIcon(FontAwesome.PLUS);
            addNewTaskBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

            final SplitButton splitButton = new SplitButton(addNewTaskBtn);
            splitButton.setWidthUndefined();
            splitButton.addStyleName(UIConstants.BUTTON_ACTION);

            OptionPopupContent popupButtonsControl = new OptionPopupContent();
            Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    splitButton.setPopupVisible(false);
                    UI.getCurrent().addWindow(new SelectChildTaskWindow(beanItem));
                }
            });
            selectBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            popupButtonsControl.addOption(selectBtn);
            splitButton.setContent(popupButtonsControl);

            contentLayout.addComponent(splitButton);

            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
            List<SimpleTask> subTasks = taskService.findSubTasks(beanItem.getId(), AppContext.getAccountId(), new
                    SearchCriteria.OrderField("createdTime", SearchCriteria.DESC));
            if (CollectionUtils.isNotEmpty(subTasks)) {
                for (SimpleTask subTask : subTasks) {
                    tasksLayout.addComponent(generateSubTaskContent(subTask));
                }
            }
            return contentLayout;
        }

        @Override
        public Class getType() {
            return Object.class;
        }

        private HorizontalLayout generateSubTaskContent(final SimpleTask subTask) {
            MHorizontalLayout layout = new MHorizontalLayout().withStyleName(UIConstants.HOVER_EFFECT_NOT_BOX);
            layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            final CheckBox checkBox = new CheckBox("", subTask.isCompleted());
            checkBox.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            layout.with(checkBox);

            Span priorityLink = new Span().appendText(ProjectAssetsManager.getTaskPriorityHtml(subTask.getPriority()))
                    .setTitle(subTask.getPriority());
            layout.with(new ELabel(priorityLink.write(), ContentMode.HTML).withWidthUndefined());

            String avatarLink = StorageFactory.getInstance().getAvatarPath(subTask.getAssignUserAvatarId(), 16);
            Img avatarImg = new Img(subTask.getAssignUserFullName(), avatarLink).setTitle(subTask.getAssignUserFullName());
            layout.with(new ELabel(avatarImg.write(), ContentMode.HTML).withWidthUndefined());

            final ToggleTaskSummaryWithParentRelationshipField toggleTaskSummaryField = new ToggleTaskSummaryWithParentRelationshipField(subTask);
            layout.with(toggleTaskSummaryField).expand(toggleTaskSummaryField);

            checkBox.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    Boolean selectedFlag = checkBox.getValue();
                    ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    if (selectedFlag) {
                        subTask.setStatus(com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.Closed.name());
                        subTask.setPercentagecomplete(100d);
                        toggleTaskSummaryField.closeTask();
                    } else {
                        subTask.setStatus(com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.Open.name());
                        subTask.setPercentagecomplete(0d);
                        if (subTask.isOverdue()) {
                            toggleTaskSummaryField.overdueTask();
                        } else {
                            toggleTaskSummaryField.reOpenTask();
                        }
                    }
                    taskService.updateSelectiveWithSession(subTask, AppContext.getUsername());
                    toggleTaskSummaryField.updateLabel();
                }
            });
            return layout;
        }
    }

    private static class SelectChildTaskWindow extends Window {
        private TaskSearchPanel taskSearchPanel;
        private SimpleTask parentTask;

        SelectChildTaskWindow(SimpleTask parentTask) {
            super("Select Task");
            this.setWidth("800px");
            this.setModal(true);
            this.setResizable(false);
            this.parentTask = parentTask;

            TaskSearchCriteria baseSearchCriteria = new TaskSearchCriteria();
            baseSearchCriteria.setProjectId(NumberSearchField.and(CurrentProjectVariables.getProjectId()));
            baseSearchCriteria.setHasParentTask(new BooleanSearchField(false));

            taskSearchPanel = new TaskSearchPanel(false);
            final DefaultBeanPagedList<ProjectTaskService, TaskSearchCriteria, SimpleTask> taskList = new DefaultBeanPagedList<>(
                    ApplicationContextUtil.getSpringBean(ProjectTaskService.class), new TaskRowRenderer(), 10);
            new Restrain(taskList).setMaxHeight((UIUtils.getBrowserHeight() - 120) + "px");
            taskSearchPanel.addSearchHandler(new SearchHandler<TaskSearchCriteria>() {
                @Override
                public void onSearch(TaskSearchCriteria criteria) {
                    criteria.setProjectId(NumberSearchField.and(CurrentProjectVariables.getProjectId()));
                    criteria.setHasParentTask(new BooleanSearchField(false));
                    taskList.setSearchCriteria(criteria);
                }
            });
            MVerticalLayout content = new MVerticalLayout(taskSearchPanel, taskList).withSpacing(false);
            taskList.setSearchCriteria(baseSearchCriteria);
            setContent(content);
        }

        private class TaskRowRenderer implements AbstractBeanPagedList.RowDisplayHandler<SimpleTask> {
            @Override
            public Component generateRow(AbstractBeanPagedList host, final SimpleTask item, int rowIndex) {
                Button taskLink = new Button(item.getTaskname());
                taskLink.addStyleName(UIConstants.BUTTON_LINK);
                taskLink.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        if (item.getId().equals(parentTask.getId())) {
                            NotificationUtil.showErrorNotification("Can not assign the parent task to itself");
                        } else {
                            item.setParenttaskid(parentTask.getId());
                            ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                            projectTaskService.updateWithSession(item, AppContext.getUsername());
                            EventBusFactory.getInstance().post(new TaskEvent.NewTaskAdded(this, item.getId()));
                        }

                        close();
                    }
                });
                return new MCssLayout(taskLink).withStyleName("list-row").withFullWidth();
            }
        }
    }
}
