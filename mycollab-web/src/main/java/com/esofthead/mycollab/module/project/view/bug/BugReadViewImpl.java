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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.ui.components.ProjectFollowersComp;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DateViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.I18nFormViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class BugReadViewImpl extends AbstractPreviewItemComp2<SimpleBug>
		implements BugReadView, IBugCallbackStatusComp {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(BugReadViewImpl.class);

	private HorizontalLayout bugWorkflowControl;

	private BugHistoryList historyList;

	private ProjectFollowersComp<SimpleBug> bugFollowersList;

	private BugTimeLogSheet bugTimeLogList;

	private BugRelatedField bugRelatedField;

	private CommentDisplay commentList;

	private DateInfoComp dateInfoComp;

	private PeopleInfoComp peopleInfoComp;

	private ProjectPreviewFormControlsGenerator<SimpleBug> bugPreviewFormControls;

	public BugReadViewImpl() {
		super(AppContext.getMessage(BugI18nEnum.VIEW_READ_TITLE),
				MyCollabResource.newResource("icons/24/project/bug.png"));
	}

	private void displayWorkflowControl() {
		if (BugStatus.Open.name().equals(this.beanItem.getStatus())
				|| BugStatus.ReOpened.name().equals(this.beanItem.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			final Button startProgressBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_START_PROGRESS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							beanItem.setStatus(BugStatus.InProgress.name());
							final BugService bugService = ApplicationContextUtil
									.getSpringBean(BugService.class);
							bugService.updateSelectiveWithSession(beanItem,
									AppContext.getUsername());
							displayWorkflowControl();
						}
					});
			startProgressBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(startProgressBtn);

			final Button resolveBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ResolvedInputWindow(
											BugReadViewImpl.this, beanItem));
						}
					});
			resolveBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(resolveBtn);

			final Button wontFixBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_WONTFIX),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new WontFixExplainWindow(
											BugReadViewImpl.this, beanItem));
						}
					});
			wontFixBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(wontFixBtn);
			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatus.InProgress.name()
				.equals(this.beanItem.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			final Button stopProgressBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_STOP_PROGRESS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							beanItem.setStatus(BugStatus.Open.name());
							final BugService bugService = ApplicationContextUtil
									.getSpringBean(BugService.class);
							bugService.updateSelectiveWithSession(beanItem,
									AppContext.getUsername());
							displayWorkflowControl();
						}
					});
			stopProgressBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(stopProgressBtn);

			final Button resolveBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ResolvedInputWindow(
											BugReadViewImpl.this, beanItem));
						}
					});
			resolveBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(resolveBtn);
			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatus.Verified.name().equals(this.beanItem.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			final Button reopenBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ReOpenWindow(BugReadViewImpl.this,
											beanItem));
						}
					});
			reopenBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(reopenBtn);

			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatus.Resolved.name().equals(this.beanItem.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			final Button reopenBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ReOpenWindow(BugReadViewImpl.this,
											beanItem));
						}
					});
			reopenBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(reopenBtn);

			final Button approveNCloseBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_APPROVE_CLOSE),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ApproveInputWindow(
											BugReadViewImpl.this, beanItem));
						}
					});
			approveNCloseBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(approveNCloseBtn);
			this.bugWorkflowControl.addComponent(navButton);
		} else if (BugStatus.Resolved.name().equals(this.beanItem.getStatus())) {
			this.bugWorkflowControl.removeAllComponents();
			final ButtonGroup navButton = new ButtonGroup();
			final Button reopenBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							UI.getCurrent().addWindow(
									new ReOpenWindow(BugReadViewImpl.this,
											beanItem));
						}
					});
			reopenBtn.setStyleName(UIConstants.THEME_BROWN_LINK);
			navButton.addButton(reopenBtn);

			this.bugWorkflowControl.addComponent(navButton);
		}
		this.bugWorkflowControl.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));
	}

	@Override
	public SimpleBug getItem() {
		return this.beanItem;
	}

	@Override
	public void previewItem(final SimpleBug item) {
		super.previewItem(item);
		displayWorkflowControl();
		this.previewLayout.clearTitleStyleName();
		if (BugStatus.Verified.name().equals(this.beanItem.getStatus())) {
			this.previewLayout.addTitleStyleName(UIConstants.LINK_COMPLETED);
		} else if (this.beanItem.isOverdue()) {
			this.previewLayout.setTitleStyleName("headerNameOverdue");
		}
	}

	@Override
	public void refreshBugItem() {
		EventBusFactory.getInstance().post(
				new BugEvent.GotoRead(BugReadViewImpl.this, this.beanItem
						.getId()));
	}

	@Override
	protected void initRelatedComponents() {
		commentList = new CommentDisplay(CommentType.PRJ_BUG,
				CurrentProjectVariables.getProjectId(), true, true,
				BugRelayEmailNotificationAction.class);
		commentList.setMargin(true);

		historyList = new BugHistoryList();

		bugRelatedField = new BugRelatedField();

		dateInfoComp = new DateInfoComp();
		addToSideBar(dateInfoComp);

		peopleInfoComp = new PeopleInfoComp();
		addToSideBar(peopleInfoComp);

		bugFollowersList = new ProjectFollowersComp<SimpleBug>(
				ProjectTypeConstants.BUG, ProjectRolePermissionCollections.BUGS);
		addToSideBar(bugFollowersList);

		bugTimeLogList = new BugTimeLogSheet();
		addToSideBar(bugTimeLogList);
	}

	@Override
	protected void onPreviewItem() {
		commentList.loadComments("" + this.beanItem.getId());
		historyList.loadHistory(this.beanItem.getId());
		bugTimeLogList.displayTime(this.beanItem);

		bugFollowersList.displayFollowers(beanItem);
		bugRelatedField.displayRelatedBugs(this.beanItem);

		dateInfoComp.displayEntryDateTime(this.beanItem);
		peopleInfoComp.displayEntryPeople(beanItem);
	}

	@Override
	protected String initFormTitle() {
		return AppContext.getMessage(BugI18nEnum.FORM_READ_TITLE,
				this.beanItem.getBugkey(), this.beanItem.getSummary());
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleBug> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleBug>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new FormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleBug> initBeanFormFieldFactory() {
		return new PreviewFormFieldFactory(this.previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		bugPreviewFormControls = new ProjectPreviewFormControlsGenerator<SimpleBug>(
				previewForm);
		final HorizontalLayout topPanel = bugPreviewFormControls
				.createButtonControls(
						ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED,
						ProjectRolePermissionCollections.BUGS);

		final Button assignBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						UI.getCurrent().addWindow(
								new AssignBugWindow(BugReadViewImpl.this,
										beanItem));
					}
				});
		assignBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));
		assignBtn.setIcon(MyCollabResource
				.newResource(WebResourceIds._16_assign));

		assignBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		this.bugWorkflowControl = new HorizontalLayout();
		this.bugWorkflowControl.setMargin(false);
		this.bugWorkflowControl.addStyleName("workflow-controls");

		bugPreviewFormControls.insertToControlBlock(bugWorkflowControl);
		bugPreviewFormControls.insertToControlBlock(assignBtn);
		topPanel.setSizeUndefined();

		return topPanel;
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabSheet tabBugDetail = new TabSheet();
		tabBugDetail.setWidth("100%");

		tabBugDetail.addTab(commentList, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_COMMENT),
				MyCollabResource
						.newResource("icons/16/project/gray/comment.png"));

		tabBugDetail.addTab(historyList, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_HISTORY),
				MyCollabResource
						.newResource("icons/16/project/gray/history.png"));

		tabBugDetail.addTab(bugRelatedField,
				AppContext.getMessage(BugI18nEnum.TAB_RELATED_BUGS),
				MyCollabResource.newResource("icons/16/project/gray/bug.png"));

		return tabBugDetail;
	}

	private class FormLayoutFactory implements IFormLayoutFactory {
		private static final long serialVersionUID = 1L;
		private GridFormLayoutHelper informationLayout;

		@Override
		public void attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("summary")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 0,
						2, "100%");
			} else if (propertyId.equals("description")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION),
						0, 1, 2, "100%");
			} else if (propertyId.equals("environment")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0,
						2, 2, "100%");
			} else if (propertyId.equals("status")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_STATUS), 0, 3);
			} else if (propertyId.equals("priority")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_PRIORITY), 1, 3);
			} else if (propertyId.equals("severity")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 4);
			} else if (propertyId.equals("resolution")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION), 1,
						4);
			} else if (propertyId.equals("duedate")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE), 0, 5);
			} else if (propertyId.equals("createdtime")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_CREATED_TIME),
						1, 5);
			} else if (propertyId.equals("loguserFullName")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_LOG_BY), 0, 6);
			} else if (propertyId.equals("assignuserFullName")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1,
						6);
			} else if (propertyId.equals("milestoneName")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_PHASE), 0, 7, 2,
						"100%");
			} else if (propertyId.equals("components")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS), 0,
						8, 2, "100%");
			} else if (propertyId.equals("affectedVersions")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS), 0, 9,
						2, "100%");
			} else if (propertyId.equals("fixedVersions")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS),
						0, 10, 2, "100%");
			} else if (propertyId.equals("id")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(BugI18nEnum.FORM_ATTACHMENT), 0,
						11, 2, "100%");
			}
		}

		@Override
		public ComponentContainer getLayout() {
			final VerticalLayout layout = new VerticalLayout();
			layout.setMargin(false);
			this.informationLayout = new GridFormLayoutHelper(2, 12, "100%",
					"167px", Alignment.TOP_LEFT);
			this.informationLayout.getLayout().addStyleName(
					"colored-gridlayout");
			this.informationLayout.getLayout().setMargin(false);
			this.informationLayout.getLayout().setWidth("100%");
			layout.addComponent(this.informationLayout.getLayout());
			layout.setComponentAlignment(this.informationLayout.getLayout(),
					Alignment.BOTTOM_CENTER);
			return layout;
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
				return new DateViewField(beanItem.getDuedate());
			} else if (propertyId.equals("createdtime")) {
				return new DateViewField(beanItem.getCreatedtime());
			} else if (propertyId.equals("assignuserFullName")) {
				return new ProjectUserFormLinkField(beanItem.getAssignuser(),
						beanItem.getAssignUserAvatarId(),
						beanItem.getAssignuserFullName());
			} else if (propertyId.equals("loguserFullName")) {
				return new ProjectUserFormLinkField(beanItem.getLogby(),
						beanItem.getLoguserAvatarId(),
						beanItem.getLoguserFullName());
			} else if (propertyId.equals("id")) {
				return new ProjectFormAttachmentDisplayField(
						beanItem.getProjectid(),
						AttachmentType.PROJECT_BUG_TYPE, beanItem.getId());
			} else if (propertyId.equals("components")) {
				final List<Component> components = beanItem.getComponents();
				if (CollectionUtils.isNotEmpty(components)) {
					final ContainerViewField componentContainer = new ContainerViewField();
					for (final Component component : beanItem.getComponents()) {
						final Button componentLink = new Button(
								component.getComponentname(),
								new Button.ClickListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(
											final ClickEvent event) {
										EventBusFactory.getInstance().post(
												new BugComponentEvent.GotoRead(
														BugReadViewImpl.this,
														component.getId()));
									}
								});
						componentContainer.addComponentField(componentLink);
						componentLink.setStyleName("link");
					}
					componentContainer
							.setStyleName(UIConstants.FORM_CONTAINER_VIEW);
					return componentContainer;
				} else {
					return new DefaultViewField("");
				}
			} else if (propertyId.equals("affectedVersions")) {
				final List<Version> affectedVersions = beanItem
						.getAffectedVersions();
				if (CollectionUtils.isNotEmpty(affectedVersions)) {
					final ContainerViewField componentContainer = new ContainerViewField();
					for (final Version version : beanItem.getAffectedVersions()) {
						final Button versionLink = new Button(
								version.getVersionname(),
								new Button.ClickListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(
											final ClickEvent event) {
										EventBusFactory.getInstance().post(
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
					return new DefaultViewField("");
				}
			} else if (propertyId.equals("fixedVersions")) {
				final List<Version> fixedVersions = beanItem.getFixedVersions();
				if (CollectionUtils.isNotEmpty(fixedVersions)) {
					final ContainerViewField componentContainer = new ContainerViewField();
					for (final Version version : beanItem.getFixedVersions()) {
						final Button versionLink = new Button(
								version.getVersionname(),
								new Button.ClickListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(
											final ClickEvent event) {
										EventBusFactory.getInstance().post(
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
					return new DefaultViewField("");
				}

			} else if (propertyId.equals("milestoneName")) {
				if (beanItem.getMilestoneid() != null) {
					final LinkViewField phaseLink = new LinkViewField(
							beanItem.getMilestoneName(),
							ProjectLinkBuilder
									.generateMilestonePreviewFullLink(
											beanItem.getProjectid(),
											beanItem.getMilestoneid()),
							MyCollabResource
									.newResourceLink("icons/16/project/milestone.png"));
					return phaseLink;
				} else {
					return new DefaultViewField("");
				}

			} else if (propertyId.equals("environment")) {
				return new RichTextViewField(beanItem.getEnvironment());
			} else if (propertyId.equals("description")) {
				return new RichTextViewField(beanItem.getDescription());
			} else if (propertyId.equals("status")) {
				return new I18nFormViewField(beanItem.getStatus(),
						BugStatus.class);
			} else if (propertyId.equals("priority")) {
				if (StringUtils.isNotBlank(beanItem.getPriority())) {
					final Resource iconPriority = new ExternalResource(
							ProjectResources
									.getIconResourceLink12ByBugPriority(beanItem
											.getPriority()));
					final Image iconEmbedded = new Image(null, iconPriority);
					final Label lbPriority = new Label(AppContext.getMessage(
							BugPriority.class, beanItem.getPriority()));

					final ContainerHorizontalViewField containerField = new ContainerHorizontalViewField();
					containerField.addComponentField(iconEmbedded);
					containerField.addComponentField(lbPriority);
					containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
					return containerField;
				}
			} else if (propertyId.equals("severity")) {
				if (StringUtils.isNotBlank(beanItem.getSeverity())) {
					final Resource iconPriority = new ExternalResource(
							ProjectResources
									.getIconResourceLink12ByBugSeverity(beanItem
											.getSeverity()));
					final Image iconEmbedded = new Image();
					iconEmbedded.setSource(iconPriority);
					final Label lbPriority = new Label(AppContext.getMessage(
							BugSeverity.class, beanItem.getSeverity()));

					final ContainerHorizontalViewField containerField = new ContainerHorizontalViewField();
					containerField.addComponentField(iconEmbedded);
					containerField.addComponentField(lbPriority);
					containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
					return containerField;
				}
			} else if (propertyId.equals("resolution")) {
				return new I18nFormViewField(beanItem.getResolution(),
						BugResolution.class);
			}
			return null;
		}
	}

	@Override
	public HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers() {
		return this.previewForm;
	}

	private class PeopleInfoComp extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		private void displayEntryPeople(ValuedBean bean) {
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
						.getProperty(bean, "loguserAvatarId");
				String createdUserDisplayName = (String) PropertyUtils
						.getProperty(bean, "loguserFullName");

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
						.getProperty(bean, "assignuserFullName");

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
