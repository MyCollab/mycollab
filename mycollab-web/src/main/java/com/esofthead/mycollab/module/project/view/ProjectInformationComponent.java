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
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlLinkViewField;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectInformationComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private SimpleProject project;

	private ProjectDisplayInformation prjDisplay;

	private final MHorizontalLayout projectInfoHeader;

	public ProjectInformationComponent() {
		this.setStyleName(UIConstants.PROJECT_INFO);
		this.prjDisplay = new BasicProjectInformation();
		this.projectInfoHeader = new MHorizontalLayout().withMargin(true).withWidth("100%");
		this.projectInfoHeader
				.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		this.projectInfoHeader.setStyleName(UIConstants.PROJECT_INFO_HEADER);
		this.addComponent(this.projectInfoHeader);
		this.addComponent(this.prjDisplay);

		MHorizontalLayout projectInfoFooter = new MHorizontalLayout().withMargin(true).withStyleName(UIConstants.PROJECT_INFO_FOOTER);
		final Button toggleBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_MORE));
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
					event.getButton().setCaption(
							AppContext.getMessage(GenericI18Enum.BUTTON_LESS));
				} else {
					ProjectInformationComponent.this.prjDisplay = new BasicProjectInformation();
					event.getButton().setCaption(
							AppContext.getMessage(GenericI18Enum.BUTTON_MORE));
				}
				ProjectInformationComponent.this.addComponent(
						ProjectInformationComponent.this.prjDisplay,
						replaceIndex);
				ProjectInformationComponent.this.prjDisplay.show();
			}
		});
		toggleBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		projectInfoFooter.addComponent(toggleBtn);
		this.addComponent(projectInfoFooter);
	}

	public void displayProjectInformation() {
		this.project = CurrentProjectVariables.getProject();
		this.projectInfoHeader.removeAllComponents();
		final Button icon = new Button(null, ProjectAssetsManager.getAsset(ProjectTypeConstants.DASHBOARD));
        icon.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        icon.addStyleName("icon-18px");

		final Label projectName = new Label(this.project.getName());
		projectName.setStyleName(UIConstants.PROJECT_NAME);
		projectName.setSizeUndefined();
		final Label projectShortname = new Label("("
				+ this.project.getShortname() + ")");
		projectShortname.setStyleName(UIConstants.PROJECT_SHORT_NAME);

		this.projectInfoHeader.with(icon, projectName, projectShortname).expand(projectShortname);

		this.prjDisplay.show();
	}

	private class BasicProjectInformation extends VerticalLayout implements
			ProjectDisplayInformation {
		private static final long serialVersionUID = 1L;

		private final AdvancedPreviewBeanForm<SimpleProject> previewForm;

		public BasicProjectInformation() {
			this.previewForm = new AdvancedPreviewBeanForm<>();
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
					if (Project.Field.homepage.equalTo(propertyId)) {
						this.informationLayout.addComponent(field, AppContext
								.getMessage(ProjectI18nEnum.FORM_HOME_PAGE), 0,
								0, Alignment.TOP_LEFT);
					} else if (Project.Field.projectstatus.equalTo(propertyId)) {
						this.informationLayout.addComponent(field, AppContext
										.getMessage(ProjectI18nEnum.FORM_STATUS), 1,
								0, Alignment.TOP_LEFT);
					} else if (SimpleProject.Field.totalBillableHours.equalTo(propertyId)) {
						this.informationLayout.addComponent(field, AppContext
										.getMessage(ProjectI18nEnum.FORM_BILLABLE_HOURS), 0,
								1, Alignment.TOP_LEFT);
					} else if (SimpleProject.Field.totalNonBillableHours.equalTo(propertyId)) {
						this.informationLayout.addComponent(field, AppContext
										.getMessage(ProjectI18nEnum.FORM_NON_BILLABLE_HOURS), 1,
								1, Alignment.TOP_LEFT);
					} else if (Project.Field.description.equalTo(propertyId)) {
						this.informationLayout.addComponent(field, AppContext
								.getMessage(GenericI18Enum.FORM_DESCRIPTION),
								0, 2, 2, "100%", Alignment.TOP_LEFT);
					}
				}

				@Override
				public ComponentContainer getLayout() {
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
							if (propertyId.equals("homepage")) {
								return new UrlLinkViewField(
										ProjectInformationComponent.this.project
												.getHomepage());
							} else if (propertyId.equals("description")) {
								return new RichTextViewField(
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
			this.previewForm = new AdvancedPreviewBeanForm<>();
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
								return new DefaultViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getPlanstartdate()));
							} else if (propertyId.equals("planenddate")) {
								return new DefaultViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getPlanenddate()));
							} else if (propertyId.equals("actualstartdate")) {
								return new DefaultViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getActualstartdate()));
							} else if (propertyId.equals("actualenddate")) {
								return new DefaultViewField(
										AppContext
												.formatDate(ProjectInformationComponent.this.project
														.getActualenddate()));
							} else if (propertyId.equals("homepage")) {
								return new UrlLinkViewField(
										ProjectInformationComponent.this.project
												.getHomepage());
							} else if (propertyId.equals("description")) {
								return new RichTextViewField(
										ProjectInformationComponent.this.project
												.getDescription());
							} else if (propertyId.equals("currencyid")) {
								if (ProjectInformationComponent.this.project
										.getCurrency() != null) {
									return new DefaultViewField(
											ProjectInformationComponent.this.project
													.getCurrency()
													.getShortname());
								} else {
									return new DefaultViewField("");
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
