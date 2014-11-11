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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectAttachmentDisplayComp;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectCommentListDisplay;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormDateViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.I18nFormViewField;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
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
import com.vaadin.ui.VerticalLayout;

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
	private Button relatedComments;

	private VerticalLayout bugWorkFlowControl;

	private BugTimeLogComp bugTimeLogComp;

	private ProjectAttachmentDisplayComp attachmentComp;

	public BugReadViewImpl() {
		super();
		bugTimeLogComp = new BugTimeLogComp();
	}

	@Override
	public HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers() {
		return this.previewForm;
	}

	private void displayWorkflowControl() {
		this.bugWorkFlowControl.removeAllComponents();
		if (BugStatus.Open.name().equals(this.beanItem.getStatus())
				|| BugStatus.ReOpened.name().equals(this.beanItem.getStatus())) {
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
			bugWorkFlowControl.addComponent(startProgressBtn);

			final Button resolveBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.PushView(this,
											new ResolvedInputView(
													BugReadViewImpl.this,
													beanItem)));
						}
					});
			bugWorkFlowControl.addComponent(resolveBtn);

			final Button wontFixBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_WONTFIX),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.PushView(this,
											new WontFixExplainView(
													BugReadViewImpl.this,
													beanItem)));
						}
					});
			bugWorkFlowControl.addComponent(wontFixBtn);
		} else if (BugStatus.InProgress.name()
				.equals(this.beanItem.getStatus())) {
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
			bugWorkFlowControl.addComponent(stopProgressBtn);

			final Button resolveBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.PushView(this,
											new ResolvedInputView(
													BugReadViewImpl.this,
													beanItem)));
						}
					});
			bugWorkFlowControl.addComponent(resolveBtn);
		} else if (BugStatus.Verified.name().equals(this.beanItem.getStatus())) {
			final Button reopenBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.PushView(this,
											new ReOpenView(
													BugReadViewImpl.this,
													beanItem)));
						}
					});
			bugWorkFlowControl.addComponent(reopenBtn);
		} else if (BugStatus.Resolved.name().equals(this.beanItem.getStatus())) {
			final Button reopenBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.PushView(this,
											new ReOpenView(
													BugReadViewImpl.this,
													beanItem)));
						}
					});
			bugWorkFlowControl.addComponent(reopenBtn);

			final Button approveNCloseBtn = new Button(
					AppContext.getMessage(BugI18nEnum.BUTTON_APPROVE_CLOSE),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.PushView(this,
											new ApproveInputView(
													BugReadViewImpl.this,
													beanItem)));
						}
					});
			bugWorkFlowControl.addComponent(approveNCloseBtn);
		} else if (BugStatus.Resolved.name().equals(this.beanItem.getStatus())) {
			final Button reopenBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.PushView(this,
											new ReOpenView(
													BugReadViewImpl.this,
													beanItem)));
						}
					});
			bugWorkFlowControl.addComponent(reopenBtn);
		}
		this.bugWorkFlowControl.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));
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
									.getMessage(ProjectCommonI18nEnum.TAB_COMMENT)
							+ "</div>");
		} else {
			relatedComments
					.setCaption("<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_MESSAGE
							+ "\"></span><div class=\"screen-reader-text\">"
							+ AppContext
									.getMessage(ProjectCommonI18nEnum.TAB_COMMENT)
							+ "</div>");
		}

		displayWorkflowControl();
		bugTimeLogComp.displayTime(beanItem);
		this.previewForm.addComponent(bugTimeLogComp);

		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		List<Content> attachments = resourceService.getContents(AttachmentUtils
				.getProjectEntityAttachmentPath(AppContext.getAccountId(),
						beanItem.getProjectid(),
						AttachmentType.PROJECT_BUG_TYPE, beanItem.getId()));
		if (CollectionUtils.isNotEmpty(attachments)) {
			attachmentComp = new ProjectAttachmentDisplayComp(attachments);
			this.previewForm.addComponent(attachmentComp);
		} else {
			if (attachmentComp != null) {
				this.previewForm.removeComponent(attachmentComp);
			}
		}
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
		ProjectPreviewFormControlsGenerator<SimpleBug> formControlsGenerator = new ProjectPreviewFormControlsGenerator<SimpleBug>(
				this.previewForm);
		VerticalLayout controlsLayout = formControlsGenerator
				.createButtonControls(ProjectRolePermissionCollections.BUGS);
		bugWorkFlowControl = new VerticalLayout();
		bugWorkFlowControl.setWidth("100%");
		bugWorkFlowControl.setSpacing(true);
		formControlsGenerator.insertToControlBlock(bugWorkFlowControl);
		return controlsLayout;

	}

	@Override
	protected ComponentContainer createBottomPanel() {
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		toolbarLayout.setSpacing(true);

		relatedComments = new Button();
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
				return new FormDateViewField(beanItem.getDuedate());
			} else if (propertyId.equals("createdtime")) {
				return new FormDateViewField(beanItem.getCreatedtime());
			} else if (propertyId.equals("assignuserFullName")) {
				return new FormViewField(beanItem.getAssignuserFullName());
			} else if (propertyId.equals("loguserFullName")) {
				return new FormViewField(beanItem.getLoguserFullName());
			} else if (propertyId.equals("milestoneid")) {
				if (beanItem.getMilestoneid() != null) {
					return new FormViewField(beanItem.getMilestoneName());
				} else {
					return new FormViewField("");
				}

			} else if (propertyId.equals("environment")) {
				return new FormDetectAndDisplayUrlViewField(
						beanItem.getEnvironment());
			} else if (propertyId.equals("description")) {
				return new FormDetectAndDisplayUrlViewField(
						beanItem.getDescription());
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
				return new I18nFormViewField(beanItem.getResolution(),
						BugResolution.class);
				// } else if (propertyId.equals("id")) {
				// return new ProjectFormAttachmentDisplayField(
				// beanItem.getProjectid(),
				// AttachmentType.PROJECT_BUG_TYPE, beanItem.getId());
			}
			return null;
		}

	}

}
