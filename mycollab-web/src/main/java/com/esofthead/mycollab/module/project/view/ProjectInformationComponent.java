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
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormUrlLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectInformationComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private SimpleProject project;

	private ProjectDisplayInformation prjDisplay;

	private final HorizontalLayout projectInfoHeader;

	private final HorizontalLayout projectInfoFooter;

	public ProjectInformationComponent() {
		this.setStyleName(UIConstants.PROJECT_INFO);
		this.prjDisplay = new BasicProjectInformation();
		this.projectInfoHeader = new HorizontalLayout();
		this.projectInfoHeader.setWidth("100%");
		this.projectInfoHeader.setMargin(true);
		this.projectInfoHeader
				.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		this.projectInfoHeader.setStyleName(UIConstants.PROJECT_INFO_HEADER);
		this.addComponent(this.projectInfoHeader);
		this.addComponent(this.prjDisplay);

		this.projectInfoFooter = new HorizontalLayout();
		this.projectInfoFooter.setMargin(true);
		this.projectInfoFooter.setStyleName(UIConstants.PROJECT_INFO_FOOTER);
		final Button toggleBtn = new Button(
				AppContext.getMessage(ProjectI18nEnum.BUTTON_MORE));
		toggleBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final int replaceIndex = ProjectInformationComponent.this
						.getComponentIndex(ProjectInformationComponent.this.prjDisplay);
				ProjectInformationComponent.this
						.removeComponent(ProjectInformationComponent.this.prjDisplay);
				if (ProjectInformationComponent.this.prjDisplay instanceof BasicProjectInformation) {
					ProjectInformationComponent.this.prjDisplay = new DetailProjectInformation();
					event.getButton().setCaption("Less");
				} else {
					ProjectInformationComponent.this.prjDisplay = new BasicProjectInformation();
					event.getButton().setCaption("More");
				}
				ProjectInformationComponent.this.addComponent(
						ProjectInformationComponent.this.prjDisplay,
						replaceIndex);
				ProjectInformationComponent.this.prjDisplay.show();
			}
		});
		toggleBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
		this.projectInfoFooter.addComponent(toggleBtn);
		this.addComponent(this.projectInfoFooter);
	}

	public void displayProjectInformation() {
		this.project = CurrentProjectVariables.getProject();

		this.projectInfoHeader.removeAllComponents();
		this.projectInfoHeader.setSpacing(true);
		final Image icon = new Image(null,
				MyCollabResource.newResource("icons/24/project/dashboard.png"));
		final Label projectName = new Label(this.project.getName());
		projectName.setStyleName(UIConstants.PROJECT_NAME);
		projectName.setSizeUndefined();
		final Label projectShortname = new Label("("
				+ this.project.getShortname() + ")");
		projectShortname.setStyleName(UIConstants.PROJECT_SHORT_NAME);
		this.projectInfoHeader.addComponent(icon);
		this.projectInfoHeader.addComponent(projectName);
		this.projectInfoHeader.setComponentAlignment(projectName,
				Alignment.MIDDLE_LEFT);
		this.projectInfoHeader.addComponent(projectShortname);
		this.projectInfoHeader.setExpandRatio(projectShortname, 1.0f);
		this.projectInfoHeader.setComponentAlignment(projectShortname,
				Alignment.MIDDLE_LEFT);

		this.prjDisplay.show();
	}

	private class BasicProjectInformation extends VerticalLayout implements
			ProjectDisplayInformation {
		private static final long serialVersionUID = 1L;

		private final AdvancedPreviewBeanForm<SimpleProject> previewForm;

		public BasicProjectInformation() {
			this.previewForm = new AdvancedPreviewBeanForm<SimpleProject>();
			this.addComponent(this.previewForm);
		}

		@Override
		public void show() {
			this.previewForm.setFormLayoutFactory(new IFormLayoutFactory() {
				private static final long serialVersionUID = 1L;
				private GridFormLayoutHelper informationLayout;

				@Override
				public void attachField(final Object propertyId,
						final Field<?> field) {
					if (propertyId.equals("homepage")) {
						this.informationLayout.addComponent(field, AppContext
								.getMessage(ProjectI18nEnum.FORM_HOME_PAGE), 0,
								0, Alignment.TOP_LEFT);
					} else if (propertyId.equals("actualstartdate")) {
						this.informationLayout.addComponent(
								field,
								AppContext
										.getMessage(ProjectI18nEnum.FORM_ACTUAL_START_DATE),
								1, 0, Alignment.TOP_LEFT);
					} else if (propertyId.equals("description")) {
						this.informationLayout.addComponent(field, AppContext
								.getMessage(GenericI18Enum.FORM_DESCRIPTION),
								0, 1, 2, "100%", Alignment.TOP_LEFT);
					}
				}

				@Override
				public Layout getLayout() {
					this.informationLayout = new GridFormLayoutHelper(2, 3,
							"100%", "167px", Alignment.TOP_LEFT);
					this.informationLayout.getLayout().setWidth("100%");
					this.informationLayout.getLayout().setMargin(false);
					this.informationLayout.getLayout().addStyleName(
							"colored-gridlayout");
					return this.informationLayout.getLayout();
				}
			});
			this.previewForm
					.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<SimpleProject>(
							previewForm) {
						private static final long serialVersionUID = 1L;

						@Override
						protected Field<?> onCreateField(Object propertyId) {
							if (propertyId.equals("actualstartdate")) {
								return new DefaultFormViewFieldFactory.FormViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getActualstartdate()));
							} else if (propertyId.equals("homepage")) {
								return new FormUrlLinkViewField(
										ProjectInformationComponent.this.project
												.getHomepage());
							} else if (propertyId.equals("description")) {
								return new FormDetectAndDisplayUrlViewField(
										ProjectInformationComponent.this.project
												.getDescription());
							}
							return null;
						}
					});
			this.previewForm.setBean(ProjectInformationComponent.this.project);
		}
	}

	private class DetailProjectInformation extends VerticalLayout implements
			ProjectDisplayInformation {
		private static final long serialVersionUID = 1L;
		private final AdvancedPreviewBeanForm<SimpleProject> previewForm;

		public DetailProjectInformation() {
			this.previewForm = new AdvancedPreviewBeanForm<SimpleProject>();
			this.addComponent(this.previewForm);
		}

		@Override
		public void show() {
			this.previewForm
					.setFormLayoutFactory(new ProjectInformationLayout());
			this.previewForm
					.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<SimpleProject>(
							previewForm) {

						private static final long serialVersionUID = 1L;

						@Override
						protected Field<?> onCreateField(Object propertyId) {
							if (propertyId.equals("planstartdate")) {
								return new FormViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getPlanstartdate()));
							} else if (propertyId.equals("planenddate")) {
								return new FormViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getPlanenddate()));
							} else if (propertyId.equals("actualstartdate")) {
								return new FormViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getActualstartdate()));
							} else if (propertyId.equals("actualenddate")) {
								return new FormViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getActualenddate()));
							} else if (propertyId.equals("homepage")) {
								return new FormUrlLinkViewField(
										ProjectInformationComponent.this.project
												.getHomepage());
							} else if (propertyId.equals("description")) {
								return new FormDetectAndDisplayUrlViewField(
										ProjectInformationComponent.this.project
												.getDescription());
							} else if (propertyId.equals("currencyid")) {
								if (ProjectInformationComponent.this.project
										.getCurrency() != null) {
									return new FormViewField(
											ProjectInformationComponent.this.project
													.getCurrency()
													.getShortname());
								} else {
									return new FormViewField("");
								}
							}
							return null;
						}
					});
			this.previewForm.setBean(ProjectInformationComponent.this.project);
		}
	}

	private interface ProjectDisplayInformation extends Component {
		void show();
	}
}
