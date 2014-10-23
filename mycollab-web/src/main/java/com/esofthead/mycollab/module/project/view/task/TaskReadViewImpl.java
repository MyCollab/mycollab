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

import java.util.GregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.ui.components.DefaultProjectFormViewFieldFactory.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.ui.components.ProjectFollowersComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class TaskReadViewImpl extends AbstractPreviewItemComp2<SimpleTask>
		implements TaskReadView {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(TaskReadViewImpl.class);

	private CommentDisplay commentList;

	private TaskHistoryList historyList;

	private ProjectFollowersComp<SimpleTask> followerSheet;

	private DateInfoComp dateInfoComp;

	private TaskTimeLogSheet timesheet;

	private PeopleInfoComp peopleInfoComp;

	private Button quickActionStatusBtn;

	public TaskReadViewImpl() {
		super(AppContext.getMessage(TaskI18nEnum.VIEW_DETAIL_TITLE),
				MyCollabResource.newResource("icons/24/project/task.png"));
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
	public void addViewListener(ViewListener listener) {

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

		followerSheet = new ProjectFollowersComp<SimpleTask>(
				ProjectTypeConstants.TASK,
				ProjectRolePermissionCollections.TASKS);
		addToSideBar(followerSheet);

		timesheet = new TaskTimeLogSheet();
		addToSideBar(timesheet);
	}

	@Override
	protected void onPreviewItem() {
		previewLayout.clearTitleStyleName();
		if (beanItem.getPercentagecomplete() != null
				&& 100d == beanItem.getPercentagecomplete()) {
			addLayoutStyleName(UIConstants.LINK_COMPLETED);
		} else {
			if ("Pending".equals(beanItem.getStatus())) {
				addLayoutStyleName(UIConstants.LINK_PENDING);
			} else if ((beanItem.getEnddate() != null && (beanItem.getEnddate()
					.before(new GregorianCalendar().getTime())))
					|| (beanItem.getActualenddate() != null && (beanItem
							.getActualenddate().before(new GregorianCalendar()
							.getTime())))
					|| (beanItem.getDeadline() != null && (beanItem
							.getDeadline().before(new GregorianCalendar()
							.getTime())))) {
				addLayoutStyleName(UIConstants.LINK_OVERDUE);
			}
		}

		if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
			quickActionStatusBtn.setCaption(AppContext
					.getMessage(GenericI18Enum.BUTTON_CLOSE));
			quickActionStatusBtn.setIcon(MyCollabResource
					.newResource("icons/16/project/closeTask.png"));
		} else {
			quickActionStatusBtn.setCaption(AppContext
					.getMessage(GenericI18Enum.BUTTON_REOPEN));
			quickActionStatusBtn.setIcon(MyCollabResource
					.newResource("icons/16/project/reopenTask.png"));

		}

		commentList.loadComments("" + beanItem.getId());

		historyList.loadHistory(beanItem.getId());

		followerSheet.displayFollowers(beanItem);

		peopleInfoComp.displayEntryPeople(beanItem);
		dateInfoComp.displayEntryDateTime(beanItem);
		timesheet.displayTime(beanItem);
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getTaskname();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleTask> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleTask>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleTask> initBeanFormFieldFactory() {
		return new ReadFormFieldFactory(previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		ProjectPreviewFormControlsGenerator<SimpleTask> taskPreviewForm = new ProjectPreviewFormControlsGenerator<SimpleTask>(
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
							.newResource("icons/16/project/closeTask.png"));
				} else {
					beanItem.setStatus(StatusI18nEnum.Closed.name());
					beanItem.setPercentagecomplete(100d);
					TaskReadViewImpl.this
							.addLayoutStyleName(UIConstants.LINK_COMPLETED);
					quickActionStatusBtn.setCaption(AppContext
							.getMessage(GenericI18Enum.BUTTON_REOPEN));
					quickActionStatusBtn.setIcon(MyCollabResource
							.newResource("icons/16/project/reopenTask.png"));
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
		final TabsheetLazyLoadComp tabTaskDetail = new TabsheetLazyLoadComp();
		tabTaskDetail.setWidth("100%");

		tabTaskDetail.addTab(commentList, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_COMMENT),
				MyCollabResource
						.newResource("icons/16/project/gray/comment.png"));

		tabTaskDetail.addTab(historyList, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_HISTORY),
				MyCollabResource
						.newResource("icons/16/project/gray/history.png"));

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

			if (propertyId.equals("assignuser")) {
				return new ProjectUserFormLinkField(beanItem.getAssignuser(),
						beanItem.getAssignUserAvatarId(),
						beanItem.getAssignUserFullName());
			} else if (propertyId.equals("taskListName")) {
				return new DefaultViewField(
						beanItem.getTaskListName());
			} else if (propertyId.equals("startdate")) {
				return new DefaultViewField(
						AppContext.formatDate(beanItem.getStartdate()));
			} else if (propertyId.equals("enddate")) {
				return new DefaultViewField(
						AppContext.formatDate(beanItem.getEnddate()));
			} else if (propertyId.equals("actualstartdate")) {
				return new DefaultViewField(
						AppContext.formatDate(beanItem.getActualstartdate()));
			} else if (propertyId.equals("actualenddate")) {
				return new DefaultViewField(
						AppContext.formatDate(beanItem.getActualenddate()));
			} else if (propertyId.equals("deadline")) {
				return new DefaultViewField(
						AppContext.formatDate(beanItem.getDeadline()));
			} else if (propertyId.equals("tasklistid")) {
				return new LinkViewField(
						beanItem.getTaskListName(),
						ProjectLinkBuilder.generateTaskGroupPreviewFullLink(
								beanItem.getProjectid(),
								beanItem.getTasklistid()),
						MyCollabResource
								.newResourceLink("icons/16/crm/task_group.png"));
			} else if (propertyId.equals("id")) {
				return new ProjectFormAttachmentDisplayField(
						beanItem.getProjectid(),
						AttachmentType.PROJECT_TASK_TYPE, beanItem.getId());
			} else if (propertyId.equals("priority")) {
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
			} else if (propertyId.equals("notes")) {
				return new RichTextViewField(beanItem.getNotes());
			}
			return null;
		}
	}

	class PeopleInfoComp extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		public void displayEntryPeople(ValuedBean bean) {
			this.removeAllComponents();
			this.setSpacing(true);
			this.setMargin(new MarginInfo(false, false, false, true));

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
				log.error("Can not build user link {} ",
						BeanUtility.printBeanObj(bean));
			}

			this.addComponent(layout);

		}
	}
}
