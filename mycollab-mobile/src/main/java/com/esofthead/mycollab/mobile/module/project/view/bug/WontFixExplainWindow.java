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

import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.Comment;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 */

/*
 * TODO: Add support BugVersion when it's ready in the next version
 */
@SuppressWarnings("serial")
class WontFixExplainWindow extends Window {

	private final SimpleBug bug;
	private final EditForm editForm;
	private final BugReadView callbackForm;

	WontFixExplainWindow(final BugReadView callbackForm, final SimpleBug bug) {
		super("Won't fix bug '" + bug.getSummary() + "'");
		this.bug = bug;
		this.callbackForm = callbackForm;
		this.setWidth("95%");
		this.setResizable(false);
		this.setDraggable(false);
		this.setClosable(false);
		this.setModal(true);

		this.editForm = new EditForm();
		this.constructUI();

		this.center();
	}

	private void constructUI() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setWidth("100%");
		contentLayout.addComponent(this.editForm);
		this.editForm.setBean(bug);
		final HorizontalLayout controlsBtn = new HorizontalLayout();
		controlsBtn.setWidth("100%");

		final Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						WontFixExplainWindow.this.close();
					}
				});
		controlsBtn.addComponent(cancelBtn);

		final Button wonFixBtn = new Button(
				AppContext.getMessage(BugI18nEnum.BUTTON_WONT_FIX),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {

						if (editForm.validateForm()) {
							WontFixExplainWindow.this.bug
									.setStatus(BugStatus.Resolved.name());

							final String commentValue = editForm.commentArea
									.getValue();
							if (commentValue != null
									&& !commentValue.trim().equals("")) {

								// Save bug status and assignee
								final BugService bugService = ApplicationContextUtil
										.getSpringBean(BugService.class);
								bugService.updateSelectiveWithSession(
										WontFixExplainWindow.this.bug,
										AppContext.getUsername());

								// Save comment

								final Comment comment = new Comment();
								comment.setComment(commentValue);
								comment.setCreatedtime(new GregorianCalendar()
										.getTime());
								comment.setCreateduser(AppContext.getUsername());
								comment.setSaccountid(AppContext.getAccountId());
								comment.setType(CommentType.PRJ_BUG.toString());
								comment.setTypeid(""
										+ WontFixExplainWindow.this.bug.getId());
								comment.setExtratypeid(CurrentProjectVariables
										.getProjectId());

								final CommentService commentService = ApplicationContextUtil
										.getSpringBean(CommentService.class);
								commentService.saveWithSession(comment,
										AppContext.getUsername());

								WontFixExplainWindow.this.close();
								WontFixExplainWindow.this.callbackForm
										.previewItem(bug);
							} else {
								NotificationUtil.showErrorNotification(AppContext
										.getMessage(BugI18nEnum.ERROR_WONT_FIX_EXPLAIN_REQUIRE_MSG));
								return;
							}

							WontFixExplainWindow.this.close();
						}
					}
				});
		controlsBtn.addComponent(wonFixBtn);
		contentLayout.addComponent(controlsBtn);
		this.setContent(contentLayout);
	}

	private class EditForm extends AdvancedEditBeanForm<BugWithBLOBs> {

		private static final long serialVersionUID = 1L;
		private TextArea commentArea;

		@Override
		public void setBean(final BugWithBLOBs newDataSource) {
			this.setFormLayoutFactory(new FormLayoutFactory());
			this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
			super.setBean(newDataSource);
		}

		class FormLayoutFactory implements IFormLayoutFactory {

			private static final long serialVersionUID = 1L;
			private VerticalComponentGroup layout;

			@Override
			public ComponentContainer getLayout() {
				layout = new VerticalComponentGroup();
				return layout;
			}

			@Override
			public void attachField(final Object propertyId,
					final Field<?> field) {
				if (propertyId.equals("resolution")) {
					field.setCaption(AppContext
							.getMessage(BugI18nEnum.FORM_RESOLUTION));
					this.layout.addComponent(field);
				} else if (propertyId.equals("assignuser")) {
					field.setCaption(AppContext
							.getMessage(GenericI18Enum.FORM_ASSIGNEE));
					this.layout.addComponent(field);
				} else if (propertyId.equals("comment")) {
					field.setCaption(AppContext
							.getMessage(BugI18nEnum.FORM_COMMENT));
					this.layout.addComponent(field);
				}
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
				if (propertyId.equals("resolution")) {
					return BugResolutionComboBox.getInstanceForWontFixWindow();
				} else if (propertyId.equals("assignuser")) {
					return new ProjectMemberSelectionField();
				} else if (propertyId.equals("comment")) {
					EditForm.this.commentArea = new TextArea();
					EditForm.this.commentArea.setNullRepresentation("");
					return EditForm.this.commentArea;
				}

				return null;
			}
		}
	}
}
