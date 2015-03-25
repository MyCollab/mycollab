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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectDataTypeFactory;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextEditField;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectAddWindow extends Window {
	private static final long serialVersionUID = 1L;

	private final Project project;
	private final AdvancedEditBeanForm<Project> editForm;

	public ProjectAddWindow() {
		this.setWidth("900px");
		this.center();
		this.setResizable(false);
		this.setModal(true);

		MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, false, true, false));
		this.setContent(contentLayout);

		this.project = new Project();
		this.editForm = new AdvancedEditBeanForm<>();
		contentLayout.addComponent(this.editForm);
		this.setCaption(AppContext.getMessage(ProjectI18nEnum.VIEW_NEW_TITLE));

		this.editForm.setFormLayoutFactory(new FormLayoutFactory());
		this.editForm.setBeanFormFieldFactory(new EditFormFieldFactory(editForm));
		this.editForm.setBean(this.project);
	}

	private class EditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<Project> {
		private static final long serialVersionUID = 1L;

		public EditFormFieldFactory(GenericBeanForm<Project> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {
			if (propertyId.equals("description")) {
				return new RichTextEditField();
			} else if (propertyId.equals("projectstatus")) {
				final ProjectStatusComboBox projectCombo = new ProjectStatusComboBox();
				projectCombo.setRequired(true);
				projectCombo
						.setRequiredError("Project status must be not null");
				if (project.getProjectstatus() == null) {
					project.setProjectstatus(StatusI18nEnum.Open.name());
				}
				return projectCombo;
			} else if (propertyId.equals("shortname")) {
				final TextField tf = new TextField();
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Project short name must be not null");
				return tf;
			} else if (propertyId.equals("name")) {
				final TextField tf = new TextField();
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Project name must be not null");
				return tf;
			}

			return null;
		}
	}

	class FormLayoutFactory implements IFormLayoutFactory {
		private static final long serialVersionUID = 1L;

		private GridFormLayoutHelper informationLayout;

		@Override
		public ComponentContainer getLayout() {
			final VerticalLayout projectAddLayout = new VerticalLayout();

			this.informationLayout =  GridFormLayoutHelper.defaultFormLayoutHelper(2, 4);
			projectAddLayout.addComponent(this.informationLayout.getLayout());

			final MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(true).withStyleName("addNewControl");

			final Button closeBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							ProjectAddWindow.this.close();
						}

					});
			closeBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			buttonControls.with(closeBtn).withAlign(closeBtn, Alignment.MIDDLE_RIGHT);

            final Button saveBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            if (editForm.validateForm()) {
                                project.setSaccountid(AppContext.getAccountId());
                                final ProjectService projectService = ApplicationContextUtil
                                        .getSpringBean(ProjectService.class);

                                projectService.saveWithSession(
                                        ProjectAddWindow.this.project,
                                        AppContext.getUsername());

                                EventBusFactory
                                        .getInstance()
                                        .post(new ProjectEvent.GotoMyProject(
                                                this,
                                                new PageActionChain(
                                                        new ProjectScreenData.Goto(
                                                                project.getId()))));
                                ProjectAddWindow.this.close();
                            }
                        }

                    });
            saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            saveBtn.setIcon(FontAwesome.SAVE);
            saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            buttonControls.with(saveBtn).withAlign(saveBtn, Alignment.MIDDLE_RIGHT);

			projectAddLayout.addComponent(buttonControls);
			projectAddLayout.setComponentAlignment(buttonControls,
					Alignment.MIDDLE_RIGHT);
			return projectAddLayout;
		}

		@Override
		public void attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("name")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(ProjectI18nEnum.FORM_NAME), 0, 0);
			} else if (propertyId.equals("homepage")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE),
						1, 0);
			} else if (propertyId.equals("shortname")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(ProjectI18nEnum.FORM_SHORT_NAME),
						0, 1);
			} else if (propertyId.equals("projectstatus")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(ProjectI18nEnum.FORM_STATUS), 1,
						1);
			} else if (propertyId.equals("planstartdate")) {
				this.informationLayout
						.addComponent(
								field,
								AppContext
										.getMessage(ProjectI18nEnum.FORM_PLAN_START_DATE),
								0, 2);
			} else if (propertyId.equals("planenddate")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(ProjectI18nEnum.FORM_PLAN_END_DATE), 1, 2);
			} else if (propertyId.equals("description")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION),
						0, 3, 2, "100%", Alignment.MIDDLE_LEFT);
			}

		}
	}

	private static class ProjectStatusComboBox extends ValueComboBox {
		private static final long serialVersionUID = 1L;

		public ProjectStatusComboBox() {
			super(false, ProjectDataTypeFactory.getProjectStatusList());
		}
	}
}
