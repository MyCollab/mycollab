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
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
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
class ReOpenWindow extends Window {
	private final SimpleBug bug;
	private final EditForm editForm;
	private final BugReadView callbackForm;

	ReOpenWindow(final BugReadView callbackForm, final SimpleBug bug) {
		super("Reopen bug '" + bug.getSummary() + "'");
		this.setResizable(false);
		this.setClosable(false);
		this.setDraggable(false);
		this.setModal(true);
		this.bug = bug;
		this.callbackForm = callbackForm;

		this.setWidth("100%");
		this.editForm = new EditForm();
		this.editForm.setBean(bug);

		constructUI();

		this.center();
	}

	private void constructUI() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setWidth("100%");
		contentLayout.addComponent(this.editForm);

		final HorizontalLayout controlsBtn = new HorizontalLayout();
		controlsBtn.setWidth("100%");

		final Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final Button.ClickEvent event) {
						ReOpenWindow.this.close();
					}
				});
		controlsBtn.addComponent(cancelBtn);

		final Button reOpenBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final Button.ClickEvent event) {

						if (editForm.validateForm()) {
							ReOpenWindow.this.bug.setStatus(BugStatus.ReOpened
									.name());

							// Save bug status and assignee
							final BugService bugService = ApplicationContextUtil
									.getSpringBean(BugService.class);
							bugService.updateSelectiveWithSession(
									ReOpenWindow.this.bug,
									AppContext.getUsername());

							// Save comment
							final String commentValue = editForm.commentArea
									.getValue();
							if (commentValue != null
									&& !commentValue.trim().equals("")) {
								final Comment comment = new Comment();
								comment.setComment(commentValue);
								comment.setCreatedtime(new GregorianCalendar()
										.getTime());
								comment.setCreateduser(AppContext.getUsername());
								comment.setSaccountid(AppContext.getAccountId());
								comment.setType(CommentType.PRJ_BUG.toString());
								comment.setTypeid(""
										+ ReOpenWindow.this.bug.getId());
								comment.setExtratypeid(CurrentProjectVariables
										.getProjectId());

								final CommentService commentService = ApplicationContextUtil
										.getSpringBean(CommentService.class);
								commentService.saveWithSession(comment,
										AppContext.getUsername());
							}

							ReOpenWindow.this.close();
							ReOpenWindow.this.callbackForm.previewItem(bug);
						}

					}
				});
		controlsBtn.addComponent(reOpenBtn);
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
			private VerticalComponentGroup informationLayout;

			@Override
			public ComponentContainer getLayout() {
				this.informationLayout = new VerticalComponentGroup();

				return informationLayout;
			}

			@Override
			public void attachField(final Object propertyId,
					final Field<?> field) {
				if (propertyId.equals("resolution")) {
					field.setCaption(AppContext
							.getMessage(BugI18nEnum.FORM_RESOLUTION));
					this.informationLayout.addComponent(field);
				} else if (propertyId.equals("assignuser")) {
					field.setCaption(AppContext
							.getMessage(GenericI18Enum.FORM_ASSIGNEE));
					this.informationLayout.addComponent(field);
				} else if (propertyId.equals("fixedVersions")) {
					field.setCaption(AppContext
							.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS));
					this.informationLayout.addComponent(field);
				} else if (propertyId.equals("comment")) {
					field.setCaption(AppContext
							.getMessage(BugI18nEnum.FORM_COMMENT));
					this.informationLayout.addComponent(field);
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
					return BugResolutionComboBox.getInstanceForValidBugWindow();
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
