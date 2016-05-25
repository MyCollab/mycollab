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
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.DoubleField;
import com.esofthead.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
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
        this(new Project());
    }

    public ProjectAddWindow(Project valuePrj) {
        setCaption(AppContext.getMessage(ProjectI18nEnum.NEW));
        this.setWidth("900px");
        this.center();
        this.setResizable(false);
        this.setModal(true);

        MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, false, true, false));
        setContent(contentLayout);

        project = valuePrj;

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

        Validator validator = AppContextUtil.getValidator();
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        if (CollectionUtils.isNotEmpty(violations)) {
            StringBuilder errorMsg = new StringBuilder();

            for (ConstraintViolation violation : violations) {
                errorMsg.append(violation.getMessage()).append("<br/>");
            }
            NotificationUtil.showErrorNotification(errorMsg.toString());
        } else {
            project.setSaccountid(AppContext.getAccountId());
            ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
            projectService.saveWithSession(project, AppContext.getUsername());

            EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this,
                    new PageActionChain(new ProjectScreenData.Goto(project.getId()))));
            if (project.getLead() != null && !AppContext.getUsername().equals(project.getLead())) {
                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                projectMemberService.inviteProjectMembers(new String[]{project.getLead()}, CurrentProjectVariables.getProjectId(),
                        -1, AppContext.getUsername(), AppContext.getMessage(ProjectMemberI18nEnum
                                .MSG_DEFAULT_INVITATION_COMMENT), AppContext.getAccountId());
            }
            close();
        }
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent wizardCancelledEvent) {
        this.close();
    }

    private class ProjectAddWizard extends Wizard {
        ProjectAddWizard() {
            this.getCancelButton().setStyleName(UIConstants.BUTTON_OPTION);
            this.getBackButton().setStyleName(UIConstants.BUTTON_OPTION);
            this.getNextButton().setStyleName(UIConstants.BUTTON_ACTION);
            this.getFinishButton().setStyleName(UIConstants.BUTTON_ACTION);
            this.footer.setMargin(new MarginInfo(true, true, false, false));

            Button newProjectFromTemplateBtn = new Button("New project from template", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    ProjectAddWindow.this.close();
                    UI.getCurrent().addWindow(new ProjectAddBaseTemplateWindow());
                }
            });
            newProjectFromTemplateBtn.addStyleName(UIConstants.BUTTON_ACTION);
            footer.addComponent(newProjectFromTemplateBtn, 0);
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

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.name).displayName(AppContext
                    .getMessage(GenericI18Enum.FORM_NAME)).fieldIndex(0).mandatory(true).required(true).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.homepage).displayName(AppContext
                    .getMessage(ProjectI18nEnum.FORM_HOME_PAGE)).fieldIndex(1).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.shortname).displayName(AppContext
                    .getMessage(ProjectI18nEnum.FORM_SHORT_NAME)).contextHelp(AppContext.getMessage(ProjectI18nEnum
                    .FORM_SHORT_NAME_HELP)).fieldIndex(2).mandatory(true).required(true).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.planstartdate).displayName
                    (AppContext.getMessage(GenericI18Enum.FORM_START_DATE)).fieldIndex(3).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.projectstatus).displayName
                    (AppContext.getMessage(GenericI18Enum.FORM_STATUS)).fieldIndex(4).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.planenddate).displayName
                    (AppContext.getMessage(GenericI18Enum.FORM_END_DATE)).fieldIndex(5).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.lead).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_LEADER)).fieldIndex(6).build());

            mainSection.fields(new TextAreaDynaFieldBuilder().fieldName(Project.Field.description).displayName
                    (AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION)).fieldIndex(7).colSpan(true).build());
            defaultForm.sections(mainSection);

            return new DefaultDynaFormLayout(defaultForm);
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
                    return new RichTextArea();
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
                } else if (Project.Field.lead.equalTo(propertyId)) {
                    return new ActiveUserComboBox();
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

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.accountid)
                    .displayName(AppContext.getMessage(ProjectI18nEnum.FORM_ACCOUNT_NAME))
                    .contextHelp(AppContext.getMessage(ProjectI18nEnum.FORM_ACCOUNT_NAME_HELP))
                    .fieldIndex(0).colSpan(true).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.currencyid)
                    .displayName(AppContext.getMessage(GenericI18Enum.FORM_CURRENCY))
                    .contextHelp(AppContext.getMessage(ProjectI18nEnum.FORM_CURRENCY_HELP)).fieldIndex(1).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.targetbudget).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_TARGET_BUDGET))
                    .contextHelp(AppContext.getMessage(ProjectI18nEnum.FORM_TARGET_BUDGET_HELP)).fieldIndex(2).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.defaultbillingrate).displayName
                    (AppContext.getMessage(ProjectI18nEnum.FORM_BILLING_RATE))
                    .contextHelp(AppContext.getMessage(ProjectI18nEnum.FORM_BILLING_RATE_HELP)).fieldIndex(3).build());

            mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.defaultovertimebillingrate)
                    .displayName(AppContext.getMessage(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE))
                    .contextHelp(AppContext.getMessage(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE_HELP)).fieldIndex(4).build());

            defaultForm.sections(mainSection);
            return new DefaultDynaFormLayout(defaultForm);
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
                } else if (Project.Field.accountid.equalTo(propertyId)) {
                    return new AccountSelectionField();
                } else if (Project.Field.targetbudget.equalTo(propertyId)
                        || Project.Field.defaultbillingrate.equalTo(propertyId)
                        || Project.Field.defaultovertimebillingrate.equalTo(propertyId)) {
                    return new DoubleField();
                }
                return null;
            }
        }
    }
}
