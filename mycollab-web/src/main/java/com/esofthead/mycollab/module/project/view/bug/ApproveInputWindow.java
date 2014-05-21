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

import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.Comment;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.tracker.BugStatusConstants;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ApproveInputWindow extends Window {
	private static final long serialVersionUID = 1L;
	private final SimpleBug bug;
	private final EditForm editForm;
	private final IBugCallbackStatusComp callbackForm;

	public ApproveInputWindow(final IBugCallbackStatusComp callbackForm,
			final SimpleBug bug) {
		super("Approve bug '" + bug.getSummary() + "'");
		this.setResizable(false);
		this.bug = bug;
		this.callbackForm = callbackForm;

		VerticalLayout contentLayout = new VerticalLayout();
		this.setWidth("750px");
		this.editForm = new EditForm();
		contentLayout.addComponent(this.editForm);
		contentLayout.setMargin(new MarginInfo(false, false, true, false));
		this.editForm.setBean(bug);
		this.setContent(contentLayout);
		this.center();
	}

	private class EditForm extends AdvancedEditBeanForm<BugWithBLOBs> {

		private static final long serialVersionUID = 1L;
		private RichTextArea commentArea;

		@Override
		public void setBean(final BugWithBLOBs newDataSource) {
			this.setFormLayoutFactory(new FormLayoutFactory());
			this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
			super.setBean(newDataSource);
		}

		class FormLayoutFactory implements IFormLayoutFactory {

			private static final long serialVersionUID = 1L;
			private GridFormLayoutHelper informationLayout;

			@Override
			public Layout getLayout() {
				final VerticalLayout layout = new VerticalLayout();
				this.informationLayout = new GridFormLayoutHelper(2, 6, "100%",
						"167px", Alignment.TOP_LEFT);
				this.informationLayout.getLayout().setWidth("100%");
				this.informationLayout.getLayout().setMargin(false);
				this.informationLayout.getLayout().addStyleName(
						"colored-gridlayout");

				layout.addComponent(this.informationLayout.getLayout());

				final HorizontalLayout controlsBtn = new HorizontalLayout();
				controlsBtn.setSpacing(true);
				controlsBtn.setMargin(new MarginInfo(true, true, true, false));
				layout.addComponent(controlsBtn);

				final Button cancelBtn = new Button(
						AppContext
								.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {
								ApproveInputWindow.this.close();
							}
						});
				cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
				controlsBtn.addComponent(cancelBtn);
				controlsBtn.setComponentAlignment(cancelBtn,
						Alignment.MIDDLE_LEFT);

				final Button approveBtn = new Button("Approve & Close",
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {

								if (EditForm.this.validateForm()) {
									// Save bug status and assignee
									ApproveInputWindow.this.bug
											.setStatus(BugStatusConstants.VERIFIED);

									final BugService bugService = ApplicationContextUtil
											.getSpringBean(BugService.class);
									bugService.updateWithSession(
											ApproveInputWindow.this.bug,
											AppContext.getUsername());

									// Save comment
									final String commentValue = EditForm.this.commentArea
											.getValue();
									if (commentValue != null
											&& !commentValue.trim().equals("")) {
										final Comment comment = new Comment();
										comment.setComment(EditForm.this.commentArea
												.getValue());
										comment.setCreatedtime(new GregorianCalendar()
												.getTime());
										comment.setCreateduser(AppContext
												.getUsername());
										comment.setSaccountid(AppContext
												.getAccountId());
										comment.setType(CommentType.PRJ_BUG
												.toString());
										comment.setTypeid(ApproveInputWindow.this.bug
												.getId());
										comment.setExtratypeid(CurrentProjectVariables
												.getProjectId());

										final CommentService commentService = ApplicationContextUtil
												.getSpringBean(CommentService.class);
										commentService.saveWithSession(comment,
												AppContext.getUsername());
									}

									ApproveInputWindow.this.close();
									ApproveInputWindow.this.callbackForm
											.refreshBugItem();
								}
							}
						});
				approveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
				controlsBtn.addComponent(approveBtn);
				controlsBtn.setComponentAlignment(approveBtn,
						Alignment.MIDDLE_RIGHT);
				layout.setComponentAlignment(controlsBtn,
						Alignment.MIDDLE_RIGHT);

				return layout;
			}

			@Override
			public boolean attachField(final Object propertyId,
					final Field<?> field) {
				if (propertyId.equals("assignuser")) {
					this.informationLayout.addComponent(field, AppContext
							.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD), 0,
							0);
				} else if (propertyId.equals("comment")) {
					this.informationLayout.addComponent(field, "Comments", 0,
							1, 2, "100%", Alignment.MIDDLE_LEFT);
				} else {
					return false;
				}

				return true;
			}
		}

		private class EditFormFieldFactory extends
				AbstractBeanFieldGroupEditFieldFactory<BugWithBLOBs> {
			private static final long serialVersionUID = 1L;

			public EditFormFieldFactory(GenericBeanForm<BugWithBLOBs> form) {
				super(form);
			}

			@Override
			protected Field<?> onCreateField(final Object propertyId) {
				if (propertyId.equals("assignuser")) {
					return new ProjectMemberSelectionField();
				} else if (propertyId.equals("comment")) {
					EditForm.this.commentArea = new RichTextArea();
					EditForm.this.commentArea.setNullRepresentation("");
					return EditForm.this.commentArea;
				}

				return null;
			}
		}
	}
}
