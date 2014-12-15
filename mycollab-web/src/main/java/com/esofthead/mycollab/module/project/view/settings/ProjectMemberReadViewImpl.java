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

package com.esofthead.mycollab.module.project.view.settings;

import java.util.Arrays;

import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MVerticalLayout;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.view.AbstractProjectPageView;
import com.esofthead.mycollab.module.project.view.bug.BugTableDisplay;
import com.esofthead.mycollab.module.project.view.bug.BugTableFieldDef;
import com.esofthead.mycollab.module.project.view.standup.StandupReportListDisplay;
import com.esofthead.mycollab.module.project.view.task.TaskTableDisplay;
import com.esofthead.mycollab.module.project.view.task.TaskTableFieldDef;
import com.esofthead.mycollab.module.project.view.user.ProjectActivityStreamPagedList;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UserLinkViewField;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberReadViewImpl extends AbstractProjectPageView
		implements ProjectMemberReadView {

	private static final long serialVersionUID = 1L;

	protected SimpleProjectMember beanItem;
	protected AdvancedPreviewBeanForm<SimpleProjectMember> previewForm;

	protected UserActivityStream userActivityComp;

	protected UserTaskComp userTaskComp;

	protected UserBugComp userBugComp;

	protected UserStandupReportDepot standupComp;
	private ComponentContainer bottomPanel;

	public ProjectMemberReadViewImpl() {
		super(AppContext.getMessage(ProjectMemberI18nEnum.VIEW_READ_TITLE),
				"user.png");

		previewForm = initPreviewForm();

		ComponentContainer actionControls = createButtonControls();
		if (actionControls != null) {
			this.addHeaderRightContent(actionControls);
		}
		previewForm.setWidth("100%");
		previewForm.setStyleName("member-preview-form");
		this.addComponent(previewForm);

		this.getBody().addStyleName("member-preview");
	}

	@Override
	public SimpleProjectMember getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleProjectMember> getPreviewFormHandlers() {
		return previewForm;
	}

	private void initLayout() {
		initRelatedComponents();
		ComponentContainer newBottomPanel = createBottomPanel();
		newBottomPanel.addStyleName("member-preview-bottom");
		if (bottomPanel != null && bottomPanel.getParent() == this.getBody()) {
			replaceComponent(bottomPanel, newBottomPanel);
		} else {
			addComponent(newBottomPanel);
		}
		bottomPanel = newBottomPanel;
	}

	public void previewItem(final SimpleProjectMember item) {
		this.beanItem = item;
		initLayout();

		previewForm.setFormLayoutFactory(initFormLayoutFactory());
		previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
		previewForm.setBean(item);

		onPreviewItem();
	}

	public SimpleProjectMember getBeanItem() {
		return beanItem;
	}

	public AdvancedPreviewBeanForm<SimpleProjectMember> getPreviewForm() {
		return previewForm;
	}

	protected AdvancedPreviewBeanForm<SimpleProjectMember> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleProjectMember>();
	}

	protected ComponentContainer createButtonControls() {
		return new ProjectPreviewFormControlsGenerator<SimpleProjectMember>(
				previewForm)
				.createButtonControls(
						ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
						ProjectRolePermissionCollections.USERS);
	}

	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
		tabContainer.setWidth("100%");
		tabContainer.addTab(this.userActivityComp, "Activities",
				MyCollabResource
						.newResource("icons/16/project/gray/user_feed.png"));
		tabContainer.addTab(this.standupComp, "Stand Ups", MyCollabResource
				.newResource("icons/16/project/gray/standup.png"));
		tabContainer.addTab(this.userTaskComp, "Task Assignments",
				MyCollabResource
						.newResource(WebResourceIds._16_project_gray_task));
		tabContainer.addTab(this.userBugComp, "Bug Assignments",
				MyCollabResource
						.newResource(WebResourceIds._16_project_gray_bug));
		return tabContainer;
	}

	private void initRelatedComponents() {
		userActivityComp = new UserActivityStream();
		userTaskComp = new UserTaskComp();
		userBugComp = new UserBugComp();
		standupComp = new UserStandupReportDepot();

	}

	private void onPreviewItem() {
		userActivityComp.displayActivityStream();
		userTaskComp.displayActiveTasksOnly();
		userBugComp.displayOpenBugs();
		standupComp.displayStandupReports();
	}

	protected String initFormTitle() {
		return beanItem.getMemberFullName();
	}

	protected IFormLayoutFactory initFormLayoutFactory() {
		return new ProjectMemberReadLayoutFactory();
	}

	protected AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> initBeanFormFieldFactory() {
		return new ProjectMemberFormFieldFactory(previewForm);
	}

	protected class ProjectMemberReadLayoutFactory implements
			IFormLayoutFactory {
		private static final long serialVersionUID = 8833593761607165873L;

		@Override
		public ComponentContainer getLayout() {
			CssLayout memberBlock = new CssLayout();
			memberBlock.addStyleName("member-block");

			HorizontalLayout blockContent = new HorizontalLayout();
			Image memberAvatar = UserAvatarControlFactory
					.createUserAvatarEmbeddedComponent(
							beanItem.getMemberAvatarId(), 100);
			blockContent.addComponent(memberAvatar);

			MVerticalLayout memberInfo = new MVerticalLayout().withStyleName(
					"member-info").withMargin(
					new MarginInfo(false, false, false, true));

			Label memberLink = new Label(beanItem.getMemberFullName());
			memberLink.setWidth("100%");
			memberLink.addStyleName("member-name");

			memberInfo.addComponent(memberLink);

			String memerRoleLinkPrefix = "<a href=\""
					+ AppContext.getSiteUrl()
					+ GenericLinkUtils.URL_PREFIX_PARAM
					+ ProjectLinkGenerator.generateRolePreviewLink(
							beanItem.getProjectid(),
							beanItem.getProjectRoleId()) + "\"";
			Label memberRole = new Label();
			memberRole.setContentMode(ContentMode.HTML);
			memberRole.setStyleName("member-role");
			if (beanItem.getIsadmin() != null
					&& beanItem.getIsadmin() == Boolean.TRUE
					|| beanItem.getProjectroleid() == null) {
				memberRole.setValue(memerRoleLinkPrefix
						+ "style=\"color: #B00000;\">" + "Project Admin"
						+ "</a>");
			} else {
				memberRole.setValue(memerRoleLinkPrefix
						+ "style=\"color:gray;font-size:12px;\">"
						+ beanItem.getRoleName() + "</a>");
			}
			memberRole.setSizeUndefined();
			memberInfo.addComponent(memberRole);

			Label memberEmailLabel = new Label("<a href='mailto:"
					+ beanItem.getUsername() + "'>" + beanItem.getUsername()
					+ "</a>", ContentMode.HTML);
			memberEmailLabel.addStyleName("member-email");
			memberEmailLabel.setWidth("100%");
			memberInfo.addComponent(memberEmailLabel);

			Label memberSinceLabel = new Label("Member since: "
					+ AppContext.formatDate(beanItem.getJoindate()));
			memberSinceLabel.addStyleName("member-email");
			memberSinceLabel.setWidth("100%");
			memberInfo.addComponent(memberSinceLabel);

			if (RegisterStatusConstants.SENT_VERIFICATION_EMAIL.equals(beanItem
					.getStatus())) {
				final VerticalLayout waitingNotLayout = new VerticalLayout();
				Label infoStatus = new Label(
						AppContext
								.getMessage(ProjectMemberI18nEnum.WAITING_ACCEPT_INVITATION));
				infoStatus.addStyleName("member-email");
				waitingNotLayout.addComponent(infoStatus);

				ButtonLink resendInvitationLink = new ButtonLink(
						"Resend Invitation", new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(ClickEvent event) {
								ProjectMemberMapper projectMemberMapper = ApplicationContextUtil
										.getSpringBean(ProjectMemberMapper.class);
								beanItem.setStatus(RegisterStatusConstants.VERIFICATING);
								projectMemberMapper
										.updateByPrimaryKeySelective(beanItem);
								waitingNotLayout.removeAllComponents();
								Label statusEmail = new Label(
										AppContext
												.getMessage(ProjectMemberI18nEnum.SENDING_EMAIL_INVITATION));
								statusEmail.addStyleName("member-email");
								waitingNotLayout.addComponent(statusEmail);
							}
						});
				resendInvitationLink.setStyleName("link");
				resendInvitationLink.addStyleName("member-email");
				waitingNotLayout.addComponent(resendInvitationLink);
				memberInfo.addComponent(waitingNotLayout);
			} else if (RegisterStatusConstants.ACTIVE.equals(beanItem
					.getStatus())) {
				Label lastAccessTimeLbl = new Label("Logged in "
						+ DateTimeUtils.getPrettyDateValue(
								beanItem.getLastAccessTime(),
								AppContext.getUserLocale()));
				lastAccessTimeLbl.addStyleName("member-email");
				memberInfo.addComponent(lastAccessTimeLbl);
			} else if (RegisterStatusConstants.VERIFICATING.equals(beanItem
					.getStatus())) {
				Label infoStatus = new Label(
						AppContext
								.getMessage(ProjectMemberI18nEnum.WAITING_ACCEPT_INVITATION));
				infoStatus.addStyleName("member-email");
				memberInfo.addComponent(infoStatus);
			}

			String bugStatus = beanItem.getNumOpenBugs() + " open bug";
			if (beanItem.getNumOpenBugs() > 1) {
				bugStatus += "s";
			}

			String taskStatus = beanItem.getNumOpenTasks() + " open task";
			if (beanItem.getNumOpenTasks() > 1) {
				taskStatus += "s";
			}

			Label memberWorkStatus = new Label(bugStatus + " - " + taskStatus);
			memberInfo.addComponent(memberWorkStatus);
			memberInfo.setWidth("100%");

			blockContent.addComponent(memberInfo);
			blockContent.setExpandRatio(memberInfo, 1.0f);
			blockContent.setWidth("100%");

			memberBlock.addComponent(blockContent);
			return memberBlock;
		}

		@Override
		public void attachField(Object propertyId, Field<?> field) {

		}

	}

	protected class ProjectMemberFormFieldFactory extends
			AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> {

		private static final long serialVersionUID = 1L;

		public ProjectMemberFormFieldFactory(
				GenericBeanForm<SimpleProjectMember> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {
			if (propertyId.equals("projectroleid")) {
				if (attachForm.getBean().getIsadmin() != null
						&& attachForm.getBean().getIsadmin() == Boolean.FALSE) {
					LinkViewField roleLink = new LinkViewField(attachForm
							.getBean().getRoleName(),
							ProjectLinkBuilder.generateRolePreviewFullLink(
									attachForm.getBean().getProjectid(),
									attachForm.getBean().getProjectroleid()),
							null);
					return roleLink;
				} else {
					return new DefaultViewField("Project Admin");
				}
			} else if (propertyId.equals("username")) {
				return new UserLinkViewField(
						attachForm.getBean().getUsername(), attachForm
								.getBean().getMemberAvatarId(), attachForm
								.getBean().getMemberFullName());
			}
			return null;
		}
	}

	protected class UserTaskComp extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		private PopupButton taskListFilterControl;
		private TaskTableDisplay taskDisplay;
		private Label taskLabel;

		private TaskSearchCriteria taskSearchCriteria;

		public UserTaskComp() {
			super();

			this.taskDisplay = new TaskTableDisplay(TaskTableFieldDef.id,
					Arrays.asList(TaskTableFieldDef.taskname,
							TaskTableFieldDef.startdate,
							TaskTableFieldDef.duedate,
							TaskTableFieldDef.percentagecomplete));

			this.taskDisplay.addTableListener(new TableClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void itemClick(final TableClickEvent event) {
					final SimpleTask task = (SimpleTask) event.getData();
					if ("taskname".equals(event.getFieldName())) {
						EventBusFactory.getInstance().post(
								new TaskEvent.GotoRead(
										ProjectMemberReadViewImpl.this, task
												.getId()));
					} else if ("closeTask".equals(event.getFieldName())
							|| "reopenTask".equals(event.getFieldName())
							|| "pendingTask".equals(event.getFieldName())
							|| "reopenTask".equals(event.getFieldName())
							|| "deleteTask".equals(event.getFieldName())) {

						UserTaskComp.this.taskDisplay
								.setSearchCriteria(UserTaskComp.this.taskSearchCriteria);
					}
				}
			});

			this.initHeader();
			this.addComponent(this.taskDisplay);
		}

		private void initHeader() {
			final HorizontalLayout headerLayout = new HorizontalLayout();
			headerLayout.setMargin(true);
			headerLayout.setStyleName("comp-header");

			taskLabel = new Label("Active Tasks&nbsp;&nbsp;", ContentMode.HTML);
			taskLabel.setStyleName("h2");
			headerLayout.addComponent(taskLabel);
			headerLayout
					.setComponentAlignment(taskLabel, Alignment.MIDDLE_LEFT);

			this.taskListFilterControl = new PopupButton("");
			this.taskListFilterControl
					.addStyleName(UIConstants.THEME_BLANK_LINK);
			this.taskListFilterControl.setIcon(MyCollabResource
					.newResource(WebResourceIds._12_project_task_filter));
			final VerticalLayout filterBtnLayout = new VerticalLayout();
			filterBtnLayout.setMargin(true);
			filterBtnLayout.setSpacing(true);
			filterBtnLayout.setWidth("200px");

			final Button allTasksFilterBtn = new Button("All Tasks",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UserTaskComp.this.taskListFilterControl
									.setPopupVisible(false);
							UserTaskComp.this.taskLabel
									.setValue("All Tasks&nbsp;&nbsp;");
							UserTaskComp.this.displayAllTasks();
						}
					});
			allTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(allTasksFilterBtn);

			final Button activeTasksFilterBtn = new Button("Active Tasks Only",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UserTaskComp.this.taskListFilterControl
									.setPopupVisible(false);
							UserTaskComp.this.taskLabel
									.setValue("Active Tasks&nbsp;&nbsp;");
							UserTaskComp.this.displayActiveTasksOnly();
						}
					});
			activeTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(activeTasksFilterBtn);

			final Button pendingTasksFilterBtn = new Button(
					"Pending Tasks Only", new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UserTaskComp.this.taskListFilterControl
									.setPopupVisible(false);
							UserTaskComp.this.taskLabel
									.setValue("Pending Tasks&nbsp;&nbsp;");
							UserTaskComp.this.displayPendingTasksOnly();
						}
					});
			pendingTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(pendingTasksFilterBtn);

			final Button archievedTasksFilterBtn = new Button(
					"Archived Tasks Only", new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UserTaskComp.this.taskListFilterControl
									.setPopupVisible(false);
							UserTaskComp.this.taskLabel
									.setValue("Archived Tasks&nbsp;&nbsp;");
							UserTaskComp.this.displayInActiveTasks();
						}
					});
			archievedTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(archievedTasksFilterBtn);
			this.taskListFilterControl.setContent(filterBtnLayout);
			headerLayout.addComponent(this.taskListFilterControl);
			this.addComponent(headerLayout);
		}

		private TaskSearchCriteria createBaseSearchCriteria() {
			final TaskSearchCriteria criteria = new TaskSearchCriteria();
			criteria.setProjectid(new NumberSearchField(CurrentProjectVariables
					.getProjectId()));
			criteria.setAssignUser(new StringSearchField(previewForm.getBean()
					.getUsername()));
			return criteria;
		}

		public void displayActiveTasksOnly() {
			this.taskSearchCriteria = this.createBaseSearchCriteria();
			this.taskSearchCriteria.setStatuses(new SetSearchField<String>(
					SearchField.AND, new String[] { "Open" }));
			this.taskDisplay.setSearchCriteria(this.taskSearchCriteria);
		}

		private void displayPendingTasksOnly() {
			this.taskSearchCriteria = this.createBaseSearchCriteria();
			this.taskSearchCriteria.setStatuses(new SetSearchField<String>(
					SearchField.AND, new String[] { "Pending" }));
			this.taskDisplay.setSearchCriteria(this.taskSearchCriteria);
		}

		private void displayAllTasks() {
			this.taskSearchCriteria = this.createBaseSearchCriteria();
			this.taskSearchCriteria.setStatuses(new SetSearchField<String>(
					SearchField.AND,
					new String[] { "Open", "Pending", "Closed" }));
			this.taskDisplay.setSearchCriteria(this.taskSearchCriteria);
		}

		private void displayInActiveTasks() {
			this.taskSearchCriteria = this.createBaseSearchCriteria();
			this.taskSearchCriteria.setStatuses(new SetSearchField<String>(
					SearchField.AND, new String[] { "Closed" }));
			this.taskDisplay.setSearchCriteria(this.taskSearchCriteria);
		}
	}

	protected class UserBugComp extends VerticalLayout {
		private static final long serialVersionUID = 1L;
		private PopupButton bugActionControl;
		private BugTableDisplay bugDisplay;
		private Label bugLabel;

		public UserBugComp() {
			super();

			this.bugDisplay = new BugTableDisplay(BugTableFieldDef.action,
					Arrays.asList(BugTableFieldDef.summary,
							BugTableFieldDef.severity,
							BugTableFieldDef.resolution,
							BugTableFieldDef.duedate));

			this.bugDisplay.addTableListener(new TableClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void itemClick(final TableClickEvent event) {
					final SimpleBug bug = (SimpleBug) event.getData();
					if ("summary".equals(event.getFieldName())) {
						EventBusFactory.getInstance().post(
								new BugEvent.GotoRead(
										ProjectMemberReadViewImpl.this, bug
												.getId()));
					}
				}
			});

			this.initHeader();

			this.addComponent(this.bugDisplay);
		}

		private void initHeader() {
			final HorizontalLayout headerLayout = new HorizontalLayout();
			headerLayout.setMargin(true);
			headerLayout.setStyleName("comp-header");

			bugLabel = new Label("Open Bugs&nbsp;&nbsp;", ContentMode.HTML);
			bugLabel.setStyleName("h2");
			headerLayout.addComponent(bugLabel);
			headerLayout.setComponentAlignment(bugLabel, Alignment.MIDDLE_LEFT);

			this.bugActionControl = new PopupButton("");
			this.bugActionControl.addStyleName(UIConstants.THEME_BLANK_LINK);
			this.bugActionControl.setIcon(MyCollabResource
					.newResource(WebResourceIds._12_project_task_filter));
			headerLayout.addComponent(this.bugActionControl);

			final VerticalLayout actionBtnLayout = new VerticalLayout();
			actionBtnLayout.setMargin(true);
			actionBtnLayout.setSpacing(true);
			actionBtnLayout.setWidth("200px");
			this.bugActionControl.setContent(actionBtnLayout);

			final Button openBugBtn = new Button("Open Bugs",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UserBugComp.this.bugActionControl
									.setPopupVisible(false);
							UserBugComp.this.bugLabel
									.setValue("Open Bugs&nbsp;&nbsp;");
							UserBugComp.this.displayOpenBugs();
						}
					});
			openBugBtn.setEnabled(CurrentProjectVariables
					.canRead(ProjectRolePermissionCollections.BUGS));
			openBugBtn.setStyleName("link");
			actionBtnLayout.addComponent(openBugBtn);

			final Button pendingBugBtn = new Button("Resolved Bugs",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UserBugComp.this.bugActionControl
									.setPopupVisible(false);
							UserBugComp.this.bugLabel
									.setValue("Resolved Bugs&nbsp;&nbsp;");
							UserBugComp.this.displayResolvedBugs();
						}
					});
			pendingBugBtn.setEnabled(CurrentProjectVariables
					.canRead(ProjectRolePermissionCollections.BUGS));
			pendingBugBtn.setStyleName("link");
			actionBtnLayout.addComponent(pendingBugBtn);

			final Button closeBugBtn = new Button("Verified Bugs",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UserBugComp.this.bugActionControl
									.setPopupVisible(false);
							UserBugComp.this.bugLabel
									.setValue("Verified Bugs&nbsp;&nbsp;");
							UserBugComp.this.displayClosedBugs();
						}
					});
			closeBugBtn.setEnabled(CurrentProjectVariables
					.canWrite(ProjectRolePermissionCollections.BUGS));
			closeBugBtn.setStyleName("link");
			actionBtnLayout.addComponent(closeBugBtn);
			this.addComponent(headerLayout);
		}

		private BugSearchCriteria createBugSearchCriteria() {
			final BugSearchCriteria criteria = new BugSearchCriteria();
			criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
					.getProjectId()));
			criteria.setAssignuser(new StringSearchField(previewForm.getBean()
					.getUsername()));
			return criteria;
		}

		public void displayOpenBugs() {
			final BugSearchCriteria criteria = this.createBugSearchCriteria();
			criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
					new String[] { BugStatus.InProgress.name(),
							BugStatus.Open.name(), BugStatus.ReOpened.name() }));
			this.bugDisplay.setSearchCriteria(criteria);
		}

		private void displayResolvedBugs() {
			final BugSearchCriteria criteria = this.createBugSearchCriteria();
			criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
					new String[] { BugStatus.Resolved.name() }));
			this.bugDisplay.setSearchCriteria(criteria);
		}

		private void displayClosedBugs() {
			final BugSearchCriteria criteria = this.createBugSearchCriteria();
			criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
					new String[] { BugStatus.Verified.name() }));
			this.bugDisplay.setSearchCriteria(criteria);
		}
	}

	protected class UserStandupReportDepot extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		private StandupReportListDisplay standupReportListDisplay;

		public UserStandupReportDepot() {
			super();

			standupReportListDisplay = new StandupReportListDisplay();
			this.addComponent(standupReportListDisplay);
		}

		public void displayStandupReports() {
			final StandupReportSearchCriteria searchCriteria = new StandupReportSearchCriteria();
			searchCriteria.setProjectId(new NumberSearchField(
					CurrentProjectVariables.getProjectId()));
			searchCriteria.setLogBy(new StringSearchField(previewForm.getBean()
					.getUsername()));
			standupReportListDisplay.setSearchCriteria(searchCriteria);
		}
	}

	protected class UserActivityStream extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		private ProjectActivityStreamPagedList activityStreamList;

		public UserActivityStream() {
			super();
			this.setMargin(true);

			activityStreamList = new ProjectActivityStreamPagedList();
		}

		public void displayActivityStream() {
			this.removeAllComponents();
			this.addComponent(this.activityStreamList);
			ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
			searchCriteria.setModuleSet(new SetSearchField<String>(
					SearchField.AND, new String[] { ModuleNameConstants.PRJ }));
			searchCriteria.setCreatedUser(new StringSearchField(
					SearchField.AND, previewForm.getBean().getUsername()));
			searchCriteria.setExtraTypeIds(new SetSearchField<Integer>(
					CurrentProjectVariables.getProjectId()));
			searchCriteria.setSaccountid(new NumberSearchField(AppContext
					.getAccountId()));
			this.activityStreamList.setSearchCriteria(searchCriteria);
		}
	}
}
