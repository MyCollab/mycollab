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
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.form.field.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectInformationComponent extends VerticalLayout {
    private static final long serialVersionUID = 1L;

    private SimpleProject project;

    private ProjectDisplayInformation prjDisplay;
    private MHorizontalLayout projectInfoHeader;

    public ProjectInformationComponent() {
        setStyleName(UIConstants.PROJECT_INFO);
        prjDisplay = new BasicProjectInformation();
        projectInfoHeader = new MHorizontalLayout().withMargin(true).withWidth("100%");
        projectInfoHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        projectInfoHeader.setStyleName(UIConstants.PROJECT_INFO_HEADER);
        this.addComponent(projectInfoHeader);
        this.addComponent(prjDisplay);

        MHorizontalLayout projectInfoFooter = new MHorizontalLayout().withMargin(true).withStyleName(UIConstants.PROJECT_INFO_FOOTER);
        Button toggleBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_MORE));
        toggleBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                int replaceIndex = ProjectInformationComponent.this
                        .getComponentIndex(ProjectInformationComponent.this.prjDisplay);
                ProjectInformationComponent.this
                        .removeComponent(ProjectInformationComponent.this.prjDisplay);
                if (prjDisplay instanceof BasicProjectInformation) {
                    prjDisplay = new DetailProjectInformation();
                    event.getButton().setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_LESS));
                } else {
                    prjDisplay = new BasicProjectInformation();
                    event.getButton().setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_MORE));
                }
                ProjectInformationComponent.this.addComponent(prjDisplay, replaceIndex);
                prjDisplay.show();
            }
        });
        toggleBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        projectInfoFooter.addComponent(toggleBtn);
        addComponent(projectInfoFooter);
    }

    public void displayProjectInformation() {
        project = CurrentProjectVariables.getProject();
        projectInfoHeader.removeAllComponents();
        String projHeader = String.format("%s [%s] %s", ProjectAssetsManager.getAsset(ProjectTypeConstants.DASHBOARD).getHtml(),
                project.getShortname(), project.getName());

        Label projectName = new Label(projHeader, ContentMode.HTML);
        projectName.setStyleName(UIConstants.PROJECT_NAME);

        projectInfoHeader.with(projectName);
        prjDisplay.show();
    }

    private class BasicProjectInformation extends VerticalLayout implements
            ProjectDisplayInformation {
        private static final long serialVersionUID = 1L;

        private final AdvancedPreviewBeanForm<SimpleProject> previewForm;

        public BasicProjectInformation() {
            previewForm = new AdvancedPreviewBeanForm<>();
            addComponent(previewForm);
        }

        @Override
        public void show() {
            this.previewForm.setFormLayoutFactory(new IFormLayoutFactory() {
                private static final long serialVersionUID = 1L;
                private GridFormLayoutHelper informationLayout;

                @Override
                public void attachField(Object propertyId, Field<?> field) {
                    if (Project.Field.homepage.equalTo(propertyId)) {
                        informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE), 0, 0);
                    } else if (Project.Field.projectstatus.equalTo(propertyId)) {
                        informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_STATUS), 1, 0);
                    } else if (SimpleProject.Field.totalBillableHours.equalTo(propertyId)) {
                        informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_BILLABLE_HOURS), 0, 1);
                    } else if (SimpleProject.Field.totalNonBillableHours.equalTo(propertyId)) {
                        informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_NON_BILLABLE_HOURS), 1, 1);
                    } else if (Project.Field.description.equalTo(propertyId)) {
                        informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 2, 2, "100%");
                    }
                }

                @Override
                public ComponentContainer getLayout() {
                    informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
                    return informationLayout.getLayout();
                }
            });
            this.previewForm.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<SimpleProject>(previewForm) {
                private static final long serialVersionUID = 1L;

                @Override
                protected Field<?> onCreateField(Object propertyId) {
                    if (propertyId.equals("homepage")) {
                        return new UrlLinkViewField(project.getHomepage());
                    } else if (Project.Field.description.equalTo(propertyId)) {
                        return new RichTextViewField(project.getDescription());
                    } else if (SimpleProject.Field.totalBillableHours.equalTo(propertyId)) {
                        return new RoundNumberField(project.getTotalBillableHours());
                    } else if (SimpleProject.Field.totalNonBillableHours.equalTo(propertyId)) {
                        return new RoundNumberField(project.getTotalNonBillableHours());
                    }
                    return null;
                }
            });
            previewForm.setBean(project);
        }
    }

    private class DetailProjectInformation extends VerticalLayout implements ProjectDisplayInformation {
        private static final long serialVersionUID = 1L;
        private final AdvancedPreviewBeanForm<SimpleProject> previewForm;

        public DetailProjectInformation() {
            previewForm = new AdvancedPreviewBeanForm<>();
            addComponent(previewForm);
        }

        @Override
        public void show() {
            this.previewForm.setFormLayoutFactory(new ProjectInformationLayout());
            this.previewForm.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<SimpleProject>(previewForm) {
                private static final long serialVersionUID = 1L;

                @Override
                protected Field<?> onCreateField(Object propertyId) {
                    if (propertyId.equals("planstartdate")) {
                        return new DateViewField(project.getPlanstartdate());
                    } else if (propertyId.equals("planenddate")) {
                        return new DateViewField(project.getPlanenddate());
                    } else if (propertyId.equals("actualstartdate")) {
                        return new DateViewField(project.getActualstartdate());
                    } else if (propertyId.equals("actualenddate")) {
                        return new DateViewField(project.getActualenddate());
                    } else if (propertyId.equals("homepage")) {
                        return new UrlLinkViewField(project.getHomepage());
                    } else if (propertyId.equals("description")) {
                        return new RichTextViewField(project.getDescription());
                    } else if (propertyId.equals("currencyid")) {
                        if (project.getCurrency() != null) {
                            return new DefaultViewField(project.getCurrency().getShortname());
                        } else {
                            return new DefaultViewField("");
                        }
                    } else if (SimpleProject.Field.totalBillableHours.equalTo(propertyId)) {
                        return new RoundNumberField(project.getTotalBillableHours());
                    } else if (SimpleProject.Field.totalNonBillableHours.equalTo(propertyId)) {
                        return new RoundNumberField(project.getTotalNonBillableHours());
                    }
                    return null;
                }
            });
            previewForm.setBean(project);
        }
    }

    private interface ProjectDisplayInformation extends Component {
        void show();
    }
}
