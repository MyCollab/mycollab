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

package com.esofthead.mycollab.module.project.view.milestone;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.view.bug.BugSimpleDisplayWidget;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.project.view.task.TaskDisplayWidget;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
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
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DateViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class MilestoneReadViewImpl extends
		AbstractPreviewItemComp2<SimpleMilestone> implements MilestoneReadView {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(MilestoneReadViewImpl.class);

	private CommentDisplay commentListComp;

	private MilestoneHistoryLogList historyListComp;

	private MilestoneBugListComp associateBugListComp;

	private MilestoneTaskGroupListComp associateTaskGroupListComp;

	private DateInfoComp dateInfoComp;

	private PeopleInfoComp peopleInfoComp;

	private boolean isSimpleView = false;

	public MilestoneReadViewImpl() {
		super(AppContext.getMessage(MilestoneI18nEnum.VIEW_DETAIL_TITLE),
				MyCollabResource.newResource(WebResourceIds._24_project_phase));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
		return new AdvancedPreviewBeanForm<>();
	}

	@Override
	protected ComponentContainer createButtonControls() {
		ProjectPreviewFormControlsGenerator<SimpleMilestone> controlsGenerator = new ProjectPreviewFormControlsGenerator<>(
				this.previewForm);
		Button toggleViewBtn = new Button("Toggle View",
				new Button.ClickListener() {
					private static final long serialVersionUID = 6800849985797693533L;

					@Override
					public void buttonClick(ClickEvent arg0) {
						if (!isSimpleView) {
							isSimpleView = true;
							displaySimpleView();
						} else {
							isSimpleView = false;
							addBottomPanel(createBottomPanel());
						}
					}
				});
		toggleViewBtn.setIcon(MyCollabResource
				.newResource("icons/16/switch-view.png"));
		controlsGenerator.addOptionButton(toggleViewBtn);
		return controlsGenerator
				.createButtonControls(ProjectRolePermissionCollections.MILESTONES);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
		tabContainer.setWidth("100%");

		tabContainer.addTab(this.commentListComp, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_COMMENT),
				MyCollabResource
						.newResource(WebResourceIds._16_project_gray_comment));
		tabContainer.addTab(historyListComp, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_HISTORY),
				MyCollabResource
						.newResource(WebResourceIds._16_project_gray_history));
		tabContainer.addTab(this.associateTaskGroupListComp,
				AppContext.getMessage(MilestoneI18nEnum.TAB_RELATED_TASKS),
				MyCollabResource.newResource(WebResourceIds._16_project_gray_task));
		tabContainer.addTab(this.associateBugListComp,
				AppContext.getMessage(MilestoneI18nEnum.TAB_RELATED_BUGS),
				MyCollabResource.newResource(WebResourceIds._16_project_gray_bug));

		return tabContainer;
	}

	private void displaySimpleView() {
		VerticalLayout simpleViewBottom = new VerticalLayout();
		simpleViewBottom.setWidth("100%");
		simpleViewBottom.setStyleName("phase-simple-view");

		Label taskListLbl = new Label(
				AppContext.getMessage(MilestoneI18nEnum.TAB_RELATED_TASKS));
		taskListLbl.addStyleName("h2");
		simpleViewBottom.addComponent(taskListLbl);
		TaskSearchCriteria criteria = new TaskSearchCriteria();
		criteria.setProjectid(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		criteria.setMilestoneId(new NumberSearchField(this.beanItem.getId()));

		TaskDisplayWidget taskDisplayWidget = new TaskDisplayWidget();
		simpleViewBottom.addComponent(taskDisplayWidget);
		taskDisplayWidget.setSearchCriteria(criteria);

		Label bugListLbl = new Label(
				AppContext.getMessage(MilestoneI18nEnum.TAB_RELATED_BUGS));
		bugListLbl.addStyleName("h2");
		simpleViewBottom.addComponent(bugListLbl);
		final BugSearchCriteria bugCriteria = new BugSearchCriteria();
		bugCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		bugCriteria.setMilestoneIds(new SetSearchField<>(this.beanItem
				.getId()));

		final BugSimpleDisplayWidget displayWidget = new BugSimpleDisplayWidget();
		simpleViewBottom.addComponent(displayWidget);
		displayWidget.setSearchCriteria(bugCriteria);

		addBottomPanel(simpleViewBottom);
	}

	@Override
	protected void initRelatedComponents() {
		this.historyListComp = new MilestoneHistoryLogList(
				ModuleNameConstants.PRJ, ProjectTypeConstants.MILESTONE);
		this.associateBugListComp = new MilestoneBugListComp();
		this.associateTaskGroupListComp = new MilestoneTaskGroupListComp();
		this.commentListComp = new CommentDisplay(CommentType.PRJ_MILESTONE,
				CurrentProjectVariables.getProjectId(), true, true,
				ProjectMilestoneRelayEmailNotificationAction.class);
		this.commentListComp.setMargin(true);

		dateInfoComp = new DateInfoComp();
		addToSideBar(dateInfoComp);

		peopleInfoComp = new PeopleInfoComp();
		addToSideBar(peopleInfoComp);
	}

	@Override
	public SimpleMilestone getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleMilestone> getPreviewFormHandlers() {
		return previewForm;
	}

	@Override
	protected void onPreviewItem() {
		this.associateTaskGroupListComp.displayTakLists(this.beanItem);
		this.associateBugListComp.displayBugs(this.beanItem);
		this.commentListComp.loadComments("" + this.beanItem.getId());

		historyListComp.loadHistory(beanItem.getId());

		dateInfoComp.displayEntryDateTime(beanItem);
		peopleInfoComp.displayEntryPeople(beanItem);

		if (beanItem.getStatus() != null
				&& beanItem.getStatus().equals("Closed")) {
			addLayoutStyleName(UIConstants.LINK_COMPLETED);
		}
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getName();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new FormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
		return new MilestoneFormFieldFactory(previewForm);
	}

	private class FormLayoutFactory implements IFormLayoutFactory {
		private static final long serialVersionUID = -180624742521398683L;
		private GridFormLayoutHelper informationLayout;

		@Override
		public void attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("startdate")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(MilestoneI18nEnum.FORM_START_DATE_FIELD),
						0, 0);
			} else if (propertyId.equals("enddate")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(MilestoneI18nEnum.FORM_END_DATE_FIELD), 0,
						1);
			} else if (propertyId.equals("owner")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1,
						0);
			} else if (propertyId.equals("status")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(MilestoneI18nEnum.FORM_STATUS_FIELD), 1, 1);
			} else if (propertyId.equals("numOpenTasks")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(MilestoneI18nEnum.FORM_TASK_FIELD), 0, 2);
			} else if (propertyId.equals("numOpenBugs")) {
				this.informationLayout
						.addComponent(field, AppContext
								.getMessage(MilestoneI18nEnum.FORM_BUG_FIELD),
								1, 2);
			} else if (propertyId.equals("description")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION),
						0, 3, 2, "100%");
			}
		}

		@Override
		public ComponentContainer getLayout() {
			final VerticalLayout layout = new VerticalLayout();

			this.informationLayout = new GridFormLayoutHelper(2, 4, "100%",
					"145px", Alignment.TOP_LEFT);
			this.informationLayout.getLayout().setWidth("100%");
			this.informationLayout.getLayout().addStyleName(
					"colored-gridlayout");
			this.informationLayout.getLayout().setMargin(false);
			layout.addComponent(this.informationLayout.getLayout());
			layout.setComponentAlignment(this.informationLayout.getLayout(),
					Alignment.BOTTOM_CENTER);
			return layout;
		}
	}

	private class MilestoneFormFieldFactory extends
			AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {

		private static final long serialVersionUID = 1L;

		public MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {
			SimpleMilestone milestone = attachForm.getBean();
			if (propertyId.equals("startdate")) {
				return new DateViewField(
						milestone.getStartdate());
			} else if (propertyId.equals("enddate")) {
				return new DateViewField(
						milestone.getEnddate());
			} else if (propertyId.equals("owner")) {
				return new ProjectUserFormLinkField(milestone.getOwner(),
						milestone.getOwnerAvatarId(),
						milestone.getOwnerFullName());
			} else if (propertyId.equals("description")) {
				return new RichTextViewField(
						milestone.getDescription());
			} else if (propertyId.equals("numOpenTasks")) {
				final ContainerHorizontalViewField taskComp = new ContainerHorizontalViewField();

				final ProgressBarIndicator progressTask = new ProgressBarIndicator(
						milestone.getNumTasks(), milestone.getNumOpenTasks());
				progressTask.setWidth("100%");
				taskComp.addComponentField(progressTask);
				return taskComp;
			} else if (propertyId.equals("numOpenBugs")) {
				final ContainerHorizontalViewField bugComp = new ContainerHorizontalViewField();

				final ProgressBarIndicator progressBug = new ProgressBarIndicator(
						milestone.getNumBugs(), milestone.getNumOpenBugs());
				progressBug.setWidth("100%");
				bugComp.addComponentField(progressBug);
				return bugComp;
			} else if (propertyId.equals("status")) {
				final ContainerHorizontalViewField statusField = new ContainerHorizontalViewField();
				Image icon = new Image();
				icon.setSource(new ExternalResource(ProjectResources
						.getIconResource12LinkOfPhaseStatus(beanItem
								.getStatus())));
				statusField.addComponentField(icon);
				Label statusLbl = new Label(AppContext.getMessage(
						MilestoneStatus.class, beanItem.getStatus()));
				statusField.addComponentField(statusLbl);
				statusField.getLayout().setExpandRatio(statusLbl, 1.0f);
				return statusField;
			}
			return null;
		}
	}

	private class PeopleInfoComp extends VerticalLayout {
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
						bean, "createduser");
				String createdUserAvatarId = (String) PropertyUtils
						.getProperty(bean, "createdUserAvatarId");
				String createdUserDisplayName = (String) PropertyUtils
						.getProperty(bean, "createdUserFullName");

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
						bean, "owner");
				String assignUserAvatarId = (String) PropertyUtils.getProperty(
						bean, "ownerAvatarId");
				String assignUserDisplayName = (String) PropertyUtils
						.getProperty(bean, "ownerFullName");

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
