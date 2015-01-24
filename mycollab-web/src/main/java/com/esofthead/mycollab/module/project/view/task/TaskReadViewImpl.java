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
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.*;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class TaskReadViewImpl extends AbstractPreviewItemComp2<SimpleTask>
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
                MyCollabResource.newResource(WebResourceIds._24_project_task));
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
        commentList.setMargin(true);

        historyList = new TaskHistoryList();
        historyList.setMargin(true);

        dateInfoComp = new DateInfoComp();
        addToSideBar(dateInfoComp);

        peopleInfoComp = new PeopleInfoComp();
        addToSideBar(peopleInfoComp);

        followerSheet = new ProjectFollowersComp<>(
                ProjectTypeConstants.TASK,
                ProjectRolePermissionCollections.TASKS);
        addToSideBar(followerSheet);

        timesheetComp = new TaskTimeLogSheet();
        addToSideBar(timesheetComp);
    }

    @Override
    protected void onPreviewItem() {
        previewLayout.clearTitleStyleName();
        if (beanItem.isCompleted()) {
            addLayoutStyleName(UIConstants.LINK_COMPLETED);
        } else if (beanItem.isPending()) {
            addLayoutStyleName(UIConstants.LINK_PENDING);
        } else if (beanItem.isOverdue()) {
            previewLayout.setTitleStyleName("headerNameOverdue");
        }

        if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_CLOSE));
            quickActionStatusBtn.setIcon(MyCollabResource
                    .newResource(WebResourceIds._16_project_closeTask));
        } else {
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_REOPEN));
            quickActionStatusBtn.setIcon(MyCollabResource
                    .newResource(WebResourceIds._16_project_reopenTask));

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
                if (beanItem.getStatus() != null
                        && beanItem.getStatus().equals(
                        StatusI18nEnum.Closed.name())) {
                    beanItem.setStatus(StatusI18nEnum.Open.name());
                    beanItem.setPercentagecomplete(0d);
                    TaskReadViewImpl.this
                            .removeLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_CLOSE));
                    quickActionStatusBtn.setIcon(MyCollabResource
                            .newResource(WebResourceIds._16_project_closeTask));
                } else {
                    beanItem.setStatus(StatusI18nEnum.Closed.name());
                    beanItem.setPercentagecomplete(100d);
                    TaskReadViewImpl.this
                            .addLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_REOPEN));
                    quickActionStatusBtn.setIcon(MyCollabResource
                            .newResource(WebResourceIds._16_project_reopenTask));
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
        TabsheetLazyLoadComp tabTaskDetail = new TabsheetLazyLoadComp();

        tabTaskDetail.addTab(commentList, AppContext
                        .getMessage(ProjectCommonI18nEnum.TAB_COMMENT, 0),
                MyCollabResource
                        .newResource(WebResourceIds._16_project_gray_comment));

        tabTaskDetail.addTab(historyList, AppContext
                        .getMessage(ProjectCommonI18nEnum.TAB_HISTORY),
                MyCollabResource
                        .newResource(WebResourceIds._16_project_gray_history));

        return tabTaskDetail;
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
                        MyCollabResource
                                .newResourceLink("icons/16/crm/task_group.png"));
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
            }
//			else if (Task.Field.parenttaskid.equalTo(propertyId)) {
//				return new SubTasksComp();
//			}
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
//	class SubTasksComp extends CustomField {
//		private static final long serialVersionUID = 1L;
//
//		private VerticalLayout tasksLayout;
//
//		SubTasksComp() {
//			tasksLayout = new VerticalLayout();
//			tasksLayout.setWidth("100%");
//		}
//
//		@Override
//		protected Component initContent() {
//			HorizontalLayout contentLayout = new HorizontalLayout();
//			contentLayout.addComponent(tasksLayout);
//			contentLayout.setExpandRatio(tasksLayout, 1.0f);
//
//			Button addNewTaskBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD),
//					new Button.ClickListener() {
//						private static final long serialVersionUID = 1L;
//
//						@Override
//						public void buttonClick(ClickEvent event) {
//							SimpleTask task = new SimpleTask();
//							task.setTasklistid(beanItem.getTasklistid());
//							task.setParenttaskid(beanItem.getId());
//							task.setPriority(TaskPriority.Medium.name());
//							EventBusFactory.getInstance().post(
//									new TaskEvent.GotoAdd(
//											TaskReadViewImpl.this, task));
//
//						}
//					});
//			addNewTaskBtn.setStyleName("link");
//			contentLayout.addComponent(addNewTaskBtn);
//
//			ProjectTaskService taskService = ApplicationContextUtil
//					.getSpringBean(ProjectTaskService.class);
//			List<SimpleTask> subTasks = taskService.findSubTasks(
//					beanItem.getId(), AppContext.getAccountId());
//			if (CollectionUtils.isNotEmpty(subTasks)) {
//				for (SimpleTask subTask : subTasks) {
//					tasksLayout.addComponent(generateSubTaskContent(subTask));
//				}
//			}
//			return contentLayout;
//		}
//
//		@Override
//		public Class getType() {
//			return Object.class;
//		}
//
//		private HorizontalLayout generateSubTaskContent(SimpleTask subTask) {
//			HorizontalLayout layout = new HorizontalLayout();
//			layout.setSpacing(true);
//
//			CheckBox checkBox = new CheckBox();
//			if (StatusI18nEnum.Closed.name().equals(subTask.getStatus())) {
//				checkBox.setValue(true);
//			}
//
//			checkBox.setEnabled(CurrentProjectVariables
//					.canWrite(ProjectRolePermissionCollections.TASKS));
//
//			layout.addComponent(checkBox);
//
//			Image assigneeRes = UserAvatarControlFactory
//					.createUserAvatarEmbeddedComponent(
//							subTask.getAssignUserAvatarId(), 16,
//							subTask.getAssignUserFullName());
//			layout.addComponent(assigneeRes);
//
//			String taskHtmlLink = String.format(
//					"<a href=\"%s\">[%s-%d] %s</a>", ProjectLinkGenerator
//							.generateTaskPreviewFullLink(
//									AppContext.getSiteUrl(),
//									subTask.getTaskkey(),
//									CurrentProjectVariables.getShortName()),
//					CurrentProjectVariables.getShortName(), subTask
//							.getTaskkey(), subTask.getTaskname());
//
//			Label taskLink = new Label(taskHtmlLink, ContentMode.HTML);
//			layout.addComponent(taskLink);
//			layout.setExpandRatio(taskLink, 1.0f);
//
//			if (subTask.getDeadline() != null) {
//				layout.addComponent(new Label(AppContext.formatDate(subTask
//						.getDeadline())));
//			}
//			return layout;
//		}
//
//	}

    class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        public void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withSpacing(true).withMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE));
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
