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
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class TaskListDisplay extends DefaultBeanPagedList<ProjectTaskService, TaskSearchCriteria, SimpleTask> {
    public TaskListDisplay() {
        super(ApplicationContextUtil.getSpringBean(ProjectTaskService.class), new TaskRowDisplayHandler(), Integer.MAX_VALUE);
        this.setStyleName("tasklist");
    }

    @Override
    protected String stringWhenEmptyList() {
        return "No tasks in this list yet.";
    }

    public static class TaskRowDisplayHandler implements RowDisplayHandler<SimpleTask> {
        @Override
        public Component generateRow(AbstractBeanPagedList host, SimpleTask task, int rowIndex) {
            return new TaskRowComp(task);
        }
    }

    private static class TaskRowComp extends MHorizontalLayout {
        private Label taskLinkLbl;
        private SimpleTask task;

        private PopupButton taskSettingPopupBtn;

        TaskRowComp(SimpleTask task) {
            this.task = task;
            withSpacing(false).withMargin(true).withWidth("100%").addStyleName("taskrow");
            this.with(createTaskActionControl());

            taskLinkLbl = new Label(buildTaskLink(), ContentMode.HTML);
            this.with(taskLinkLbl).expand(taskLinkLbl);
            if (task.isCompleted()) {
                taskLinkLbl.addStyleName("completed");
            } else if (task.isOverdue()) {
                taskLinkLbl.addStyleName("overdue");
            } else if (task.isPending()) {
                taskLinkLbl.addStyleName("pending");
            }
            taskLinkLbl.addStyleName("wordWrap");
        }

        private String buildTaskLink() {
            String uid = UUID.randomUUID().toString();
            String linkName = String.format("[#%d] - %s", task.getTaskkey(), task.getTaskname());
            A taskLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateTaskPreviewFullLink(task.getTaskkey(),
                    CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");

            taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.TASK, task.getId() + ""));
            taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));

            String avatarLink = StorageManager.getAvatarLink(task.getAssignUserAvatarId(), 16);
            Img avatarImg = new Img(task.getAssignUserFullName(), avatarLink).setTitle(task.getAssignUserFullName());

            Div resultDiv = new DivLessFormatter().appendChild(avatarImg, DivLessFormatter.EMPTY_SPACE(), taskLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            if (task.getPercentagecomplete() != null && task.getPercentagecomplete() > 0) {
                Div completeTxt = new Div().appendChild(new Text(String.format(" %s%%", task.getPercentagecomplete())))
                        .setStyle("display:inline").setCSSClass("footer2");
                resultDiv.appendChild(completeTxt);
            }

            if (task.getDeadline() != null) {
                Div deadline = new Div().appendChild(new Text(String.format(" - %s: %s", AppContext.getMessage
                        (TaskI18nEnum.FORM_DEADLINE), AppContext.formatPrettyTime(task.getDeadline()))))
                        .setStyle("display:inline").setCSSClass("footer2").setTitle(AppContext.formatDate(task.getDeadline()));

                resultDiv.appendChild(deadline);
            }
            return resultDiv.write();
        }

        private void closeTask() {
            taskLinkLbl.removeStyleName("overdue pending");
            taskLinkLbl.addStyleName("completed");
            OptionPopupContent filterBtnLayout = createPopupContent();
            taskSettingPopupBtn.setContent(filterBtnLayout);
        }

        private void reOpenTask() {
            taskLinkLbl.removeStyleName("overdue pending completed");
            OptionPopupContent filterBtnLayout = createPopupContent();
            taskSettingPopupBtn.setContent(filterBtnLayout);
        }

        private void pendingTask() {
            taskLinkLbl.removeStyleName("overdue completed");
            taskLinkLbl.addStyleName("pending");
            OptionPopupContent filterBtnLayout = createPopupContent();
            taskSettingPopupBtn.setContent(filterBtnLayout);
        }

        private void deleteTask() {
            ComponentContainer parent = (ComponentContainer) this.getParent();
            if (parent != null) {
                parent.removeComponent(this);
            }
        }

        private PopupButton createTaskActionControl() {
            taskSettingPopupBtn = new PopupButton();
            taskSettingPopupBtn.setIcon(FontAwesome.COGS);

            taskSettingPopupBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);

            OptionPopupContent filterBtnLayout = createPopupContent();
            taskSettingPopupBtn.setContent(filterBtnLayout);
            return taskSettingPopupBtn;
        }

        private OptionPopupContent createPopupContent() {
            OptionPopupContent filterBtnLayout = new OptionPopupContent().withWidth("100px");

            Button editButton = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            taskSettingPopupBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(
                                    new TaskEvent.GotoEdit(TaskRowComp.this, task));
                        }
                    });
            editButton.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            editButton.setIcon(FontAwesome.EDIT);
            filterBtnLayout.addOption(editButton);

            if (!task.isCompleted()) {
                Button closeBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                task.setStatus(OptionI18nEnum.StatusI18nEnum.Closed.name());
                                task.setPercentagecomplete(100d);
                                ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                                projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                                taskSettingPopupBtn.setPopupVisible(false);
                                closeTask();
                            }
                        });
                closeBtn.setIcon(FontAwesome.CHECK_CIRCLE_O);
                closeBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
                filterBtnLayout.addOption(closeBtn);
            } else {
                Button reOpenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                taskSettingPopupBtn.setPopupVisible(false);
                                task.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
                                task.setPercentagecomplete(0d);

                                ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                                projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                                reOpenTask();
                            }
                        });
                reOpenBtn.setIcon(FontAwesome.UNLOCK);
                reOpenBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
                filterBtnLayout.addOption(reOpenBtn);
            }

            if (!"Pending".equals(task.getStatus())) {
                if (!"Closed".equals(task.getStatus())) {
                    Button pendingBtn = new Button(AppContext.getMessage(OptionI18nEnum.StatusI18nEnum.Pending),
                            new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    taskSettingPopupBtn.setPopupVisible(false);
                                    task.setStatus("Pending");
                                    task.setPercentagecomplete(0d);

                                    ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                                    projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                                    pendingTask();
                                }
                            });
                    pendingBtn.setIcon(FontAwesome.HDD_O);
                    pendingBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
                    filterBtnLayout.addOption(pendingBtn);
                }
            } else {
                Button reOpenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                taskSettingPopupBtn.setPopupVisible(false);
                                task.setStatus("Open");
                                task.setPercentagecomplete(0d);

                                ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                                projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());

                                reOpenTask();
                            }
                        });
                reOpenBtn.setIcon(FontAwesome.UNLOCK);
                reOpenBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
                filterBtnLayout.addOption(reOpenBtn);
            }

            Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            taskSettingPopupBtn.setPopupVisible(false);
                            ConfirmDialogExt.show(UI.getCurrent(),
                                    AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                                            AppContext.getSiteName()),
                                    AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                    new ConfirmDialog.Listener() {
                                        private static final long serialVersionUID = 1L;

                                        @Override
                                        public void onClose(
                                                ConfirmDialog dialog) {
                                            if (dialog.isConfirmed()) {
                                                ProjectTaskService projectTaskService = ApplicationContextUtil
                                                        .getSpringBean(ProjectTaskService.class);
                                                projectTaskService.removeWithSession(
                                                        task.getId(), AppContext.getUsername(), AppContext.getAccountId());
                                                deleteTask();
                                            }
                                        }
                                    });
                        }
                    });
            deleteBtn.setIcon(FontAwesome.TRASH_O);
            deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.TASKS));
            filterBtnLayout.addOption(deleteBtn);
            return filterBtnLayout;
        }
    }
}
