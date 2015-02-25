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

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.*;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class TaskReadViewImpl extends AbstractPreviewItemComp<SimpleTask>
        implements TaskReadView {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory
            .getLogger(TaskReadViewImpl.class);

    private CommentDisplay commentList;

    private TaskHistoryList historyList;

    private ProjectFollowersComp<SimpleTask> followerSheet;

    private DateInfoComp dateInfoComp;

    private TaskTimeLogSheet timesheetComp;

    private PeopleInfoComp peopleInfoComp;

    private Button quickActionStatusBtn;

    public TaskReadViewImpl() {
        super(AppContext.getMessage(TaskI18nEnum.VIEW_DETAIL_TITLE),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK), new TaskPreviewFormLayout());
    }

    @Override
    public SimpleTask getItem() {
        return beanItem;
    }

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    @Override
    public HasPreviewFormHandlers<SimpleTask> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        commentList = new CommentDisplay(CommentType.PRJ_TASK,
                CurrentProjectVariables.getProjectId(), true, true,
                ProjectTaskRelayEmailNotificationAction.class);
        historyList = new TaskHistoryList();
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        followerSheet = new ProjectFollowersComp<>(ProjectTypeConstants.TASK, ProjectRolePermissionCollections.TASKS);
        timesheetComp = new TaskTimeLogSheet();

        addToSideBar(dateInfoComp, peopleInfoComp, followerSheet, timesheetComp);
    }

    @Override
    protected void onPreviewItem() {
        ((TaskPreviewFormLayout) previewLayout).displayTaskHeader(beanItem);
        if (beanItem.isCompleted()) {
            addLayoutStyleName(UIConstants.LINK_COMPLETED);
        } else if (beanItem.isPending()) {
            addLayoutStyleName(UIConstants.LINK_PENDING);
        } else if (beanItem.isOverdue()) {
            addLayoutStyleName("headerNameOverdue");
        }

        if (!beanItem.isCompleted()) {
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_CLOSE));
            quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
        } else {
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_REOPEN));
            quickActionStatusBtn.setIcon(FontAwesome.CIRCLE_O_NOTCH);

        }

        commentList.loadComments("" + beanItem.getId());
        historyList.loadHistory(beanItem.getId());
        followerSheet.displayFollowers(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        dateInfoComp.displayEntryDateTime(beanItem);
        timesheetComp.displayTime(beanItem);
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getTaskname();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleTask> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.TASK,
                TaskDefaultFormLayoutFactory.getForm(),
                Task.Field.taskname.name());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleTask> initBeanFormFieldFactory() {
        return new ReadFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleTask> taskPreviewForm = new ProjectPreviewFormControlsGenerator<>(
                previewForm);
        final HorizontalLayout topPanel = taskPreviewForm
                .createButtonControls(
                        ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.ASSIGN_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
                        ProjectRolePermissionCollections.TASKS);

        quickActionStatusBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (beanItem.isCompleted()) {
                    beanItem.setStatus(StatusI18nEnum.Open.name());
                    beanItem.setPercentagecomplete(0d);
                    TaskReadViewImpl.this
                            .removeLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_CLOSE));
                    quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
                } else {
                    beanItem.setStatus(StatusI18nEnum.Closed.name());
                    beanItem.setPercentagecomplete(100d);
                    TaskReadViewImpl.this
                            .addLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_REOPEN));
                    quickActionStatusBtn.setIcon(FontAwesome.CIRCLE_O_NOTCH);
                }

                ProjectTaskService service = ApplicationContextUtil
                        .getSpringBean(ProjectTaskService.class);
                service.updateWithSession(beanItem, AppContext.getUsername());

            }
        });

        quickActionStatusBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        taskPreviewForm.insertToControlBlock(quickActionStatusBtn);

        if (!CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.TASKS)) {
            quickActionStatusBtn.setEnabled(false);
        }

        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        TabSheetLazyLoadComponent tabTaskDetail = new TabSheetLazyLoadComponent();
        tabTaskDetail.addTab(commentList, AppContext.getMessage(ProjectCommonI18nEnum.TAB_COMMENT, 0), FontAwesome.COMMENTS);
        tabTaskDetail.addTab(historyList, AppContext.getMessage(ProjectCommonI18nEnum.TAB_HISTORY), FontAwesome.HISTORY);
        return tabTaskDetail;
    }

    private static class TaskPreviewFormLayout extends ReadViewLayout {
        private Label titleLbl;

        void displayTaskHeader(SimpleTask task) {
            if (task.getParenttaskid() == null) {
                MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");
                titleLbl = new Label(task.getTaskname());
                titleLbl.setStyleName("headerName");
                header.with(titleLbl).expand(titleLbl);
                this.addHeader(header);
            } else {
                MVerticalLayout header = new MVerticalLayout().withMargin(false);
                Label parentLabel = new Label(buildParentTaskLink(task), ContentMode.HTML);

                titleLbl = new Label(task.getTaskname());
                titleLbl.setStyleName("headerName");
                MHorizontalLayout wrapLayout = new MHorizontalLayout().withMargin(false).withSpacing(false).with(new
                        Label("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", ContentMode.HTML), titleLbl);
                header.with(parentLabel, wrapLayout);
                this.addHeader(header);
            }

            if (task.isCompleted()) {
                titleLbl.addStyleName("completed");
            } else if (task.isPending()) {
                titleLbl.addStyleName("pending");
            } else if (task.isOverdue()) {
                titleLbl.setStyleName("headerNameOverdue");
            }
        }

        private String buildParentTaskLink(SimpleTask task) {
            String linkName = String.format("[%s-%d] %s", CurrentProjectVariables.getShortName(), task.getParentTaskKey(),
                    task.getParentTaskName());
            A taskLink = new A().setHref(ProjectLinkBuilder.generateTaskPreviewFullLink(task.getParentTaskKey(),
                    CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
            return taskLink.write();
        }

        @Override
        public void clearTitleStyleName() {
            this.titleLbl.setStyleName("headerName");
        }

        @Override
        public void addTitleStyleName(final String styleName) {
            this.titleLbl.addStyleName(styleName);
        }

        @Override
        public void setTitleStyleName(final String styleName) {
            this.titleLbl.setStyleName(styleName);
        }

        @Override
        public void removeTitleStyleName(final String styleName) {
            this.titleLbl.removeStyleName(styleName);
        }

        @Override
        public void setTitle(final String title) {
        }
    }

    private class ReadFormFieldFactory extends
            AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
        private static final long serialVersionUID = 1L;

        public ReadFormFieldFactory(GenericBeanForm<SimpleTask> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {

            if (Task.Field.assignuser.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getAssignuser(),
                        beanItem.getAssignUserAvatarId(),
                        beanItem.getAssignUserFullName());
            } else if (SimpleTask.Field.taskListName.equalTo(propertyId)) {
                return new DefaultViewField(beanItem.getTaskListName());
            } else if (Task.Field.startdate.equalTo(propertyId)) {
                return new DefaultViewField(AppContext.formatDate(beanItem
                        .getStartdate()));
            } else if (Task.Field.enddate.equalTo(propertyId)) {
                return new DefaultViewField(AppContext.formatDate(beanItem
                        .getEnddate()));
            } else if (Task.Field.actualstartdate.equalTo(propertyId)) {
                return new DefaultViewField(AppContext.formatDate(beanItem
                        .getActualstartdate()));
            } else if (Task.Field.actualenddate.equalTo(propertyId)) {
                return new DefaultViewField(AppContext.formatDate(beanItem
                        .getActualenddate()));
            } else if (Task.Field.deadline.equalTo(propertyId)) {
                return new DefaultViewField(AppContext.formatDate(beanItem
                        .getDeadline()));
            } else if (Task.Field.tasklistid.equalTo(propertyId)) {
                return new LinkViewField(beanItem.getTaskListName(),
                        ProjectLinkBuilder.generateTaskGroupPreviewFullLink(
                                beanItem.getProjectid(),
                                beanItem.getTasklistid()),
                        ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK_LIST));
            } else if (Task.Field.id.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(
                        beanItem.getProjectid(),
                        AttachmentType.PROJECT_TASK_TYPE, beanItem.getId());
            } else if (Task.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    final Resource iconPriority = new ExternalResource(
                            ProjectResources
                                    .getIconResourceLink12ByTaskPriority(beanItem
                                            .getPriority()));
                    final Embedded iconEmbedded = new Embedded(null,
                            iconPriority);
                    final Label lbPriority = new Label(AppContext.getMessage(
                            TaskPriority.class, beanItem.getPriority()));

                    final ContainerHorizontalViewField containerField = new ContainerHorizontalViewField();
                    containerField.addComponentField(iconEmbedded);
                    containerField.getLayout().setComponentAlignment(
                            iconEmbedded, Alignment.MIDDLE_LEFT);
                    lbPriority.setWidth("220px");
                    containerField.addComponentField(lbPriority);
                    containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
                    return containerField;
                }
            } else if (Task.Field.notes.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getNotes());
            } else if (Task.Field.parenttaskid.equalTo(propertyId)) {
                return new SubTasksComp();
            }
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private class SubTasksComp extends CustomField {
        private static final long serialVersionUID = 1L;

        private VerticalLayout tasksLayout;

        @Override
        protected Component initContent() {
            MHorizontalLayout contentLayout = new MHorizontalLayout().withWidth("100%");

            tasksLayout = new MVerticalLayout().withWidth("100%").withMargin(new MarginInfo(false,
                    true, true, false));
            contentLayout.addComponent(tasksLayout);
            contentLayout.setExpandRatio(tasksLayout, 1.0f);

            Button addNewTaskBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(ClickEvent event) {
                            SimpleTask task = new SimpleTask();
                            task.setTasklistid(beanItem.getTasklistid());
                            task.setParenttaskid(beanItem.getId());
                            task.setPriority(TaskPriority.Medium.name());
                            EventBusFactory.getInstance().post(
                                    new TaskEvent.GotoAdd(
                                            TaskReadViewImpl.this, task));

                        }
                    });
            addNewTaskBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            addNewTaskBtn.setIcon(FontAwesome.PLUS);
            addNewTaskBtn.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.TASKS));

            contentLayout.addComponent(addNewTaskBtn);

            ProjectTaskService taskService = ApplicationContextUtil
                    .getSpringBean(ProjectTaskService.class);
            List<SimpleTask> subTasks = taskService.findSubTasks(
                    beanItem.getId(), AppContext.getAccountId());
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
            MHorizontalLayout layout = new MHorizontalLayout();

            final CheckBox checkBox = new CheckBox("", subTask.isCompleted());
            checkBox.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.TASKS));
            layout.addComponent(checkBox);

            final Label taskLink = new Label(buildTaskLink(subTask), ContentMode.HTML);
            taskLink.addStyleName("wordWrap");
            layout.addComponent(taskLink);
            layout.setExpandRatio(taskLink, 1.0f);

            checkBox.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    Boolean selectedFlag = checkBox.getValue();
                    ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    if (selectedFlag) {
                        subTask.setStatus(StatusI18nEnum.Closed.name());
                        subTask.setPercentagecomplete(100d);
                    } else {
                        subTask.setStatus(StatusI18nEnum.Open.name());
                        subTask.setPercentagecomplete(0d);
                    }
                    taskService.updateSelectiveWithSession(subTask, AppContext.getUsername());
                    taskLink.setValue(buildTaskLink(subTask));
                }
            });
            return layout;
        }

        private String buildTaskLink(SimpleTask subTask) {
            String linkName = String.format("[%s-%d] %s", CurrentProjectVariables.getShortName(), subTask.getTaskkey(), subTask
                    .getTaskname());
            A taskLink = new A().setHref(ProjectLinkBuilder.generateTaskPreviewFullLink(subTask.getTaskkey(),
                    CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
            if (subTask.isCompleted()) {
                taskLink.setCSSClass("completed");
            } else if (subTask.isOverdue()) {
                taskLink.setCSSClass("overdue");
            } else if (subTask.isPending()) {
                taskLink.setCSSClass("pending");
            }

            String uid = UUID.randomUUID().toString();
            taskLink.setId("tag" + uid);
            String arg17 = "'" + uid + "'";
            String arg18 = "'" + ProjectTypeConstants.TASK + "'";
            String arg19 = "'" + subTask.getId() + "'";
            String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
            String arg21 = "'" + AppContext.getAccountId() + "'";
            String arg22 = "'" + AppContext.getSiteUrl() + "'";
            String arg23 = AppContext.getSession().getTimezone();
            String arg24 = "'" + AppContext.getUserLocale().toString() + "'";

            String mouseOverFunc = String.format(
                    "return overIt(%s,%s,%s,%s,%s,%s,%s,%s);", arg17, arg18, arg19,
                    arg20, arg21, arg22, arg23, arg24);
            taskLink.setAttribute("onmouseover", mouseOverFunc);

            String avatarLink = StorageManager.getAvatarLink(subTask.getAssignUserAvatarId(), 16);
            Img avatarImg = new Img(subTask.getAssignUserFullName(), avatarLink).setTitle(subTask.getAssignUserFullName());

            if (subTask.getDeadline() != null) {
                Div deadline = new Div().appendChild(new Text(String.format(" - %s: %s", AppContext.getMessage
                        (TaskI18nEnum.FORM_DEADLINE), AppContext.formatDate(subTask.getDeadline()))))
                        .setStyle("color:gray; display:inline");

                return new DivLessFormatter().appendChild(avatarImg, DivLessFormatter.EMPTY_SPACE(), taskLink, deadline,
                        DivLessFormatter.EMPTY_SPACE(),
                        TooltipHelper.buildDivTooltipEnable(uid)).write();
            } else {
                return new DivLessFormatter().appendChild(avatarImg, DivLessFormatter.EMPTY_SPACE(), taskLink, DivLessFormatter
                                .EMPTY_SPACE(),
                        TooltipHelper.buildDivTooltipEnable(uid)).write();
            }
        }

    }

    private class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        public void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " +
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(
                        AppContext
                                .getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(
                        bean, "logby");
                String createdUserAvatarId = (String) PropertyUtils
                        .getProperty(bean, "logByAvatarId");
                String createdUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "logByFullName");

                UserLink createdUserLink = new UserLink(createdUserName,
                        createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(
                        AppContext
                                .getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(
                        bean, "assignuser");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(
                        bean, "assignUserAvatarId");
                String assignUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "assignUserFullName");

                UserLink assignUserLink = new UserLink(assignUserName,
                        assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ",
                        BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);

        }
    }
}
