/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.milestone;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectCommentListDisplay;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormDateViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */

@ViewComponent
public class MilestoneReadViewImpl extends
		AbstractPreviewItemComp<SimpleMilestone> implements MilestoneReadView {

	private static final long serialVersionUID = -2466318105833801922L;

	private ProjectCommentListDisplay associateComments;
	private Button relatedComments;
	private MilestoneRelatedBugView associateBugs;
	private MilestoneRelatedTaskView associateTasks;

	@Override
	public HasPreviewFormHandlers<SimpleMilestone> getPreviewFormHandlers() {
		return this.previewForm;
	}

	@Override
	protected void afterPreviewItem() {
		associateComments.loadComments("" + beanItem.getId());
		if (associateComments.getNumComments() > 0) {
			relatedComments
					.setCaption("<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_MESSAGE
							+ "\" data-count=\""
							+ associateComments.getNumComments()
							+ "\"></span><div class=\"screen-reader-text\">"
							+ AppContext
									.getMessage(GenericI18Enum.TAB_COMMENT)
							+ "</div>");
		} else {
			relatedComments
					.setCaption("<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_MESSAGE
							+ "\"></span><div class=\"screen-reader-text\">"
							+ AppContext
									.getMessage(GenericI18Enum.TAB_COMMENT)
							+ "</div>");
		}
	}

	@Override
	protected String initFormTitle() {
		return this.beanItem.getName();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
		return new AdvancedPreviewBeanForm<>();
	}

	@Override
	protected void initRelatedComponents() {
		associateComments = new ProjectCommentListDisplay(
				ProjectTypeConstants.MILESTONE,
				CurrentProjectVariables.getProjectId(), true,
				ProjectMilestoneRelayEmailNotificationAction.class);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new MilestoneFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
		return new MilestoneFormFieldFactory(this.previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new ProjectPreviewFormControlsGenerator<>(
				this.previewForm)
				.createButtonControls(ProjectRolePermissionCollections.MILESTONES);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		toolbarLayout.setSpacing(true);

		Button relatedBugs = new Button();
		relatedBugs.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.PROJECT_BUG
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(ProjectCommonI18nEnum.VIEW_BUG)
				+ "</div>");
		relatedBugs.setHtmlContentAllowed(true);
		relatedBugs.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 2113811477874305745L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new ShellEvent.PushView(this, getRelatedBugs()));
			}
		});
		toolbarLayout.addComponent(relatedBugs);

		Button relatedTasks = new Button();
		relatedTasks.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.PROJECT_TASK
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TASK)
				+ "</div>");
		relatedTasks.setHtmlContentAllowed(true);
		relatedTasks.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -9171495386840452500L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new ShellEvent.PushView(this, getRelatedTasks()));
			}
		});
		toolbarLayout.addComponent(relatedTasks);

		relatedComments = new Button();
		relatedComments.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.PROJECT_MESSAGE
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(GenericI18Enum.TAB_COMMENT)
				+ "</div>");
		relatedComments.setHtmlContentAllowed(true);
		relatedComments.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 2206489649468573076L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new ShellEvent.PushView(this, associateComments));
			}
		});
		toolbarLayout.addComponent(relatedComments);

		return toolbarLayout;
	}

	private MilestoneRelatedBugView getRelatedBugs() {
		if (associateBugs == null)
			associateBugs = new MilestoneRelatedBugView();
		associateBugs.displayBugs(beanItem);
		return associateBugs;
	}

	private MilestoneRelatedTaskView getRelatedTasks() {
		if (associateTasks == null)
			associateTasks = new MilestoneRelatedTaskView();
		associateTasks.displayTasks(beanItem);
		return associateTasks;
	}

	private class MilestoneFormFieldFactory extends
			AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {

		private static final long serialVersionUID = 1L;

		public MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {
			if (propertyId.equals("startdate")) {
				return new FormDateViewField(beanItem.getStartdate());
			} else if (propertyId.equals("enddate")) {
				return new FormDateViewField(beanItem.getEnddate());
			} else if (propertyId.equals("owner")) {
				return new FormViewField(beanItem.getOwnerFullName());
			} else if (propertyId.equals("description")) {
				return new FormDetectAndDisplayUrlViewField(
						beanItem.getDescription());
			} else if (propertyId.equals("numOpenTasks")) {
				return new FormViewField(beanItem.getNumOpenTasks() + "/"
						+ beanItem.getNumTasks());
			} else if (propertyId.equals("numOpenBugs")) {
				return new FormViewField(beanItem.getNumOpenBugs() + "/"
						+ beanItem.getNumBugs());
			} else if (propertyId.equals("status")) {
				return new FormViewField(AppContext.getMessage(
						MilestoneStatus.class, beanItem.getStatus()));
			}
			return null;
		}
	}

}
