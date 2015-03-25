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

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.ProgressPercentageIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskTableDisplay extends
        DefaultPagedBeanTable<ProjectTaskService, TaskSearchCriteria, SimpleTask> {
    private static final long serialVersionUID = 1L;

    TaskTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns, SearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS);
    }

    public TaskTableDisplay(TableViewField requiredColumn,
                            List<TableViewField> displayColumns) {
        this(requiredColumn, displayColumns, Integer.MAX_VALUE);
    }

    public TaskTableDisplay(TableViewField requiredColumn,
                            List<TableViewField> displayColumns, int displayNums) {
        super(ApplicationContextUtil.getSpringBean(ProjectTaskService.class),
                SimpleTask.class, requiredColumn, displayColumns);
        this.displayNumItems = displayNums;

        this.addGeneratedColumn("taskname", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleTask task = getBeanByIndex(itemId);

                CssLayout taskName = new CssLayout();

                String taskname = "[%s-%s] %s";
                taskname = String.format(taskname, CurrentProjectVariables
                        .getProject().getShortname(), task.getTaskkey(), task
                        .getTaskname());

                LabelLink b = new LabelLink(taskname, ProjectLinkBuilder
                        .generateTaskPreviewFullLink(task.getTaskkey(),
                                task.getProjectShortname()));
                b.setDescription(ProjectTooltipGenerator.generateToolTipTask(
                        AppContext.getUserLocale(), task,
                        AppContext.getSiteUrl(), AppContext.getTimezone()));

                if (StringUtils.isNotBlank(task.getPriority())) {
                    b.setIconLink(ProjectResources
                            .getIconResourceLink12ByTaskPriority(task
                                    .getPriority()));

                }

                if (task.isCompleted()) {
                    b.addStyleName(UIConstants.LINK_COMPLETED);
                } else if (task.isPending()) {
                    b.addStyleName(UIConstants.LINK_PENDING);
                } else if (task.isOverdue()) {
                    b.addStyleName(UIConstants.LINK_OVERDUE);
                }

                taskName.addComponent(b);
                taskName.setWidth("100%");
                taskName.setHeightUndefined();
                return taskName;

            }
        });

        this.addGeneratedColumn("percentagecomplete",
                new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public com.vaadin.ui.Component generateCell(Table source,
                                                                final Object itemId, Object columnId) {
                        final SimpleTask task = getBeanByIndex(itemId);
                        Double percomp = (task.getPercentagecomplete() == null) ? new Double(
                                0) : task.getPercentagecomplete();
                        ProgressPercentageIndicator progress = new ProgressPercentageIndicator(
                                percomp);
                        progress.setWidth("100px");
                        return progress;
                    }
                });

        this.addGeneratedColumn("startdate", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleTask task = TaskTableDisplay.this
                        .getBeanByIndex(itemId);
                return new Label(AppContext.formatDate(task.getStartdate()));

            }
        });

        this.addGeneratedColumn("deadline", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleTask task = getBeanByIndex(itemId);
                return new Label(AppContext.formatDate(task.getDeadline()));

            }
        });

        this.addGeneratedColumn("id", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleTask task = getBeanByIndex(itemId);
                PopupButton taskSettingPopupBtn = new PopupButton();
                taskSettingPopupBtn.setIcon(FontAwesome.COGS);
                taskSettingPopupBtn.setStyleName("action");
                taskSettingPopupBtn.addStyleName("noDefaultIcon");

                MVerticalLayout filterBtnLayout = new MVerticalLayout().withWidth("100px");

                Button editButton = new Button(AppContext
                        .getMessage(GenericI18Enum.BUTTON_EDIT),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(ClickEvent event) {
                                EventBusFactory.getInstance().post(
                                        new TaskEvent.GotoEdit(
                                                TaskTableDisplay.this, task));
                            }
                        });
                editButton.setEnabled(CurrentProjectVariables
                        .canWrite(ProjectRolePermissionCollections.TASKS));
                editButton.setIcon(FontAwesome.EDIT);
                editButton.setStyleName("action");
                filterBtnLayout.addComponent(editButton);

                if ((task.getPercentagecomplete() != null && task
                        .getPercentagecomplete() != 100)
                        || task.getPercentagecomplete() == null) {
                    Button closeBtn = new Button(AppContext
                            .getMessage(GenericI18Enum.BUTTON_CLOSE),
                            new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    task.setStatus(StatusI18nEnum.Closed.name());
                                    task.setPercentagecomplete(100d);

                                    ProjectTaskService projectTaskService = ApplicationContextUtil
                                            .getSpringBean(ProjectTaskService.class);
                                    projectTaskService
                                            .updateSelectiveWithSession(task,
                                                    AppContext.getUsername());

                                    fireTableEvent(new TableClickEvent(
                                            TaskTableDisplay.this, task,
                                            "closeTask"));
                                }
                            });
                    closeBtn.setIcon(FontAwesome.CHECK_CIRCLE_O);
                    closeBtn.setStyleName("action");
                    closeBtn.setEnabled(CurrentProjectVariables
                            .canWrite(ProjectRolePermissionCollections.TASKS));
                    filterBtnLayout.addComponent(closeBtn);
                } else {
                    Button reOpenBtn = new Button("ReOpen",
                            new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    task.setStatus(StatusI18nEnum.Open.name());
                                    task.setPercentagecomplete(0d);

                                    ProjectTaskService projectTaskService = ApplicationContextUtil
                                            .getSpringBean(ProjectTaskService.class);
                                    projectTaskService
                                            .updateSelectiveWithSession(task,
                                                    AppContext.getUsername());
                                    fireTableEvent(new TableClickEvent(
                                            TaskTableDisplay.this, task,
                                            "reopenTask"));
                                }
                            });
                    reOpenBtn.setIcon(FontAwesome.UNLOCK);
                    reOpenBtn.setStyleName("action");
                    reOpenBtn.setEnabled(CurrentProjectVariables
                            .canWrite(ProjectRolePermissionCollections.TASKS));
                    filterBtnLayout.addComponent(reOpenBtn);
                }

                if (!"Pending".equals(task.getStatus())) {
                    if (!"Closed".equals(task.getStatus())) {
                        Button pendingBtn = new Button("Pending",
                                new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        task.setStatus("Pending");
                                        task.setPercentagecomplete(0d);

                                        ProjectTaskService projectTaskService = ApplicationContextUtil
                                                .getSpringBean(ProjectTaskService.class);
                                        projectTaskService
                                                .updateSelectiveWithSession(
                                                        task, AppContext
                                                                .getUsername());
                                        fireTableEvent(new TableClickEvent(
                                                TaskTableDisplay.this, task,
                                                "pendingTask"));
                                    }
                                });
                        pendingBtn.setIcon(FontAwesome.HDD_O);
                        pendingBtn.setStyleName("action");
                        pendingBtn.setEnabled(CurrentProjectVariables
                                .canWrite(ProjectRolePermissionCollections.TASKS));
                        filterBtnLayout.addComponent(pendingBtn);
                    }
                } else {
                    Button reOpenBtn = new Button("ReOpen",
                            new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    task.setStatus("Open");
                                    task.setPercentagecomplete(0d);

                                    ProjectTaskService projectTaskService = ApplicationContextUtil
                                            .getSpringBean(ProjectTaskService.class);
                                    projectTaskService
                                            .updateSelectiveWithSession(task,
                                                    AppContext.getUsername());

                                    fireTableEvent(new TableClickEvent(
                                            TaskTableDisplay.this, task,
                                            "reopenTask"));
                                }
                            });
                    reOpenBtn.setIcon(FontAwesome.UNLOCK);
                    reOpenBtn.setStyleName("action");
                    reOpenBtn.setEnabled(CurrentProjectVariables
                            .canWrite(ProjectRolePermissionCollections.TASKS));
                    filterBtnLayout.addComponent(reOpenBtn);
                }

                Button deleteBtn = new Button(AppContext
                        .getMessage(GenericI18Enum.BUTTON_DELETE),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(ClickEvent event) {
                                ConfirmDialogExt.show(
                                        UI.getCurrent(),
                                        AppContext
                                                .getMessage(
                                                        GenericI18Enum.DIALOG_DELETE_TITLE,
                                                        SiteConfiguration
                                                                .getSiteName()),
                                        AppContext
                                                .getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                        AppContext
                                                .getMessage(GenericI18Enum.BUTTON_YES),
                                        AppContext
                                                .getMessage(GenericI18Enum.BUTTON_NO),
                                        new ConfirmDialog.Listener() {
                                            private static final long serialVersionUID = 1L;

                                            @Override
                                            public void onClose(
                                                    ConfirmDialog dialog) {
                                                if (dialog.isConfirmed()) {
                                                    ProjectTaskService projectTaskService = ApplicationContextUtil
                                                            .getSpringBean(ProjectTaskService.class);
                                                    projectTaskService.removeWithSession(
                                                            task.getId(),
                                                            AppContext.getUsername(),
                                                            AppContext.getAccountId());
                                                    fireTableEvent(new TableClickEvent(
                                                            TaskTableDisplay.this,
                                                            task, "deleteTask"));
                                                }
                                            }
                                        });
                            }
                        });
                deleteBtn.setIcon(FontAwesome.TRASH_O);
                deleteBtn.setStyleName("action");
                deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.TASKS));
                filterBtnLayout.addComponent(deleteBtn);

                taskSettingPopupBtn.setContent(filterBtnLayout);
                return taskSettingPopupBtn;
            }
        });

        this.addGeneratedColumn("assignUserFullName",
                new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public com.vaadin.ui.Component generateCell(Table source,
                                                                final Object itemId, Object columnId) {
                        final SimpleTask task = getBeanByIndex(itemId);
                        return new ProjectUserLink(task.getAssignuser(), task
                                .getAssignUserAvatarId(), task
                                .getAssignUserFullName());

                    }
                });
        this.setWidth("100%");
    }
}
