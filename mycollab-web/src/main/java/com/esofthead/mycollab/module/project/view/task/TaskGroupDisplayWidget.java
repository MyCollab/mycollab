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
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskGroupDisplayWidget extends
        BeanList<ProjectTaskListService, TaskListSearchCriteria, SimpleTaskList> {
    private static final long serialVersionUID = 1L;

    public TaskGroupDisplayWidget() {
        super(null, ApplicationContextUtil.getSpringBean(ProjectTaskListService.class),
                TaskListRowDisplayHandler.class);
        this.setDisplayEmptyListText(false);
    }

    public static class TaskListRowDisplayHandler extends
            BeanList.RowDisplayHandler<SimpleTaskList> {
        private static final long serialVersionUID = 1L;

        @Override
        public Component generateRow(SimpleTaskList taskList, int rowIndex) {
            return new TaskListDepot(taskList);
        }
    }

    static class TaskListDepot extends Depot {
        private static final long serialVersionUID = 1L;

        private TaskSearchCriteria searchCriteria;
        private SimpleTaskList taskList;
        private PopupButton taskListActionControl;
        private TaskDisplayComponent taskDisplayComponent;

        private Button toogleBtn;

        private TaskListDepot(final SimpleTaskList taskListParam) {
            super(taskListParam.getName(), null, new TaskDisplayComponent(taskListParam, true));

            if (taskListParam.isArchieved()) {
                this.headerLbl.addStyleName(UIConstants.LINK_COMPLETED);
            }
            this.taskList = taskListParam;
            this.addStyleName("task-list");
            this.initHeader();
            this.setHeaderColor(true);
            this.taskDisplayComponent = (TaskDisplayComponent) this.bodyContent;
        }

        private void initHeader() {
            searchCriteria = new TaskSearchCriteria();
            searchCriteria.setProjectid(new NumberSearchField(CurrentProjectVariables
                    .getProjectId()));
            searchCriteria.setTaskListId(new NumberSearchField(taskList.getId()));
            searchCriteria.setStatuses(new SetSearchField<>(new String[]{StatusI18nEnum.Open.name()}));

            final MHorizontalLayout headerElement = new MHorizontalLayout().withMargin(false);
            headerElement.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            final CheckBox activeTasksFilterBtn = new CheckBox(AppContext.getMessage(StatusI18nEnum.Open), true);
            activeTasksFilterBtn.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if (activeTasksFilterBtn.getValue()) {
                        searchCriteria.getStatuses().addValue(StatusI18nEnum.Open.name());
                    } else {
                        searchCriteria.getStatuses().removeValue(StatusI18nEnum.Open.name());
                    }
                    updateSearchFilter();
                }
            });

            final CheckBox pendingTasksFilterBtn = new CheckBox(AppContext.getMessage(StatusI18nEnum.Pending));
            pendingTasksFilterBtn.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if (pendingTasksFilterBtn.getValue()) {
                        searchCriteria.getStatuses().addValue(StatusI18nEnum.Pending.name());
                    } else {
                        searchCriteria.getStatuses().removeValue(StatusI18nEnum.Pending.name());
                    }
                    updateSearchFilter();
                }
            });

            final CheckBox archievedTasksFilterBtn = new CheckBox(AppContext.getMessage(StatusI18nEnum.Closed));
            archievedTasksFilterBtn.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if (archievedTasksFilterBtn.getValue()) {
                        searchCriteria.getStatuses().addValue(StatusI18nEnum.Closed.name());
                    } else {
                        searchCriteria.getStatuses().removeValue(StatusI18nEnum.Closed.name());
                    }
                    updateSearchFilter();
                }
            });
            headerElement.with(activeTasksFilterBtn, pendingTasksFilterBtn, archievedTasksFilterBtn);

            taskListActionControl = new PopupButton();
            taskListActionControl.addStyleName("popuplistindicator");
            taskListActionControl.setWidthUndefined();

            headerElement.with(taskListActionControl);

            this.addHeaderElement(headerElement);

            MVerticalLayout actionBtnLayout = new MVerticalLayout().withWidth("200px");

            taskListActionControl.setContent(actionBtnLayout);

            final Button readBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_VIEW),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            TaskListDepot.this.taskListActionControl
                                    .setPopupVisible(false);
                            EventBusFactory
                                    .getInstance()
                                    .post(new TaskListEvent.GotoRead(event,
                                            TaskListDepot.this.taskList.getId()));
                        }
                    });
            readBtn.setIcon(FontAwesome.HACKER_NEWS);
            readBtn.setEnabled(CurrentProjectVariables
                    .canRead(ProjectRolePermissionCollections.TASKS));
            readBtn.setStyleName("link");
            actionBtnLayout.addComponent(readBtn);

            final Button editBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            TaskListDepot.this.taskListActionControl
                                    .setPopupVisible(false);
                            EventBusFactory.getInstance().post(
                                    new TaskListEvent.GotoEdit(event,
                                            TaskListDepot.this.taskList));
                        }
                    });
            editBtn.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.TASKS));
            editBtn.setStyleName("link");
            editBtn.setIcon(FontAwesome.EDIT);
            actionBtnLayout.addComponent(editBtn);

            Enum actionEnum = (taskList.isArchieved()) ? GenericI18Enum.BUTTON_REOPEN : GenericI18Enum.BUTTON_CLOSE;

            toogleBtn = new Button(
                    AppContext.getMessage(actionEnum),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            TaskListDepot.this.taskListActionControl
                                    .setPopupVisible(false);
                            if (taskList.isArchieved()) {
                                TaskListDepot.this.taskList
                                        .setStatus(StatusI18nEnum.Open.name());
                            } else {
                                TaskListDepot.this.taskList
                                        .setStatus(StatusI18nEnum.Archived.name());
                            }

                            final ProjectTaskListService taskListService = ApplicationContextUtil
                                    .getSpringBean(ProjectTaskListService.class);
                            taskListService.updateWithSession(
                                    TaskListDepot.this.taskList,
                                    AppContext.getUsername());

                            updateToogleButtonStatus();

                        }
                    });
            toogleBtn.setIcon(FontAwesome.TOGGLE_UP);
            toogleBtn.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.TASKS));
            toogleBtn.setStyleName("link");
            actionBtnLayout.addComponent(toogleBtn);

            final Button deleteBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            TaskListDepot.this.taskListActionControl
                                    .setPopupVisible(false);
                            ConfirmDialogExt.show(
                                    UI.getCurrent(),
                                    AppContext.getMessage(
                                            GenericI18Enum.DIALOG_DELETE_TITLE,
                                            SiteConfiguration.getSiteName()),
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
                                                final ConfirmDialog dialog) {
                                            if (dialog.isConfirmed()) {
                                                final ProjectTaskListService taskListService = ApplicationContextUtil
                                                        .getSpringBean(ProjectTaskListService.class);
                                                taskListService
                                                        .removeWithSession(
                                                                TaskListDepot.this.taskList
                                                                        .getId(),
                                                                AppContext
                                                                        .getUsername(),
                                                                AppContext
                                                                        .getAccountId());
                                                final Component parentComp = TaskListDepot.this
                                                        .getParent();
                                                if (parentComp instanceof CssLayout) {
                                                    ((CssLayout) parentComp)
                                                            .removeComponent(TaskListDepot.this);
                                                } else {
                                                    ((TaskGroupDisplayWidget) parentComp)
                                                            .removeRow(TaskListDepot.this);
                                                }

                                            }
                                        }
                                    });
                        }
                    });
            deleteBtn.setIcon(FontAwesome.TRASH_O);
            deleteBtn.setEnabled(CurrentProjectVariables
                    .canAccess(ProjectRolePermissionCollections.TASKS));
            deleteBtn.setStyleName("link");

            actionBtnLayout.addComponent(deleteBtn);
        }

        private void updateToogleButtonStatus() {
            Enum actionEnum = (taskList.isArchieved()) ? GenericI18Enum.BUTTON_REOPEN : GenericI18Enum.BUTTON_CLOSE;
            toogleBtn.setCaption(AppContext.getMessage(actionEnum));
        }

        private void updateSearchFilter() {
            this.taskDisplayComponent.setSearchCriteria(searchCriteria);
        }
    }
}
