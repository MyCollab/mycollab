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

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectCommentListDisplay;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

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

	@Override
	public HasPreviewFormHandlers<SimpleMilestone> getPreviewFormHandlers() {
		return this.previewForm;
	}

	@Override
	protected void afterPreviewItem() {
		associateComments.loadComments("" + beanItem.getId());

	}

	@Override
	protected String initFormTitle() {
		return this.beanItem.getName();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleMilestone>();
	}

	@Override
	protected void initRelatedComponents() {
		associateComments = new ProjectCommentListDisplay(
				CommentType.PRJ_MILESTONE,
				CurrentProjectVariables.getProjectId(), true, true,
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
		return new ProjectPreviewFormControlsGenerator<SimpleMilestone>(
				this.previewForm)
				.createButtonControls(ProjectRolePermissionCollections.MILESTONES);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		toolbarLayout.setSpacing(true);

		Button relatedComments = new Button();
		relatedComments.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.PROJECT_MESSAGE
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(ProjectCommonI18nEnum.TAB_COMMENT)
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

	private class MilestoneFormFieldFactory extends
			AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {

		private static final long serialVersionUID = 1L;

		public MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {
			if (propertyId.equals("startdate")) {
				return new DefaultFormViewFieldFactory.FormDateViewField(
						beanItem.getStartdate());
			} else if (propertyId.equals("enddate")) {
				return new DefaultFormViewFieldFactory.FormDateViewField(
						beanItem.getEnddate());
			} else if (propertyId.equals("owner")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						beanItem.getOwnerFullName());
			} else if (propertyId.equals("description")) {
				return new DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField(
						beanItem.getDescription());
			} else if (propertyId.equals("numOpenTasks")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						beanItem.getNumOpenTasks() + "/"
								+ beanItem.getNumTasks());
			} else if (propertyId.equals("numOpenBugs")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						beanItem.getNumOpenBugs() + "/" + beanItem.getNumBugs());
			} else if (propertyId.equals("status")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						AppContext.getMessage(MilestoneStatus.class,
								beanItem.getStatus()));
			}
			return null;
		}
	}

}
