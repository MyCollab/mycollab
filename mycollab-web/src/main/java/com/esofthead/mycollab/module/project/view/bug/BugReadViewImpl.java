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
package com.esofthead.mycollab.module.project.view.bug;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DefaultProjectFormViewFieldFactory.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.tracker.BugStatusConstants;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormContainerViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormDateViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.ReadViewLayout;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.web.MyCollabResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class BugReadViewImpl extends AbstractPageView implements BugReadView,
		IBugCallbackStatusComp {

	private static final long serialVersionUID = 1L;
	private SimpleBug bug;
	private final BugPreviewForm previewForm;

	private HorizontalLayout bugWorkflowControl;

	public BugReadViewImpl() {
		super();

		this.setMargin(new MarginInfo(true, false, false, false));

		this.previewForm = new BugPreviewForm();
		this.addComponent(this.previewForm);
	}

	private void displayWorkflowControl() {
		if (BugStatusConstants.OPEN.equals(this.bug.getStatus())
				|| BugStatusConstants.REOPENNED.equals(this.bug.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			navButton.addButton(new Button("Start Progress",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							BugReadViewImpl.this.bug
									.setStatus(BugStatusConstants.INPROGRESS);
							final BugService bugService = ApplicationContextUtil
									.getSpringBean(BugService.class);
							bugService.updateWithSession(
									BugReadViewImpl.this.bug,
									AppContext.getUsername());
							BugReadViewImpl.this.displayWorkflowControl();
						}
					}));
			navButton.addButton(new Button("Resolved",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ResolvedInputWindow(
											BugReadViewImpl.this,
											BugReadViewImpl.this.bug));
						}
					}));

			navButton.addButton(new Button("Won't Fix",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new WontFixExplainWindow(
											BugReadViewImpl.this,
											BugReadViewImpl.this.bug));
						}
					}));
			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatusConstants.INPROGRESS.equals(this.bug.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			navButton.addButton(new Button("Stop Progress",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							BugReadViewImpl.this.bug
									.setStatus(BugStatusConstants.OPEN);
							final BugService bugService = ApplicationContextUtil
									.getSpringBean(BugService.class);
							bugService.updateWithSession(
									BugReadViewImpl.this.bug,
									AppContext.getUsername());
							BugReadViewImpl.this.displayWorkflowControl();
						}
					}));
			navButton.addButton(new Button("Resolved",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ResolvedInputWindow(
											BugReadViewImpl.this,
											BugReadViewImpl.this.bug));
						}
					}));
			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatusConstants.VERIFIED.equals(this.bug.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			final Button reopenBtn = new Button("Reopen",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ReOpenWindow(BugReadViewImpl.this,
											BugReadViewImpl.this.bug));
						}
					});
			navButton.addButton(reopenBtn);

			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatusConstants.RESOLVED.equals(this.bug.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			navButton.addButton(new Button("Reopen",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ReOpenWindow(BugReadViewImpl.this,
											BugReadViewImpl.this.bug));
						}
					}));
			navButton.addButton(new Button("Approve & Close",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ApproveInputWindow(
											BugReadViewImpl.this,
											BugReadViewImpl.this.bug));
						}
					}));
			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatusConstants.RESOLVED.equals(this.bug.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			final Button reopenBtn = new Button("Reopen",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ReOpenWindow(BugReadViewImpl.this,
											BugReadViewImpl.this.bug));
						}
					});
			reopenBtn.setStyleName(UIConstants.THEME_ROUND_BUTTON);
			navButton.addButton(reopenBtn);

			this.bugWorkflowControl.addComponent(navButton);
		}
		this.bugWorkflowControl.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));
	}

	@Override
	public SimpleBug getItem() {
		return this.bug;
	}

	@Override
	public void previewItem(final SimpleBug item) {
		this.bug = item;
		this.previewForm.setBean(item);
	}

	@Override
	public void refreshBugItem() {
		EventBus.getInstance().fireEvent(
				new BugEvent.GotoRead(BugReadViewImpl.this, this.bug.getId()));
	}

	private class BugPreviewForm extends AdvancedPreviewBeanForm<SimpleBug> {
		private static final long serialVersionUID = 1L;

		private BugHistoryList historyList;

		private BugFollowersSheet bugFollowersList;

		private BugTimeLogSheet bugTimeLogList;

		private BugRelatedField bugRelatedField;

		private CommentDisplay commentList;

		@Override
		public void setBean(SimpleBug bean) {
			this.setFormLayoutFactory(new FormLayoutFactory());
			this.setBeanFormFieldFactory(new PreviewFormFieldFactory(this));
			super.setBean(bean);
			BugReadViewImpl.this.displayWorkflowControl();

			onPreviewItem();
		}

		private void onPreviewItem() {
			commentList.loadComments(this.getBean().getId());
			historyList.loadHistory(this.getBean().getId());

			bugTimeLogList.setBean(this.getBean());
			bugTimeLogList.loadTimeValue();

			bugFollowersList.displayMonitorItems();
		}

		private class FormLayoutFactory implements IFormLayoutFactory {
			private static final long serialVersionUID = 1L;
			private GridFormLayoutHelper informationLayout;

			@Override
			public boolean attachField(final Object propertyId,
					final Field<?> field) {
				if (propertyId.equals("summary")) {
					this.informationLayout.addComponent(field, "Summary", 0, 0,
							2, "100%");
				} else if (propertyId.equals("description")) {
					this.informationLayout.addComponent(field, "Description",
							0, 1, 2, "100%");
				} else if (propertyId.equals("environment")) {
					this.informationLayout.addComponent(field, "Environment",
							0, 2, 2, "100%");
				} else if (propertyId.equals("status")) {
					this.informationLayout.addComponent(field, "Status", 0, 3);
				} else if (propertyId.equals("priority")) {
					this.informationLayout
							.addComponent(field, "Priority", 1, 3);
				} else if (propertyId.equals("severity")) {
					this.informationLayout
							.addComponent(field, "Severity", 0, 4);
				} else if (propertyId.equals("resolution")) {
					this.informationLayout.addComponent(field, "Resolution", 1,
							4);
				} else if (propertyId.equals("duedate")) {
					this.informationLayout
							.addComponent(field, "Due Date", 0, 5);
				} else if (propertyId.equals("createdtime")) {
					this.informationLayout.addComponent(field, "Created Time",
							1, 5);
				} else if (propertyId.equals("loguserFullName")) {
					this.informationLayout.addComponent(field, "Logged by", 0,
							6);
				} else if (propertyId.equals("assignuserFullName")) {
					this.informationLayout
							.addComponent(
									field,
									LocalizationHelper
											.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
									1, 6);
				} else if (propertyId.equals("milestoneName")) {
					this.informationLayout.addComponent(field,
							LocalizationHelper
									.getMessage(TaskI18nEnum.FORM_PHASE_FIELD),
							0, 7, 2, "100%");
				} else if (propertyId.equals("components")) {
					this.informationLayout.addComponent(field, "Components", 0,
							8, 2, "100%");
				} else if (propertyId.equals("affectedVersions")) {
					this.informationLayout.addComponent(field,
							"Affected Versions", 0, 9, 2, "100%");
				} else if (propertyId.equals("fixedVersions")) {
					this.informationLayout.addComponent(field,
							"Fixed Versions", 0, 10, 2, "100%");
				} else if (propertyId.equals("id")) {
					this.informationLayout.addComponent(field, "Attachments",
							0, 11, 2, "100%");
				} else {
					return false;
				}

				return true;
			}

			private ComponentContainer createBottomLayout() {
				final TabSheet tabBugDetail = new TabSheet();
				tabBugDetail.setWidth("100%");

				commentList = new CommentDisplay(CommentType.PRJ_BUG,
						CurrentProjectVariables.getProjectId(), true, true,
						BugRelayEmailNotificationAction.class);
				commentList.setMargin(true);
				tabBugDetail.addTab(commentList, "Comments", MyCollabResource
						.newResource("icons/16/project/gray/comment.png"));

				historyList = new BugHistoryList(
						BugReadViewImpl.this.bug.getId());
				historyList.setMargin(true);
				tabBugDetail.addTab(historyList, "History", MyCollabResource
						.newResource("icons/16/project/gray/history.png"));

				bugRelatedField = new BugRelatedField(BugReadViewImpl.this.bug);
				tabBugDetail.addTab(bugRelatedField, "Related Bugs",
						MyCollabResource
								.newResource("icons/16/project/gray/bug.png"));

				bugFollowersList = new BugFollowersSheet(
						BugReadViewImpl.this.bug);
				tabBugDetail
						.addTab(bugFollowersList,
								"Followers",
								MyCollabResource
										.newResource("icons/16/project/gray/follow.png"));

				bugTimeLogList = new BugTimeLogSheet(BugReadViewImpl.this.bug);
				tabBugDetail.addTab(bugTimeLogList, "Time", MyCollabResource
						.newResource("icons/16/project/gray/time.png"));
				return tabBugDetail;
			}

			@Override
			public Layout getLayout() {
				final ReadViewLayout bugAddLayout = new ReadViewLayout(
						"[Issue " + BugReadViewImpl.this.bug.getBugkey()
								+ "#]: "
								+ BugReadViewImpl.this.bug.getSummary(),
						MyCollabResource
								.newResource("icons/24/project/bug.png"));

				if (BugStatusConstants.VERIFIED.equals(BugReadViewImpl.this.bug
						.getStatus())) {
					bugAddLayout.addTitleStyleName(UIConstants.LINK_COMPLETED);
				} else if (BugReadViewImpl.this.bug.isOverdue()) {
					bugAddLayout.addTitleStyleName(UIConstants.LINK_OVERDUE);
				}

				final Button createAccountBtn = new Button("Create",
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								EventBus.getInstance().fireEvent(
										new BugEvent.GotoAdd(this, null));
							}
						});
				createAccountBtn.setEnabled(CurrentProjectVariables
						.canWrite(ProjectRolePermissionCollections.BUGS));
				createAccountBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
				createAccountBtn.setIcon(MyCollabResource
						.newResource("icons/16/addRecord.png"));

				final HorizontalLayout headerRight = new HorizontalLayout();
				headerRight.addComponent(createAccountBtn);
				bugAddLayout.addHeaderRight(headerRight);

				final HorizontalLayout topPanel = new HorizontalLayout();
				topPanel.setSpacing(false);
				topPanel.setMargin(true);
				topPanel.addStyleName("control-buttons");
				topPanel.setWidth("100%");

				final HorizontalLayout buttonControls = new HorizontalLayout();
				buttonControls.addStyleName("edit-btn");
				buttonControls.setSpacing(true);

				Button backBtn;
				backBtn = new Button(null, new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new BugEvent.GotoList(this, null));
					}
				});
				backBtn.setIcon(MyCollabResource
						.newResource("icons/16/back.png"));
				backBtn.setDescription("Back to list");
				backBtn.setStyleName("link");
				topPanel.addComponent(backBtn);
				topPanel.setComponentAlignment(backBtn, Alignment.MIDDLE_LEFT);

				final Button assignBtn = new Button("Assign",
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								UI.getCurrent().addWindow(
										new AssignBugWindow(
												BugReadViewImpl.this,
												BugReadViewImpl.this.bug));
							}
						});
				assignBtn.setEnabled(CurrentProjectVariables
						.canWrite(ProjectRolePermissionCollections.BUGS));
				assignBtn.setIcon(MyCollabResource
						.newResource("icons/16/assign.png"));

				assignBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
				buttonControls.addComponent(assignBtn);
				buttonControls.setComponentAlignment(assignBtn,
						Alignment.MIDDLE_CENTER);

				final Button editBtn = new Button("Edit",
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								EventBus.getInstance().fireEvent(
										new BugEvent.GotoEdit(
												BugReadViewImpl.this,
												BugReadViewImpl.this.bug));
							}
						});
				editBtn.setIcon(MyCollabResource
						.newResource("icons/16/edit_white.png"));
				editBtn.setEnabled(CurrentProjectVariables
						.canWrite(ProjectRolePermissionCollections.BUGS));
				editBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
				buttonControls.addComponent(editBtn);
				buttonControls.setComponentAlignment(editBtn,
						Alignment.MIDDLE_CENTER);

				final Button deleteBtn = new Button("Delete",
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								ConfirmDialogExt.show(
										UI.getCurrent(),
										LocalizationHelper
												.getMessage(
														GenericI18Enum.DELETE_DIALOG_TITLE,
														SiteConfiguration
																.getSiteName()),
										LocalizationHelper
												.getMessage(GenericI18Enum.CONFIRM_DELETE_RECORD_DIALOG_MESSAGE),
										LocalizationHelper
												.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
										LocalizationHelper
												.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
										new ConfirmDialog.Listener() {
											private static final long serialVersionUID = 1L;

											@Override
											public void onClose(
													final ConfirmDialog dialog) {
												if (dialog.isConfirmed()) {
													final BugService bugService = ApplicationContextUtil
															.getSpringBean(BugService.class);
													bugService
															.removeWithSession(
																	BugReadViewImpl.this.bug
																			.getId(),
																	AppContext
																			.getUsername(),
																	AppContext
																			.getAccountId());
													EventBus.getInstance()
															.fireEvent(
																	new BugEvent.GotoDashboard(
																			BugReadViewImpl.this,
																			null));
												}
											}
										});
							}
						});
				deleteBtn.setIcon(MyCollabResource
						.newResource("icons/16/delete2.png"));
				deleteBtn.setEnabled(CurrentProjectVariables
						.canAccess(ProjectRolePermissionCollections.BUGS));
				deleteBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
				buttonControls.addComponent(deleteBtn);
				buttonControls.setComponentAlignment(deleteBtn,
						Alignment.MIDDLE_CENTER);
				buttonControls.setMargin(false);

				topPanel.addComponent(buttonControls);
				topPanel.setComponentAlignment(buttonControls,
						Alignment.MIDDLE_CENTER);
				topPanel.setExpandRatio(buttonControls, 1);

				BugReadViewImpl.this.bugWorkflowControl = new HorizontalLayout();
				BugReadViewImpl.this.bugWorkflowControl.setMargin(false);
				BugReadViewImpl.this.bugWorkflowControl
						.addStyleName("workflow-controls");
				topPanel.addComponent(BugReadViewImpl.this.bugWorkflowControl);
				topPanel.setComponentAlignment(
						BugReadViewImpl.this.bugWorkflowControl,
						Alignment.MIDDLE_RIGHT);

				bugAddLayout.addTopControls(topPanel);

				this.informationLayout = new GridFormLayoutHelper(2, 12,
						"100%", "167px", Alignment.MIDDLE_LEFT);
				this.informationLayout.getLayout().addStyleName(
						"colored-gridlayout");
				this.informationLayout.getLayout().setMargin(false);
				this.informationLayout.getLayout().setWidth("100%");
				bugAddLayout.addBody(this.informationLayout.getLayout());

				bugAddLayout.addBottomControls(this.createBottomLayout());
				return bugAddLayout;
			}
		}

		private class PreviewFormFieldFactory extends
				AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {
			private static final long serialVersionUID = 1L;

			public PreviewFormFieldFactory(GenericBeanForm<SimpleBug> form) {
				super(form);
			}

			@Override
			protected Field<?> onCreateField(final Object propertyId) {
				if (propertyId.equals("duedate")) {
					return new FormDateViewField(
							BugReadViewImpl.this.bug.getDuedate());
				} else if (propertyId.equals("createdtime")) {
					return new FormDateViewField(
							BugReadViewImpl.this.bug.getCreatedtime());
				} else if (propertyId.equals("assignuserFullName")) {
					return new ProjectUserFormLinkField(
							BugReadViewImpl.this.bug.getAssignuser(),
							BugReadViewImpl.this.bug.getAssignUserAvatarId(),
							BugReadViewImpl.this.bug.getAssignuserFullName());
				} else if (propertyId.equals("loguserFullName")) {
					return new ProjectUserFormLinkField(
							BugReadViewImpl.this.bug.getLogby(),
							BugReadViewImpl.this.bug.getLoguserAvatarId(),
							BugReadViewImpl.this.bug.getLoguserFullName());
				} else if (propertyId.equals("id")) {
					return new ProjectFormAttachmentDisplayField(
							bug.getProjectid(),
							AttachmentType.PROJECT_BUG_TYPE,
							BugReadViewImpl.this.bug.getId());
				} else if (propertyId.equals("components")) {
					final List<Component> components = BugReadViewImpl.this.bug
							.getComponents();
					if (components != null && components.size() > 0) {
						final FormContainerViewField componentContainer = new FormContainerViewField();
						for (final Component component : BugReadViewImpl.this.bug
								.getComponents()) {
							final Button componentLink = new Button(
									component.getComponentname(),
									new Button.ClickListener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void buttonClick(
												final ClickEvent event) {
											EventBus.getInstance()
													.fireEvent(
															new BugComponentEvent.GotoRead(
																	BugReadViewImpl.this,
																	component
																			.getId()));
										}
									});
							componentContainer.addComponentField(componentLink);
							componentLink.setStyleName("link");
						}
						componentContainer
								.setStyleName(UIConstants.FORM_CONTAINER_VIEW);
						return componentContainer;
					} else {
						return new FormViewField("");
					}
				} else if (propertyId.equals("affectedVersions")) {
					final List<Version> affectedVersions = BugReadViewImpl.this.bug
							.getAffectedVersions();
					if (affectedVersions != null && affectedVersions.size() > 0) {
						final FormContainerViewField componentContainer = new FormContainerViewField();
						for (final Version version : BugReadViewImpl.this.bug
								.getAffectedVersions()) {
							final Button versionLink = new Button(
									version.getVersionname(),
									new Button.ClickListener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void buttonClick(
												final ClickEvent event) {
											EventBus.getInstance()
													.fireEvent(
															new BugVersionEvent.GotoRead(
																	BugReadViewImpl.this,
																	version.getId()));
										}
									});
							componentContainer.addComponentField(versionLink);
							versionLink.setStyleName("link");
						}
						return componentContainer;
					} else {
						return new FormViewField("");
					}
				} else if (propertyId.equals("fixedVersions")) {
					final List<Version> fixedVersions = BugReadViewImpl.this.bug
							.getFixedVersions();
					if (fixedVersions != null && fixedVersions.size() > 0) {
						final FormContainerViewField componentContainer = new FormContainerViewField();
						for (final Version version : BugReadViewImpl.this.bug
								.getFixedVersions()) {
							final Button versionLink = new Button(
									version.getVersionname(),
									new Button.ClickListener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void buttonClick(
												final ClickEvent event) {
											EventBus.getInstance()
													.fireEvent(
															new BugVersionEvent.GotoRead(
																	BugReadViewImpl.this,
																	version.getId()));
										}
									});
							componentContainer.addComponentField(versionLink);
							versionLink.setStyleName("link");
						}
						return componentContainer;
					} else {
						return new FormViewField("");
					}

				} else if (propertyId.equals("milestoneName")) {
					if (BugReadViewImpl.this.bug.getMilestoneid() != null) {
						final FormLinkViewField phaseLink = new FormLinkViewField(
								BugReadViewImpl.this.bug.getMilestoneName(),
								new Button.ClickListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(
											final ClickEvent event) {
										EventBus.getInstance()
												.fireEvent(
														new MilestoneEvent.GotoRead(
																BugReadViewImpl.this,
																BugReadViewImpl.this.bug
																		.getMilestoneid()));
									}
								},
								MyCollabResource
										.newResource("icons/16/project/milestone.png"));
						return phaseLink;
					} else {
						return new FormViewField("");
					}

				} else if (propertyId.equals("environment")) {
					return new DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField(
							BugReadViewImpl.this.bug.getEnvironment());
				} else if (propertyId.equals("description")) {
					return new DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField(
							BugReadViewImpl.this.bug.getDescription());
				} else if (propertyId.equals("priority")) {
					if (StringUtils.isNotNullOrEmpty(BugReadViewImpl.this.bug
							.getPriority())) {
						final Resource iconPriority = ProjectResources
								.getIconResource12ByPriority(BugReadViewImpl.this.bug
										.getPriority());
						final Image iconEmbedded = new Image(null, iconPriority);
						final Label lbPriority = new Label(
								BugReadViewImpl.this.bug.getPriority());

						final FormContainerHorizontalViewField containerField = new FormContainerHorizontalViewField();
						containerField.addComponentField(iconEmbedded);
						containerField.addComponentField(lbPriority);
						containerField.getLayout().setExpandRatio(lbPriority,
								1.0f);
						return containerField;
					}
				} else if (propertyId.equals("severity")) {
					if (StringUtils.isNotNullOrEmpty(BugReadViewImpl.this.bug
							.getSeverity())) {
						final Resource iconPriority = ProjectResources
								.getIconResource12BySeverity(BugReadViewImpl.this.bug
										.getSeverity());
						final Image iconEmbedded = new Image();
						iconEmbedded.setSource(iconPriority);
						final Label lbPriority = new Label(
								BugReadViewImpl.this.bug.getSeverity());

						final FormContainerHorizontalViewField containerField = new FormContainerHorizontalViewField();
						containerField.addComponentField(iconEmbedded);
						containerField.addComponentField(lbPriority);
						containerField.getLayout().setExpandRatio(lbPriority,
								1.0f);
						return containerField;
					}
				}
				return null;
			}
		}
	}

}
