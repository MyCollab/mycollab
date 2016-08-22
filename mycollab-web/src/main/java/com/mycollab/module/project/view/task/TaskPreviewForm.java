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
package com.mycollab.module.project.view.task;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.HumanTime;
import com.mycollab.db.arguments.BooleanSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.events.TaskEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.mycollab.module.project.ui.form.ProjectItemViewField;
import com.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.mycollab.module.project.view.task.components.TaskSearchPanel;
import com.mycollab.module.project.view.task.components.ToggleTaskSummaryWithParentRelationshipField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.DateTimeOptionViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class TaskPreviewForm extends AdvancedPreviewBeanForm<SimpleTask> {
    @Override
    public void setBean(SimpleTask bean) {
        this.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getForm(),
                Task.Field.taskname.name(), SimpleTask.Field.selected.name()));
        this.setBeanFormFieldFactory(new PreviewFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
        private static final long serialVersionUID = 1L;

        PreviewFormFieldFactory(GenericBeanForm<SimpleTask> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleTask beanItem = attachForm.getBean();
            if (Task.Field.assignuser.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getAssignuser(), beanItem.getAssignUserAvatarId(),
                        beanItem.getAssignUserFullName());
            } else if (Task.Field.startdate.equalTo(propertyId)) {
                return new DateTimeOptionViewField(beanItem.getStartdate());
            } else if (Task.Field.enddate.equalTo(propertyId)) {
                return new DateTimeOptionViewField(beanItem.getEnddate());
            } else if (Task.Field.deadline.equalTo(propertyId)) {
                return new DateTimeOptionViewField(beanItem.getDeadline());
            } else if (Task.Field.milestoneid.equalTo(propertyId)) {
                return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid() + "", beanItem.getMilestoneName());
            } else if (Task.Field.id.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(beanItem.getProjectid(), ProjectTypeConstants.TASK, beanItem.getId());
            } else if (Task.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    FontAwesome fontPriority = ProjectAssetsManager.getTaskPriority(beanItem.getPriority());
                    String priorityLbl = fontPriority.getHtml() + " " + AppContext.getMessage(TaskPriority.class, beanItem.getPriority());
                    DefaultViewField field = new DefaultViewField(priorityLbl, ContentMode.HTML);
                    field.addStyleName("task-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (Task.Field.isestimated.equalTo(propertyId)) {
                return new DefaultViewField(Boolean.TRUE.equals(beanItem.getIsestimated()) ? "Yes" : "No");
            } else if (Task.Field.duration.equalTo(propertyId)) {
                if (beanItem.getDuration() != null) {
                    HumanTime humanTime = new HumanTime(beanItem.getDuration());
                    return new DefaultViewField(humanTime.getExactly());
                }
            } else if (Task.Field.notes.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getNotes());
            } else if (Task.Field.parenttaskid.equalTo(propertyId)) {
                return new SubTasksComp(beanItem);
            } else if (Task.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), StatusI18nEnum.class).withStyleName(WebUIConstants.FIELD_NOTE);
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
                        final ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
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
            MHorizontalLayout contentLayout = new MHorizontalLayout().withFullWidth();
            tasksLayout = new VerticalRemoveInlineComponentMarker().withFullWidth().withMargin(new MarginInfo(false, true, true, false));
            contentLayout.with(tasksLayout).expand(tasksLayout);

            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                MButton addNewTaskBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                    SimpleTask task = new SimpleTask();
                    task.setMilestoneid(beanItem.getMilestoneid());
                    task.setParenttaskid(beanItem.getId());
                    task.setPriority(TaskPriority.Medium.name());
                    task.setProjectid(beanItem.getProjectid());
                    task.setSaccountid(beanItem.getSaccountid());
                    UI.getCurrent().addWindow(new TaskAddWindow(task));
                }).withStyleName(WebUIConstants.BUTTON_ACTION).withIcon(FontAwesome.PLUS);

                final SplitButton splitButton = new SplitButton(addNewTaskBtn);
                splitButton.setWidthUndefined();
                splitButton.addStyleName(WebUIConstants.BUTTON_ACTION);

                OptionPopupContent popupButtonsControl = new OptionPopupContent();
                Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                    splitButton.setPopupVisible(false);
                    UI.getCurrent().addWindow(new SelectChildTaskWindow(beanItem));
                });
                popupButtonsControl.addOption(selectBtn);
                splitButton.setContent(popupButtonsControl);
                contentLayout.addComponent(splitButton);
            }

            ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
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
            MHorizontalLayout layout = new MHorizontalLayout().withStyleName(WebUIConstants.HOVER_EFFECT_NOT_BOX);
            layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            final CheckBox checkBox = new CheckBox("", subTask.isCompleted());
            checkBox.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            layout.with(checkBox);

            Span priorityLink = new Span().appendText(ProjectAssetsManager.getTaskPriorityHtml(subTask.getPriority()))
                    .setTitle(subTask.getPriority());
            layout.with(ELabel.html(priorityLink.write()).withWidthUndefined());

            String taskStatus = AppContext.getMessage(StatusI18nEnum.class, subTask.getStatus());
            final ELabel statusLbl = new ELabel(taskStatus).withStyleName(WebUIConstants.FIELD_NOTE).withWidthUndefined();
            layout.with(statusLbl);

            String avatarLink = StorageFactory.getAvatarPath(subTask.getAssignUserAvatarId(), 16);
            Img avatarImg = new Img(subTask.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                    .setTitle(subTask.getAssignUserFullName());
            layout.with(ELabel.html(avatarImg.write()).withWidthUndefined());

            final ToggleTaskSummaryWithParentRelationshipField toggleTaskSummaryField = new ToggleTaskSummaryWithParentRelationshipField(subTask);
            layout.with(toggleTaskSummaryField).expand(toggleTaskSummaryField);

            checkBox.addValueChangeListener(valueChangeEvent -> {
                Boolean selectedFlag = checkBox.getValue();
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                if (selectedFlag) {
                    statusLbl.setValue(AppContext.getMessage(StatusI18nEnum.class, StatusI18nEnum.Closed.name()));
                    subTask.setStatus(StatusI18nEnum.Closed.name());
                    subTask.setPercentagecomplete(100d);
                    toggleTaskSummaryField.closeTask();
                } else {
                    statusLbl.setValue(AppContext.getMessage(StatusI18nEnum.class, StatusI18nEnum.Open.name()));
                    subTask.setStatus(StatusI18nEnum.Open.name());
                    subTask.setPercentagecomplete(0d);
                    if (subTask.isOverdue()) {
                        toggleTaskSummaryField.overdueTask();
                    } else {
                        toggleTaskSummaryField.reOpenTask();
                    }
                }
                taskService.updateSelectiveWithSession(subTask, AppContext.getUsername());
                toggleTaskSummaryField.updateLabel();
            });
            return layout;
        }
    }

    private static class SelectChildTaskWindow extends MWindow {
        private TaskSearchPanel taskSearchPanel;
        private SimpleTask parentTask;

        SelectChildTaskWindow(SimpleTask parentTask) {
            super(AppContext.getMessage(TaskI18nEnum.ACTION_SELECT_TASK));
            this.withModal(true).withResizable(false).withWidth("800px");
            this.parentTask = parentTask;

            TaskSearchCriteria baseSearchCriteria = new TaskSearchCriteria();
            baseSearchCriteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId()));
            baseSearchCriteria.setHasParentTask(new BooleanSearchField(false));

            taskSearchPanel = new TaskSearchPanel(false);
            final DefaultBeanPagedList<ProjectTaskService, TaskSearchCriteria, SimpleTask> taskList = new DefaultBeanPagedList<>(
                    AppContextUtil.getSpringBean(ProjectTaskService.class), new TaskRowRenderer(), 10);
            new Restrain(taskList).setMaxHeight((UIUtils.getBrowserHeight() - 120) + "px");
            taskSearchPanel.addSearchHandler(criteria -> {
                criteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId()));
                criteria.setHasParentTask(new BooleanSearchField(false));
                taskList.setSearchCriteria(criteria);
            });
            MVerticalLayout content = new MVerticalLayout(taskSearchPanel, taskList).withSpacing(false);
            taskList.setSearchCriteria(baseSearchCriteria);
            setContent(content);
        }

        private class TaskRowRenderer implements AbstractBeanPagedList.RowDisplayHandler<SimpleTask> {
            @Override
            public Component generateRow(AbstractBeanPagedList host, final SimpleTask item, int rowIndex) {
                MButton taskLink = new MButton(item.getTaskname(), clickEvent -> {
                    if (item.getId().equals(parentTask.getId())) {
                        NotificationUtil.showErrorNotification(AppContext.getMessage(TaskI18nEnum.ERROR_CAN_NOT_ASSIGN_PARENT_TASK_TO_ITSELF));
                    } else {
                        item.setParenttaskid(parentTask.getId());
                        ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                        projectTaskService.updateWithSession(item, AppContext.getUsername());
                        EventBusFactory.getInstance().post(new TaskEvent.NewTaskAdded(this, item.getId()));
                    }

                    close();
                }).withStyleName(WebUIConstants.BUTTON_LINK);
                return new MCssLayout(taskLink).withStyleName("list-row").withFullWidth();
            }
        }
    }
}
