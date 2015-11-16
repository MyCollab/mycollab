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
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionField;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectAddWindow extends Window implements WizardProgressListener {
    private static final long serialVersionUID = 1L;

    private Project project;
    private ProjectAddWizard wizard;
    private GeneralInfoStep infoStep;
    private BillingAccountStep billingAccountStep;

    public ProjectAddWindow() {
        setCaption(AppContext.getMessage(ProjectI18nEnum.VIEW_NEW_TITLE));
        this.setWidth("900px");
        this.center();
        this.setResizable(false);
        this.setModal(true);

        MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, false, true, false));
        setContent(contentLayout);

        project = new Project();

        wizard = new ProjectAddWizard();
        infoStep = new GeneralInfoStep();
        billingAccountStep = new BillingAccountStep();
        wizard.addStep(infoStep);
        wizard.addStep(billingAccountStep);
        wizard.getFinishButton().setEnabled(true);
        wizard.addListener(this);
        contentLayout.with(wizard).withAlign(wizard, Alignment.TOP_CENTER);
    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent wizardStepActivationEvent) {
        wizard.getFinishButton().setEnabled(true);
        if (wizard.isActive(infoStep)) {
            billingAccountStep.commit();
        } else {
            infoStep.commit();
        }
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent wizardStepSetChangedEvent) {
        wizard.getFinishButton().setEnabled(true);
    }


    @Override
    public void wizardCompleted(WizardCompletedEvent wizardCompletedEvent) {
        boolean isInfoValid = infoStep.commit();

        boolean isBillingValid = true;
        if (wizard.isActive(billingAccountStep)) {
            isBillingValid = billingAccountStep.commit();
        }

        if (!isInfoValid || !isBillingValid) {
            return;
        }

        Validator validator = ApplicationContextUtil.getValidator();
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        if (CollectionUtils.isNotEmpty(violations)) {
            StringBuilder errorMsg = new StringBuilder();

            for (ConstraintViolation violation : violations) {
                errorMsg.append(violation.getMessage()).append("<br/>");
            }
            NotificationUtil.showErrorNotification(errorMsg.toString());
        } else {
            project.setSaccountid(AppContext.getAccountId());
            ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
            projectService.saveWithSession(project, AppContext.getUsername());

            EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this,
                    new PageActionChain(new ProjectScreenData.Goto(project.getId()))));
            ProjectAddWindow.this.close();
        }
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent wizardCancelledEvent) {
        this.close();
    }

    private static class ProjectAddWizard extends Wizard {
        ProjectAddWizard() {
            this.getCancelButton().setStyleName(UIConstants.THEME_GRAY_LINK);
            this.getBackButton().setStyleName(UIConstants.THEME_GRAY_LINK);
            this.getNextButton().setStyleName(UIConstants.BUTTON_ACTION);
            this.getFinishButton().setStyleName(UIConstants.BUTTON_ACTION);
            this.footer.setMargin(new MarginInfo(true, true, false, false));
        }

        @Override
        public void finish() {
            if (this.currentStep.onAdvance()) {
                this.fireEvent(new WizardCompletedEvent(this));
            }
        }
    }

    private static class ProjectStatusComboBox extends I18nValueComboBox {
        private static final long serialVersionUID = 1L;

        public ProjectStatusComboBox() {
            super(false, StatusI18nEnum.Open, StatusI18nEnum.Closed);
        }
    }

    private interface FormWizardStep extends WizardStep {
        boolean commit();
    }

    private class GeneralInfoStep implements FormWizardStep {
        private AdvancedEditBeanForm<Project> editForm;
        private EditFormFieldFactory editFormFieldFactory;

        GeneralInfoStep() {
            editForm = new AdvancedEditBeanForm<>();
            editForm.setFormLayoutFactory(buildFormLayout());
            editFormFieldFactory = new EditFormFieldFactory(editForm);
            editForm.setBeanFormFieldFactory(editFormFieldFactory);
            editForm.setBean(project);
        }

        private IDynaFormLayout buildFormLayout() {
            DynaForm defaultForm = new DynaForm();
            DynaSection mainSection = new DynaSectionBuilder().layoutType(DynaSection.LayoutType.TWO_COLUMN).build();

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.name).displayName(AppContext
                    .getMessage(ProjectI18nEnum.FORM_NAME)).fieldIndex(0).mandatory(true).required(true)
                    .build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.homepage).displayName(AppContext
                    .getMessage(ProjectI18nEnum.FORM_HOME_PAGE)).fieldIndex(1).build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.shortname).displayName(AppContext
                    .getMessage(ProjectI18nEnum.FORM_SHORT_NAME)).fieldIndex(2).mandatory(true).required(true).build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.planstartdate).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_PLAN_START_DATE)).fieldIndex(3).build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.projectstatus).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_STATUS)).fieldIndex(4).build());


            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.planenddate).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_PLAN_END_DATE)).fieldIndex(5).build());

            mainSection.addField(new TextAreaDynaFieldBuilder().fieldName(Project.Field.description).displayName
                    (AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION)).fieldIndex(6).colSpan(true).build());
            defaultForm.addSection(mainSection);

            return new DynaFormLayout(defaultForm);
        }

        @Override
        public boolean commit() {
            return editFormFieldFactory.commit();
        }

        @Override
        public String getCaption() {
            return "General";
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

            public EditFormFieldFactory(GenericBeanForm<Project> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (Project.Field.description.equalTo(propertyId)) {
                    return new RichTextEditField();
                } else if (Project.Field.projectstatus.equalTo(propertyId)) {
                    ProjectStatusComboBox projectCombo = new ProjectStatusComboBox();
                    projectCombo.setRequired(true);
                    projectCombo.setRequiredError("Project status must be not null");
                    if (project.getProjectstatus() == null) {
                        project.setProjectstatus(StatusI18nEnum.Open.name());
                    }
                    return projectCombo;
                } else if (Project.Field.shortname.equalTo(propertyId)) {
                    TextField tf = new TextField();
                    tf.setNullRepresentation("");
                    tf.setRequired(true);
                    tf.setRequiredError("Project short name must be not null");
                    return tf;
                } else if (Project.Field.name.equalTo(propertyId)) {
                    TextField tf = new TextField();
                    tf.setNullRepresentation("");
                    tf.setRequired(true);
                    tf.setRequiredError("Project name must be not null");
                    return tf;
                }

                return null;
            }
        }
    }

    private class BillingAccountStep implements FormWizardStep {
        private AdvancedEditBeanForm<Project> editForm;
        private EditFormFieldFactory editFormFieldFactory;

        BillingAccountStep() {
            editForm = new AdvancedEditBeanForm<>();
            editForm.setFormLayoutFactory(buildFormLayout());
            editFormFieldFactory = new EditFormFieldFactory(editForm);
            editForm.setBeanFormFieldFactory(editFormFieldFactory);
            editForm.setBean(project);
        }

        private IDynaFormLayout buildFormLayout() {
            DynaForm defaultForm = new DynaForm();
            DynaSection mainSection = new DynaSectionBuilder().layoutType(DynaSection.LayoutType.TWO_COLUMN).build();

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.account).displayName(AppContext
                    .getMessage(ProjectI18nEnum.FORM_ACCOUNT_NAME)).fieldIndex(0).colSpan(true).build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.currencyid).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_CURRENCY)).fieldIndex(1).build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.targetbudget).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_TARGET_BUDGET)).fieldIndex(2).build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.defaultbillingrate).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_BILLING_RATE)).fieldIndex(3).build());

            mainSection.addField(new TextDynaFieldBuilder().fieldName(Project.Field.defaultovertimebillingrate)
                    .displayName(AppContext.getMessage(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE)).fieldIndex(4).build());

            defaultForm.addSection(mainSection);
            return new DynaFormLayout(defaultForm);
        }

        @Override
        public boolean commit() {
            return editFormFieldFactory.commit();
        }

        @Override
        public String getCaption() {
            return "Account & Billing";
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
            return true;
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Project> {
            private static final long serialVersionUID = 1L;

            public EditFormFieldFactory(GenericBeanForm<Project> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (Project.Field.currencyid.equalTo(propertyId)) {
                    return new CurrencyComboBoxField();
                } else if (Project.Field.account.equalTo(propertyId)) {
                    return new AccountSelectionField();
                }
                return null;
            }
        }
    }
}
