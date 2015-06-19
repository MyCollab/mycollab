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

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.*;
import com.esofthead.mycollab.module.project.ui.form.ProjectItemViewField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TaskGroupReadViewImpl extends AbstractPreviewItemComp<SimpleTaskList> implements TaskGroupReadView {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TaskGroupReadViewImpl.class);

    private CommentDisplay commentList;
    private TaskGroupHistoryLogList historyList;
    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private TaskGroupTimeLogSheet timeLogSheet;

    public TaskGroupReadViewImpl() {
        super(AppContext.getMessage(TaskGroupI18nEnum.FORM_VIEW_TASKGROUP_TITLE),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK_LIST));
    }

    @Override
    public HasPreviewFormHandlers<SimpleTaskList> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        commentList = new CommentDisplay(ProjectTypeConstants.TASK_LIST,
                CurrentProjectVariables.getProjectId(),
                ProjectTaskGroupRelayEmailNotificationAction.class);
        commentList.setWidth("100%");
        historyList = new TaskGroupHistoryLogList();
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        timeLogSheet = new TaskGroupTimeLogSheet();
        addToSideBar(dateInfoComp, peopleInfoComp, timeLogSheet);
    }

    @Override
    protected void onPreviewItem() {
        commentList.loadComments("" + beanItem.getId());
        historyList.loadHistory(beanItem.getId());

        peopleInfoComp.displayEntryPeople(beanItem);
        dateInfoComp.displayEntryDateTime(beanItem);
        timeLogSheet.displayTime(beanItem);
    }

    @Override
    protected String initFormTitle() {
        if (StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
            this.addLayoutStyleName(UIConstants.LINK_COMPLETED);
        }
        return beanItem.getName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleTaskList> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.TASK_LIST,
                TaskGroupDefaultFormLayoutFactory.getForm(),
                TaskList.Field.name.name());
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return (new ProjectPreviewFormControlsGenerator<>(
                previewForm))
                .createButtonControls(
                        ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.ASSIGN_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
                        ProjectRolePermissionCollections.TASKS);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        final TabSheetLazyLoadComponent tabContainer = new TabSheetLazyLoadComponent();
        tabContainer.addTab(commentList, AppContext.getMessage(GenericI18Enum.TAB_COMMENT), FontAwesome.COMMENTS);
        tabContainer.addTab(historyList, AppContext.getMessage(GenericI18Enum.TAB_HISTORY), FontAwesome.HISTORY);
        return tabContainer;
    }

    @Override
    public SimpleTaskList getItem() {
        return beanItem;
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList> initBeanFormFieldFactory() {
        return new AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList>(
                previewForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (TaskList.Field.milestoneid.equalTo(propertyId)) {
                    return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid() + "", beanItem.getMilestoneName());
                } else if (TaskList.Field.owner.equalTo(propertyId)) {
                    return new ProjectUserFormLinkField(beanItem.getOwner(),
                            beanItem.getOwnerAvatarId(),
                            beanItem.getOwnerFullName());
                } else if (TaskList.Field.description.equalTo(propertyId)) {
                    return new DefaultViewField(beanItem.getDescription(),
                            ContentMode.HTML);
                } else if (TaskList.Field.groupindex.equalTo(propertyId)) {
                    return new SubTasksField();
                }
                return null;
            }
        };
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.TASK_LIST;
    }

    private class SubTasksField extends CustomField {
        private TaskSearchCriteria searchCriteria;
        private DefaultBeanPagedList<ProjectTaskService, TaskSearchCriteria, SimpleTask> assignmentsList;

        @Override
        protected Component initContent() {
            MVerticalLayout layout = new MVerticalLayout().withMargin(false);

            MHorizontalLayout header = new MHorizontalLayout();

            final CheckBox openSelection = new CheckBox("Open", true);
            openSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if (openSelection.getValue()) {
                        searchCriteria.setStatuses(new SetSearchField<>(new String[]{StatusI18nEnum.Open.name()}));
                    } else {
                        searchCriteria.setStatuses(null);
                    }
                    updateSearchStatus();
                }
            });

            final CheckBox overdueSelection = new CheckBox("Overdue", false);
            overdueSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if (overdueSelection.getValue()) {
                        searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS(), DateSearchField.LESSTHAN));
                    } else {
                        searchCriteria.setDueDate(null);
                    }
                    updateSearchStatus();
                }
            });
            header.with(openSelection, overdueSelection);

            assignmentsList = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectTaskService.class), new
                    AssignmentRowDisplay(), 10);
            assignmentsList.setControlStyle("borderlessControl");

            layout.with(header, assignmentsList);
            searchCriteria = new TaskSearchCriteria();
            searchCriteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setStatuses(new SetSearchField<>(new String[]{StatusI18nEnum.Open.name()}));
            searchCriteria.setTaskListId(new NumberSearchField(beanItem.getId()));
            updateSearchStatus();
            return layout;
        }

        void updateSearchStatus() {
            assignmentsList.setSearchCriteria(searchCriteria);
        }

        @Override
        public Class getType() {
            return Integer.class;
        }
    }

    private static class AssignmentRowDisplay implements AbstractBeanPagedList.RowDisplayHandler<SimpleTask> {
        @Override
        public Component generateRow(AbstractBeanPagedList host, SimpleTask task, int rowIndex) {
            Label lbl = new Label(buildDivLine(task).write(), ContentMode.HTML);
            if (task.isOverdue()) {
                lbl.addStyleName("overdue");
            } else if (task.isCompleted()) {
                lbl.addStyleName("completed");
            }
            return lbl;
        }

        private Div buildDivLine(SimpleTask task) {
            Div div = new Div().setCSSClass("project-tableless");
            div.appendChild(buildItemValue(task), buildAssigneeValue(task), buildLastUpdateTime(task));
            return div;
        }

        private Div buildItemValue(SimpleTask task) {
            Div div = new Div();
            String linkName = String.format("[#%d] - %s", task.getTaskkey(), task.getTaskname());
            Text image = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml());
            String uid = UUID.randomUUID().toString();
            A taskLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateTaskPreviewFullLink(task.getTaskkey(),
                    CurrentProjectVariables.getShortName())).appendText(linkName);
            if (task.isCompleted()) {
                taskLink.setCSSClass("completed");
            } else if (task.isOverdue()) {
                taskLink.setCSSClass("overdue");
            } else if (task.isPending()) {
                taskLink.setCSSClass("pending");
            }

            taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.TASK, task.getId() + ""));
            taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), taskLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            return div;
        }

        private Div buildAssigneeValue(SimpleTask task) {
            if (task.getAssignuser() == null) {
                return new Div().setCSSClass("column200");
            }
            String uid = UUID.randomUUID().toString();
            Div div = new Div();
            Img userAvatar = new Img("", StorageManager.getAvatarLink(
                    task.getAssignUserAvatarId(), 16));
            A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    task.getProjectid(), task.getAssignuser()));

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, task.getAssignuser()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            userLink.appendText(task.getAssignUserFullName());

            div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));

            return div.setCSSClass("column200");
        }

        private Div buildLastUpdateTime(SimpleTask task) {
            Div div = new Div();
            div.appendChild(new Text(AppContext.formatPrettyTime(task.getLastupdatedtime()))).setTitle(AppContext
                    .getMessage(DayI18nEnum.LAST_UPDATED_ON, AppContext
                            .formatDateTime(task.getLastupdatedtime())));
            return div.setCSSClass("column100");
        }
    }

    private static class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        public void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " + AppContext
                            .getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(AppContext
                                .getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(bean, "createduser");
                String createdUserAvatarId = (String) PropertyUtils
                        .getProperty(bean, "createdUserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "createdUserFullName");

                ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName,
                        createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(bean, "owner");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(bean, "ownerAvatarId");
                String assignUserDisplayName = (String) PropertyUtils.getProperty(bean, "ownerFullName");

                ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName,
                        assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ", BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);

        }
    }
}
