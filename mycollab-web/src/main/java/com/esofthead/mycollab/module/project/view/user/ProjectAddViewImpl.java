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
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.module.file.PathUtils;
import com.esofthead.mycollab.module.file.service.EntityUploaderService;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsUtil;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.user.ui.components.ImagePreviewCropWindow;
import com.esofthead.mycollab.module.user.ui.components.UploadImageField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.web.ui.DoubleField;
import com.esofthead.mycollab.vaadin.web.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.awt.image.BufferedImage;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
@ViewComponent
public class ProjectAddViewImpl extends AbstractPageView implements ProjectAddView {

    private Project project;
    private final AdvancedEditBeanForm<Project> editForm;

    public ProjectAddViewImpl() {
        editForm = new AdvancedEditBeanForm<>();
        addComponent(editForm);
    }

    @Override
    public HasEditFormHandlers<Project> getEditFormHandlers() {
        return editForm;
    }

    @Override
    public void editItem(final Project item) {
        this.project = item;
        editForm.setFormLayoutFactory(new FormLayoutFactory());
        editForm.setBeanFormFieldFactory(new EditFormFieldFactory(editForm));
        editForm.setBean(project);
    }

    class FormLayoutFactory implements IFormLayoutFactory, ImagePreviewCropWindow.ImageSelectionCommand {
        private static final long serialVersionUID = 1L;

        private ProjectInformationLayout projectInformationLayout;

        private Layout createButtonControls() {
            final HorizontalLayout controlPanel = new HorizontalLayout();
            final ComponentContainer controlButtons;

            if (project.getId() == null) {
                controlButtons = (new EditFormControlsGenerator<>(editForm)).createButtonControls();
            } else {
                controlButtons = (new EditFormControlsGenerator<>(editForm)).createButtonControls(true, false, true);
            }
            controlPanel.addComponent(controlButtons);
            controlPanel.setComponentAlignment(controlButtons, Alignment.TOP_RIGHT);
            return controlPanel;
        }

        @Override
        public ComponentContainer getLayout() {
            MHorizontalLayout header = new MHorizontalLayout().withWidth("100%").withMargin(new MarginInfo(true, false, true, false));
            header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            final AddViewLayout projectAddLayout = new AddViewLayout(header);
            projectInformationLayout = new ProjectInformationLayout();
            projectAddLayout.addHeaderTitle(buildHeaderTitle());
            projectAddLayout.addHeaderRight(createButtonControls());
            projectAddLayout.addBody(projectInformationLayout.getLayout());

            return projectAddLayout;
        }

        private MHorizontalLayout buildHeaderTitle() {
            ELabel titleLbl = ELabel.h2(project.getName());
            UploadImageField uploadImageField = new UploadImageField(this);
            uploadImageField.setButtonCaption("Change logo");

            MVerticalLayout logoLayout = new MVerticalLayout(ProjectAssetsUtil.buildProjectLogo(project.getShortname(),
                    project.getId(), project.getAvatarid(), 100),
                    uploadImageField).withMargin(false).withWidth("-1px").alignAll(Alignment.TOP_CENTER);
            return new MHorizontalLayout(logoLayout, titleLbl).expand(titleLbl);
        }

        @Override
        public void attachField(Object propertyId, final Field<?> field) {
            projectInformationLayout.attachField(propertyId, field);
        }

        @Override
        public void process(BufferedImage image) {
            EntityUploaderService entityUploaderService = ApplicationContextUtil.getSpringBean(EntityUploaderService.class);
            String newLogoId = entityUploaderService.upload(image, PathUtils.getProjectLogoPath(AppContext.getAccountId(),
                    project.getId()), project.getAvatarid(), AppContext.getUsername(), AppContext.getAccountId(),
                    new int[]{16, 32, 48, 64, 100});
            ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
            project.setAvatarid(newLogoId);
            projectService.updateSelectiveWithSession(project, AppContext.getUsername());
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }
    }

    private static class ProjectInformationLayout implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;
        private GridFormLayoutHelper financialLayout;
        private GridFormLayoutHelper descriptionLayout;

        @Override
        public ComponentContainer getLayout() {
            final FormContainer layout = new FormContainer();

            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
            layout.addSection(AppContext.getMessage(ProjectI18nEnum.SECTION_PROJECT_INFO), informationLayout.getLayout());

            financialLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 4);
            layout.addSection(AppContext.getMessage(ProjectI18nEnum.SECTION_FINANCE_SCHEDULE), financialLayout.getLayout());

            descriptionLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 1);
            layout.addSection(AppContext.getMessage(ProjectI18nEnum.SECTION_DESCRIPTION), descriptionLayout.getLayout());
            return layout;
        }

        @Override
        public void attachField(Object propertyId, final Field<?> field) {
            if (propertyId.equals("name")) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_NAME), 0, 0);
            } else if (propertyId.equals("homepage")) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE), 1, 0);
            } else if (propertyId.equals("shortname")) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_SHORT_NAME), 0, 1);
            } else if (propertyId.equals("projectstatus")) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_STATUS), 1, 1);
            } else if (Project.Field.lead.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_LEADER), 0, 2);
            } else if (propertyId.equals("planstartdate")) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_START_DATE), 0, 0);
            } else if (Project.Field.accountid.equalTo(propertyId)) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_ACCOUNT_NAME), 1, 0);
            } else if (propertyId.equals("planenddate")) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_END_DATE), 0, 1);
            } else if (Project.Field.currencyid.equalTo(propertyId)) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_CURRENCY), 1, 1);
            } else if (propertyId.equals("defaultbillingrate")) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_BILLING_RATE), 0, 2);
            } else if (Project.Field.defaultovertimebillingrate.equalTo(propertyId)) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE), 1, 2);
            } else if (Project.Field.targetbudget.equalTo(propertyId)) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_TARGET_BUDGET), 0, 3);
            } else if (Project.Field.actualbudget.equalTo(propertyId)) {
                financialLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_ACTUAL_BUDGET), 1, 3);
            } else if (propertyId.equals("description")) {
                descriptionLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 0, 2, "100%");
            }
        }
    }

    private static class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Project> {
        private static final long serialVersionUID = 1L;

        public EditFormFieldFactory(GenericBeanForm<Project> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            Project project = attachForm.getBean();
            if (Project.Field.description.equalTo(propertyId)) {
                final RichTextArea field = new RichTextArea();
                field.setHeight("350px");
                return field;
            } else if (Project.Field.projectstatus.equalTo(propertyId)) {
                final ProjectStatusComboBox projectCombo = new ProjectStatusComboBox();
                projectCombo.setRequired(true);
                projectCombo.setRequiredError("Please enter a project status");
                if (project.getProjectstatus() == null) {
                    project.setProjectstatus(StatusI18nEnum.Open.name());
                }
                return projectCombo;
            } else if (Project.Field.shortname.equalTo(propertyId)) {
                final TextField tf = new TextField();
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("Please enter a project short name");
                return tf;
            } else if (Project.Field.currencyid.equalTo(propertyId)) {
                return new CurrencyComboBoxField();
            } else if (Project.Field.name.equalTo(propertyId)) {
                final TextField tf = new TextField();
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("Please enter a Name");
                return tf;
            } else if (Project.Field.accountid.equalTo(propertyId)) {
                return new AccountSelectionField();
            } else if (Project.Field.lead.equalTo(propertyId)) {
                return new ProjectMemberSelectionField();
            } else if (Project.Field.defaultbillingrate.equalTo(propertyId)
                    || Project.Field.defaultovertimebillingrate.equalTo(propertyId)
                    || Project.Field.targetbudget.equalTo(propertyId)
                    || Project.Field.actualbudget.equalTo(propertyId)) {
                return new DoubleField();
            }

            return null;
        }
    }

    private static class ProjectStatusComboBox extends I18nValueComboBox {
        public ProjectStatusComboBox() {
            super(false, StatusI18nEnum.Open, StatusI18nEnum.Closed);
        }
    }

    @Override
    public Project getItem() {
        return project;
    }
}
