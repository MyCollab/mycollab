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

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class MilestoneReadViewImpl extends
		AbstractPreviewItemComp<SimpleMilestone> implements MilestoneReadView {

	private static final long serialVersionUID = 1L;

	protected CommentDisplay associateCommentListComp;

	protected MilestoneHistoryLogList historyList;

	protected MilestoneBugListComp associateBugListComp;

	protected MilestoneTaskGroupListComp associateTaskGroupListComp;

	public MilestoneReadViewImpl() {
		super("Phase detail", MyCollabResource
				.newResource("icons/24/project/phase.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleMilestone>();
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new ProjectPreviewFormControlsGenerator<SimpleMilestone>(
				this.previewForm)
				.createButtonControls(ProjectRolePermissionCollections.MILESTONES);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
		tabContainer.setWidth("100%");

		tabContainer.addTab(this.associateCommentListComp, "Comments",
				MyCollabResource
						.newResource("icons/16/project/gray/comment.png"));
		tabContainer.addTab(historyList, "History", MyCollabResource
				.newResource("icons/16/project/gray/history.png"));
		tabContainer.addTab(this.associateTaskGroupListComp, "Related Task",
				MyCollabResource.newResource("icons/16/project/gray/task.png"));
		tabContainer.addTab(this.associateBugListComp, "Related Bugs",
				MyCollabResource.newResource("icons/16/project/gray/bug.png"));

		return tabContainer;
	}

	@Override
	protected void initRelatedComponents() {
		this.historyList = new MilestoneHistoryLogList(ModuleNameConstants.PRJ,
				ProjectTypeConstants.MILESTONE);
		this.associateBugListComp = new MilestoneBugListComp();
		this.associateTaskGroupListComp = new MilestoneTaskGroupListComp();
		this.associateCommentListComp = new CommentDisplay(
				CommentType.PRJ_MILESTONE,
				CurrentProjectVariables.getProjectId(), true, true,
				ProjectMilestoneRelayEmailNotificationAction.class);
		this.associateCommentListComp.setMargin(true);
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
		displayTaskGroups();
		displayBugs();
		displayComments();

		historyList.loadHistory(beanItem.getId());

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

	protected void displayBugs() {
		this.associateBugListComp.displayBugs(this.beanItem);
	}

	protected void displayComments() {
		this.associateCommentListComp.loadComments(this.beanItem.getId());
	}

	protected void displayTaskGroups() {
		this.associateTaskGroupListComp.displayTakLists(this.beanItem);
	}

	private class FormLayoutFactory implements IFormLayoutFactory {
		private static final long serialVersionUID = -180624742521398683L;
		private GridFormLayoutHelper informationLayout;

		@Override
		public boolean attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("startdate")) {
				this.informationLayout.addComponent(field, "Start Date", 0, 0);
			} else if (propertyId.equals("enddate")) {
				this.informationLayout.addComponent(field, "End Date", 0, 1);
			} else if (propertyId.equals("owner")) {
				this.informationLayout.addComponent(field, LocalizationHelper
						.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD), 1, 0);
			} else if (propertyId.equals("status")) {
				this.informationLayout.addComponent(field, "Status", 1, 1);
			} else if (propertyId.equals("numOpenTasks")) {
				this.informationLayout.addComponent(field, "Tasks", 0, 2);
			} else if (propertyId.equals("numOpenBugs")) {
				this.informationLayout.addComponent(field, "Bugs", 1, 2);
			} else if (propertyId.equals("description")) {
				this.informationLayout.addComponent(field, "Description", 0, 3,
						2, "100%");
			} else {
				return false;
			}

			return true;
		}

		@Override
		public Layout getLayout() {
			final VerticalLayout layout = new VerticalLayout();

			this.informationLayout = new GridFormLayoutHelper(2, 4, "100%",
					"145px", Alignment.MIDDLE_LEFT);
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
				return new DefaultFormViewFieldFactory.FormDateViewField(
						milestone.getStartdate());
			} else if (propertyId.equals("enddate")) {
				return new DefaultFormViewFieldFactory.FormDateViewField(
						milestone.getEnddate());
			} else if (propertyId.equals("owner")) {
				return new ProjectUserFormLinkField(milestone.getOwner(),
						milestone.getOwnerAvatarId(),
						milestone.getOwnerFullName());
			} else if (propertyId.equals("description")) {
				return new DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField(
						milestone.getDescription());
			} else if (propertyId.equals("numOpenTasks")) {
				final FormContainerHorizontalViewField taskComp = new FormContainerHorizontalViewField();

				final ProgressBarIndicator progressTask = new ProgressBarIndicator(
						milestone.getNumTasks(), milestone.getNumOpenTasks());
				progressTask.setWidth("100%");
				taskComp.addComponentField(progressTask);
				return taskComp;
			} else if (propertyId.equals("numOpenBugs")) {
				final FormContainerHorizontalViewField bugComp = new FormContainerHorizontalViewField();

				final ProgressBarIndicator progressBug = new ProgressBarIndicator(
						milestone.getNumBugs(), milestone.getNumOpenBugs());
				progressBug.setWidth("100%");
				bugComp.addComponentField(progressBug);
				return bugComp;
			} else if (propertyId.equals("status")) {
				final FormContainerHorizontalViewField statusField = new FormContainerHorizontalViewField();
				Image icon = new Image();
				icon.setSource(ProjectResources
						.getIconResource12ByPhase(beanItem.getStatus()));
				statusField.addComponentField(icon);
				Label statusLbl = new Label(beanItem.getStatus());
				statusField.addComponentField(statusLbl);
				statusField.getLayout().setExpandRatio(statusLbl, 1.0f);
				return statusField;
			}
			return null;
		}
	}
}
