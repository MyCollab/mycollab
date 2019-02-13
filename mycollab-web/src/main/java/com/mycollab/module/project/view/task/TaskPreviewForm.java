/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.task;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.BooleanSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.mycollab.module.project.ui.form.ProjectItemViewField;
import com.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.ui.field.StyleViewField;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        this.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getReadForm(),
                Task.Field.name.name(), SimpleTask.Field.selected.name()));
        this.setBeanFormFieldFactory(new PreviewFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
        private static final long serialVersionUID = 1L;

        PreviewFormFieldFactory(GenericBeanForm<SimpleTask> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(final Object propertyId) {
            SimpleTask beanItem = attachForm.getBean();
            if (Task.Field.assignuser.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getProjectid(), beanItem.getAssignuser(),
                        beanItem.getAssignUserAvatarId(), beanItem.getAssignUserFullName());
            } else if (Task.Field.milestoneid.equalTo(propertyId)) {
                return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid(), beanItem.getMilestoneName());
            } else if ("section-attachments".equals(propertyId)) {
                return new ProjectFormAttachmentDisplayField(beanItem.getProjectid(), ProjectTypeConstants.TASK, beanItem.getId());
            } else if (Task.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    VaadinIcons fontPriority = ProjectAssetsManager.getPriority(beanItem.getPriority());
                    String priorityLbl = fontPriority.getHtml() + " " + UserUIContext.getMessage(Priority.class, beanItem.getPriority());
                    StyleViewField field = new StyleViewField(priorityLbl);
                    field.addStyleName("priority-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (Task.Field.isestimated.equalTo(propertyId)) {
                return new DefaultViewField(Boolean.TRUE.equals(beanItem.getIsestimated()) ?
                        UserUIContext.getMessage(GenericI18Enum.ACTION_YES) : UserUIContext.getMessage(GenericI18Enum.ACTION_NO));
            } else if (Task.Field.description.equalTo(propertyId)) {
                return new RichTextViewField();
            } else if ("section-subTasks".equals(propertyId)) {
                return new SubTasksComp(beanItem);
            } else if (Task.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(StatusI18nEnum.class).withStyleName(WebThemes.FIELD_NOTE);
            }
            return null;
        }
    }

    private static class SubTasksComp extends IgnoreBindingField {
        private static final long serialVersionUID = 1L;

        private ApplicationEventListener<TaskEvent.NewTaskAdded> newTaskAddedHandler = new
                ApplicationEventListener<TaskEvent.NewTaskAdded>() {
                    @Override
                    @Subscribe
                    public void handle(TaskEvent.NewTaskAdded event) {
                        final ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                        SimpleTask task = projectTaskService.findById(event.getData(), AppUI.getAccountId());
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
                MButton addNewTaskBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                    SimpleTask task = new SimpleTask();
                    task.setMilestoneid(beanItem.getMilestoneid());
                    task.setParenttaskid(beanItem.getId());
                    task.setPriority(Priority.Medium.name());
                    task.setProjectid(beanItem.getProjectid());
                    task.setSaccountid(beanItem.getSaccountid());
                    UI.getCurrent().addWindow(new TaskAddWindow(task));
                }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.PLUS);

                SplitButton splitButton = new SplitButton(addNewTaskBtn);
                splitButton.setWidthUndefined();
                splitButton.addStyleName(WebThemes.BUTTON_ACTION);

                OptionPopupContent popupButtonsControl = new OptionPopupContent();
                Button selectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                    splitButton.setPopupVisible(false);
                    UI.getCurrent().addWindow(new SelectChildTaskWindow(beanItem));
                });
                popupButtonsControl.addOption(selectBtn);
                splitButton.setContent(popupButtonsControl);
                contentLayout.addComponent(splitButton);
            }

            ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
            List<SimpleTask> subTasks = taskService.findSubTasks(beanItem.getId(), AppUI.getAccountId(),
                    new SearchCriteria.OrderField("createdTime", SearchCriteria.DESC));
            if (CollectionUtils.isNotEmpty(subTasks)) {
                for (SimpleTask subTask : subTasks) {
                    tasksLayout.addComponent(generateSubTaskContent(subTask));
                }
            }
            return contentLayout;
        }

        @Override
        protected void doSetValue(Object o) {

        }

        @Override
        public Object getValue() {
            return null;
        }

        private HorizontalLayout generateSubTaskContent(final SimpleTask subTask) {
            MHorizontalLayout layout = new MHorizontalLayout().withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX);
            layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            final CheckBox checkBox = new CheckBox("", subTask.isCompleted());
            checkBox.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            layout.with(checkBox);

            Span priorityLink = new Span().appendText(ProjectAssetsManager.getPriorityHtml(subTask.getPriority()))
                    .setTitle(subTask.getPriority());
            layout.with(ELabel.html(priorityLink.write()).withUndefinedWidth());

            String taskStatus = UserUIContext.getMessage(StatusI18nEnum.class, subTask.getStatus());
            final ELabel statusLbl = new ELabel(taskStatus).withStyleName(WebThemes.FIELD_NOTE).withUndefinedWidth();
            layout.with(statusLbl);

            String avatarLink = StorageUtils.getAvatarPath(subTask.getAssignUserAvatarId(), 16);
            Img avatarImg = new Img(subTask.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                    .setTitle(subTask.getAssignUserFullName());
            layout.with(ELabel.html(avatarImg.write()).withUndefinedWidth());

            final ToggleTaskSummaryWithParentRelationshipField toggleTaskSummaryField = new ToggleTaskSummaryWithParentRelationshipField(subTask);
            layout.with(toggleTaskSummaryField).expand(toggleTaskSummaryField);

            checkBox.addValueChangeListener(valueChangeEvent -> {
                Boolean selectedFlag = checkBox.getValue();
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                if (selectedFlag) {
                    statusLbl.setValue(UserUIContext.getMessage(StatusI18nEnum.class, StatusI18nEnum.Closed.name()));
                    subTask.setStatus(StatusI18nEnum.Closed.name());
                    subTask.setPercentagecomplete(100d);
                    toggleTaskSummaryField.closeTask();
                } else {
                    statusLbl.setValue(UserUIContext.getMessage(StatusI18nEnum.class, StatusI18nEnum.Open.name()));
                    subTask.setStatus(StatusI18nEnum.Open.name());
                    subTask.setPercentagecomplete(0d);
                    if (subTask.isOverdue()) {
                        toggleTaskSummaryField.overdueTask();
                    } else {
                        toggleTaskSummaryField.reOpenTask();
                    }
                }
                taskService.updateSelectiveWithSession(subTask, UserUIContext.getUsername());
                toggleTaskSummaryField.updateLabel();
            });
            return layout;
        }
    }

    private static class SelectChildTaskWindow extends MWindow {
        private TaskSearchPanel taskSearchPanel;
        private SimpleTask parentTask;

        SelectChildTaskWindow(SimpleTask parentTask) {
            super(UserUIContext.getMessage(TaskI18nEnum.ACTION_SELECT_TASK));
            this.withModal(true).withResizable(false).withWidth(WebThemes.WINDOW_FORM_WIDTH);
            this.parentTask = parentTask;

            TaskSearchCriteria baseSearchCriteria = new TaskSearchCriteria();
            baseSearchCriteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId()));
            baseSearchCriteria.setHasParentTask(new BooleanSearchField(false));

            taskSearchPanel = new TaskSearchPanel(false);
            DefaultBeanPagedList<ProjectTaskService, TaskSearchCriteria, SimpleTask> taskList = new DefaultBeanPagedList<>(
                    AppContextUtil.getSpringBean(ProjectTaskService.class), new TaskRowRenderer(), 10);
            taskSearchPanel.addSearchHandler(criteria -> {
                criteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId()));
                criteria.setHasParentTask(new BooleanSearchField(false));
                taskList.setSearchCriteria(criteria);
            });
            MVerticalLayout content = new MVerticalLayout(taskSearchPanel, taskList).withSpacing(false);
            taskList.setSearchCriteria(baseSearchCriteria);
            setContent(content);
        }

        private class TaskRowRenderer implements IBeanList.RowDisplayHandler<SimpleTask> {
            @Override
            public Component generateRow(IBeanList<SimpleTask> host, SimpleTask item, int rowIndex) {
                MButton taskLink = new MButton(item.getName(), clickEvent -> {
                    if (item.getId().equals(parentTask.getId())) {
                        NotificationUtil.showErrorNotification(UserUIContext.getMessage(TaskI18nEnum.ERROR_CAN_NOT_ASSIGN_PARENT_TASK_TO_ITSELF));
                    } else {
                        item.setParenttaskid(parentTask.getId());
                        ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                        projectTaskService.updateWithSession(item, UserUIContext.getUsername());
                        EventBusFactory.getInstance().post(new TaskEvent.NewTaskAdded(this, item.getId()));
                    }

                    close();
                }).withStyleName(WebThemes.BUTTON_LINK, WebThemes.TEXT_ELLIPSIS).withFullWidth();
                return new MCssLayout(taskLink).withStyleName("list-row").withFullWidth();
            }
        }
    }
}
