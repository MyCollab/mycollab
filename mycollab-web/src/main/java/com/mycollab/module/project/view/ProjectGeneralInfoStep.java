/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.IDynaFormLayout;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
public class ProjectGeneralInfoStep implements AbstractProjectAddWindow.FormWizardStep {
    private Project project;
    private AdvancedEditBeanForm<Project> editForm;
    private EditFormFieldFactory editFormFieldFactory;

    public ProjectGeneralInfoStep(Project project) {
        this.project = project;
        editForm = new AdvancedEditBeanForm<>();
        editForm.setFormLayoutFactory(buildFormLayout());
        editFormFieldFactory = new EditFormFieldFactory(editForm);
        editForm.setBeanFormFieldFactory(editFormFieldFactory);
        editForm.setBean(project);
    }

    private IDynaFormLayout buildFormLayout() {
        DynaForm defaultForm = new DynaForm();
        DynaSection mainSection = new DynaSectionBuilder().layoutType(DynaSection.LayoutType.TWO_COLUMN).build();

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.name).displayName(GenericI18Enum.FORM_NAME)
                .fieldIndex(0).mandatory(true).required(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.homepage).displayName(ProjectI18nEnum.FORM_HOME_PAGE).fieldIndex(1).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.shortname).displayName(ProjectI18nEnum.FORM_SHORT_NAME)
                .contextHelp(ProjectI18nEnum.FORM_SHORT_NAME_HELP).fieldIndex(2).mandatory(true).required(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.planstartdate).displayName
                (GenericI18Enum.FORM_START_DATE).fieldIndex(3).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.projectstatus).displayName
                (GenericI18Enum.FORM_STATUS).fieldIndex(4).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.planenddate).displayName
                (GenericI18Enum.FORM_END_DATE).fieldIndex(5).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.memlead).displayName
                (ProjectI18nEnum.FORM_LEADER).fieldIndex(6).build());

        mainSection.fields(new TextAreaDynaFieldBuilder().fieldName(Project.Field.description).displayName
                (GenericI18Enum.FORM_DESCRIPTION).fieldIndex(7).colSpan(true).build());
        defaultForm.sections(mainSection);

        return new DefaultDynaFormLayout(defaultForm);
    }

    @Override
    public boolean commit() {
        return editFormFieldFactory.commit();
    }

    @Override
    public String getCaption() {
        return UserUIContext.getMessage(ProjectI18nEnum.OPT_GENERAL);
    }

    @Override
    public Component getContent() {
        return editForm;
    }

    @Override
    public boolean onAdvance() {
        return true;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Project> {
        private static final long serialVersionUID = 1L;

        EditFormFieldFactory(GenericBeanForm<Project> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (Project.Field.description.equalTo(propertyId)) {
                return new RichTextArea();
            } else if (Project.Field.projectstatus.equalTo(propertyId)) {
                ProjectStatusComboBox projectCombo = new ProjectStatusComboBox();
                projectCombo.setRequired(true);
                projectCombo.setRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                        UserUIContext.getMessage(GenericI18Enum.FORM_STATUS)));
                if (project.getProjectstatus() == null) {
                    project.setProjectstatus(StatusI18nEnum.Open.name());
                }
                return projectCombo;
            } else if (Project.Field.shortname.equalTo(propertyId)) {
                TextField tf = new TextField();
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                        UserUIContext.getMessage(ProjectI18nEnum.FORM_SHORT_NAME)));
                return tf;
            } else if (Project.Field.name.equalTo(propertyId)) {
                TextField tf = new TextField();
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                        UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
                return tf;
            } else if (Project.Field.memlead.equalTo(propertyId)) {
                return new ActiveUserComboBox();
            }

            return null;
        }
    }
}
