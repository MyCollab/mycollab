/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.user;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.AddViewLayout;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.fields.DoubleField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectAddViewImpl extends AbstractVerticalPageView implements ProjectAddView {

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

    class FormLayoutFactory extends AbstractFormLayoutFactory {

        private ProjectInformationLayout projectInformationLayout;

        private Layout createButtonControls() {
            final HorizontalLayout controlPanel = new HorizontalLayout();
            final ComponentContainer controlButtons;

            if (project.getId() == null) {
                controlButtons = generateEditFormControls(editForm);
            } else {
                controlButtons = generateEditFormControls(editForm, true, false, true);
            }
            controlPanel.addComponent(controlButtons);
            controlPanel.setComponentAlignment(controlButtons, Alignment.TOP_RIGHT);
            return controlPanel;
        }

        @Override
        public AbstractComponent getLayout() {
            MHorizontalLayout header = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(true, false, true, false));
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

            MVerticalLayout logoLayout = new MVerticalLayout(ProjectAssetsUtil.projectLogoComp(project.getShortname(),
                    project.getId(), project.getAvatarid(), 32))
                    .withMargin(false).withWidth("-1px").alignAll(Alignment.TOP_CENTER);
            return new MHorizontalLayout(logoLayout, titleLbl).expand(titleLbl);
        }

        @Override
        public HasValue<?> onAttachField(Object propertyId, final HasValue<?> field) {
            return projectInformationLayout.onAttachField(propertyId, field);
        }
    }

    private static class ProjectInformationLayout extends AbstractFormLayoutFactory {
        private IFormLayoutFactory formLayoutFactory;

        @Override
        public AbstractComponent getLayout() {
            if (SiteConfiguration.isCommunityEdition())
                formLayoutFactory = new DefaultDynaFormLayout(ProjectTypeConstants.PROJECT, ProjectDefaultFormLayoutFactory.getAddForm(), Project.Field.clientid.name());
            else
                formLayoutFactory = new DefaultDynaFormLayout(ProjectTypeConstants.PROJECT, ProjectDefaultFormLayoutFactory.getAddForm());
            return formLayoutFactory.getLayout();
        }

        @Override
        protected HasValue<?> onAttachField(Object propertyId, HasValue<?> field) {
            return formLayoutFactory.attachField(propertyId, field);
        }
    }

    private static class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Project> {
        private static final long serialVersionUID = 1L;

        EditFormFieldFactory(GenericBeanForm<Project> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(final Object propertyId) {
            Project project = attachForm.getBean();
            if (Project.Field.description.equalTo(propertyId)) {
                final RichTextArea field = new RichTextArea();
                field.setHeight("350px");
                return field;
            } else if (Project.Field.status.equalTo(propertyId)) {
                final ProjectStatusComboBox statusSelection = new ProjectStatusComboBox();
                statusSelection.setRequiredIndicatorVisible(true);
                statusSelection.setWidth(WebThemes.FORM_CONTROL_WIDTH);
                if (project.getStatus() == null) {
                    project.setStatus(StatusI18nEnum.Open.name());
                }
                return statusSelection;
            } else if (Project.Field.shortname.equalTo(propertyId)) {
                return new MTextField().withWidth(WebThemes.FORM_CONTROL_WIDTH).withRequiredIndicatorVisible(true);
            } else if (Project.Field.currencyid.equalTo(propertyId)) {
                return new CurrencyComboBoxField();
            } else if (Project.Field.name.equalTo(propertyId)) {
                return new MTextField().withRequiredIndicatorVisible(true);
            } else if (Project.Field.clientid.equalTo(propertyId)) {
                return UIUtils.getComponent("com.mycollab.pro.module.project.view.client.ClientSelectionField");
            } else if (Project.Field.memlead.equalTo(propertyId)) {
                return new ProjectMemberSelectionField();
            } else if (Project.Field.defaultbillingrate.equalTo(propertyId)
                    || Project.Field.defaultovertimebillingrate.equalTo(propertyId)
                    || Project.Field.targetbudget.equalTo(propertyId)
                    || Project.Field.actualbudget.equalTo(propertyId)) {
                return new DoubleField().withWidth(WebThemes.FORM_CONTROL_WIDTH);
            } else if (Project.Field.planstartdate.equalTo(propertyId) || Project.Field.planenddate.equalTo(propertyId)) {
                return new DateField();
            }

            return null;
        }
    }

    private static class ProjectStatusComboBox extends I18nValueComboBox<StatusI18nEnum> {
        ProjectStatusComboBox() {
            super(StatusI18nEnum.class, StatusI18nEnum.Open, StatusI18nEnum.Closed);
        }
    }

    @Override
    public Project getItem() {
        return project;
    }
}
