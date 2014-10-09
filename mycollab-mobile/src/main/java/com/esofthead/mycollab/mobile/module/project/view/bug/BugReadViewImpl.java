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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import org.apache.commons.lang3.StringUtils;

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
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
@ViewComponent
public class BugReadViewImpl extends AbstractPreviewItemComp<SimpleBug>
		implements BugReadView {

	private static final long serialVersionUID = 579279560838174387L;

	private ProjectCommentListDisplay associateComments;

	@Override
	public HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers() {
		return this.previewForm;
	}

	@Override
	protected void afterPreviewItem() {
		associateComments.loadComments("" + beanItem.getId());
	}

	@Override
	protected String initFormTitle() {
		return "[" + CurrentProjectVariables.getProject().getShortname() + "-"
				+ beanItem.getBugkey() + "]";
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleBug> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleBug>();
	}

	@Override
	protected void initRelatedComponents() {
		associateComments = new ProjectCommentListDisplay(CommentType.PRJ_BUG,
				CurrentProjectVariables.getProjectId(), true, true,
				BugRelayEmailNotificationAction.class);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new BugFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleBug> initBeanFormFieldFactory() {
		return new BugPreviewBeanFormFieldFactory(this.previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new ProjectPreviewFormControlsGenerator<SimpleBug>(
				this.previewForm)
				.createButtonControls(ProjectRolePermissionCollections.BUGS);
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

			private static final long serialVersionUID = -7469027678729887223L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new ShellEvent.PushView(this, associateComments));
			}
		});
		toolbarLayout.addComponent(relatedComments);

		return toolbarLayout;
	}

	private class BugPreviewBeanFormFieldFactory extends
			AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {

		private static final long serialVersionUID = -288972730658409446L;

		public BugPreviewBeanFormFieldFactory(GenericBeanForm<SimpleBug> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(Object propertyId) {
			if (propertyId.equals("duedate")) {
				return new DefaultFormViewFieldFactory.FormDateViewField(
						beanItem.getDuedate());
			} else if (propertyId.equals("createdtime")) {
				return new DefaultFormViewFieldFactory.FormDateViewField(
						beanItem.getCreatedtime());
			} else if (propertyId.equals("assignuserFullName")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						beanItem.getAssignuserFullName());
			} else if (propertyId.equals("loguserFullName")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						beanItem.getLoguserFullName());
			} else if (propertyId.equals("milestoneid")) {
				if (beanItem.getMilestoneid() != null) {
					return new DefaultFormViewFieldFactory.FormViewField(
							beanItem.getMilestoneName());
				} else {
					return new DefaultFormViewFieldFactory.FormViewField("");
				}

			} else if (propertyId.equals("environment")) {
				return new DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField(
						beanItem.getEnvironment());
			} else if (propertyId.equals("description")) {
				return new DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField(
						beanItem.getDescription());
			} else if (propertyId.equals("status")) {
				return new DefaultFormViewFieldFactory.I18nFormViewField(
						beanItem.getStatus(), BugStatus.class);
			} else if (propertyId.equals("priority")) {
				if (StringUtils.isNotBlank(beanItem.getPriority())) {
					final Resource iconPriority = new ExternalResource(
							ProjectResources
									.getIconResourceLink12ByBugPriority(beanItem
											.getPriority()));
					final Image iconEmbedded = new Image(null, iconPriority);
					final Label lbPriority = new Label(AppContext.getMessage(
							BugPriority.class, beanItem.getPriority()));

					final FormContainerHorizontalViewField containerField = new FormContainerHorizontalViewField();
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

					final FormContainerHorizontalViewField containerField = new FormContainerHorizontalViewField();
					containerField.addComponentField(iconEmbedded);
					containerField.addComponentField(lbPriority);
					containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
					return containerField;
				}
			} else if (propertyId.equals("resolution")) {
				return new DefaultFormViewFieldFactory.I18nFormViewField(
						beanItem.getResolution(), BugResolution.class);
			}
			return null;
		}

	}

}
