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

package com.esofthead.mycollab.module.project.view.task;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.Comment;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
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
public class AssignTaskGroupWindow extends Window {
	private static final long serialVersionUID = 1L;
	private final TaskList taskList;
	private final EditForm editForm;

	public AssignTaskGroupWindow(TaskList task) {
		super(AppContext.getMessage(TaskI18nEnum.DIALOG_ASSIGN_TASKGROUP_TITLE,
				task.getName()));
		this.taskList = task;
		this.setWidth("750px");
		this.setResizable(false);
		this.setModal(true);

		VerticalLayout contentLayout = new VerticalLayout();
		editForm = new EditForm();
		editForm.setBean(task);
		contentLayout.addComponent(editForm);
		contentLayout.setSpacing(true);
		contentLayout.setMargin(new MarginInfo(false, false, true, false));

		this.setContent(contentLayout);
		center();
	}

	private class EditForm extends AdvancedEditBeanForm<TaskList> {

		private static final long serialVersionUID = 1L;
		private RichTextArea commentArea;

		@Override
		public void setBean(TaskList newDataSource) {
			this.setFormLayoutFactory(new FormLayoutFactory());
			this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
			super.setBean(newDataSource);
		}

		class FormLayoutFactory implements IFormLayoutFactory {

			private static final long serialVersionUID = 1L;
			private GridFormLayoutHelper informationLayout;

			@Override
			public Layout getLayout() {
				VerticalLayout layout = new VerticalLayout();
				this.informationLayout = new GridFormLayoutHelper(2, 2, "100%",
						"167px", Alignment.TOP_LEFT);
				this.informationLayout.getLayout().setWidth("100%");
				this.informationLayout.getLayout().setMargin(false);
				this.informationLayout.getLayout().addStyleName(
						"colored-gridlayout");

				layout.addComponent(informationLayout.getLayout());

				HorizontalLayout controlsBtn = new HorizontalLayout();
				controlsBtn.setSpacing(true);
				controlsBtn.setMargin(new MarginInfo(true, true, true, false));
				layout.addComponent(controlsBtn);

				Button cancelBtn = new Button(
						AppContext
								.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(Button.ClickEvent event) {
								AssignTaskGroupWindow.this.close();
							}
						});
				cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
				controlsBtn.addComponent(cancelBtn);
				controlsBtn.setComponentAlignment(cancelBtn,
						Alignment.MIDDLE_LEFT);

				Button approveBtn = new Button(
						AppContext
								.getMessage(GenericI18Enum.BUTTON_ASSIGN_LABEL),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(Button.ClickEvent event) {
								if (EditForm.this.validateForm()) {
									// Save taskList status and assignee
									ProjectTaskListService taskListService = ApplicationContextUtil
											.getSpringBean(ProjectTaskListService.class);
									taskListService.updateWithSession(taskList,
											AppContext.getUsername());

									// Save comment
									String commentValue = commentArea
											.getValue();
									if (commentValue != null
											&& !commentValue.trim().equals("")) {
										Comment comment = new Comment();
										comment.setComment(commentArea
												.getValue());
										comment.setCreatedtime(new GregorianCalendar()
												.getTime());
										comment.setCreateduser(AppContext
												.getUsername());
										comment.setSaccountid(AppContext
												.getAccountId());
										comment.setType(CommentType.PRJ_TASK_LIST
												.toString());
										comment.setTypeid("" + taskList.getId());
										comment.setExtratypeid(CurrentProjectVariables
												.getProjectId());

										CommentService commentService = ApplicationContextUtil
												.getSpringBean(CommentService.class);
										commentService.saveWithSession(comment,
												AppContext.getUsername());
									}

									AssignTaskGroupWindow.this.close();
									EventBusFactory.getInstance().post(
											new TaskListEvent.GotoRead(this,
													taskList.getId()));
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
			public void attachField(Object propertyId, Field<?> field) {
				if (propertyId.equals("owner")) {
					informationLayout
							.addComponent(field, AppContext
									.getMessage(GenericI18Enum.FORM_ASSIGNEE),
									0, 0);
				} else if (propertyId.equals("comment")) {
					informationLayout.addComponent(field,
							AppContext.getMessage(TaskI18nEnum.FORM_COMMENT),
							0, 1, 2, "100%");
				}
			}
		}

		private class EditFormFieldFactory extends
				AbstractBeanFieldGroupEditFieldFactory<TaskList> {
			private static final long serialVersionUID = 1L;

			public EditFormFieldFactory(GenericBeanForm<TaskList> form) {
				super(form);
			}

			@Override
			protected Field<?> onCreateField(Object propertyId) {
				if (propertyId.equals("owner")) {
					return new ProjectMemberSelectionField();
				} else if (propertyId.equals("comment")) {
					commentArea = new RichTextArea();
					commentArea.setNullRepresentation("");
					return commentArea;
				}
				return null;
			}
		}
	}
}
