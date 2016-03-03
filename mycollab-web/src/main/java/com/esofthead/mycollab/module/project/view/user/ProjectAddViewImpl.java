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

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.CurrencyComboBoxField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.ui.*;

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

    class FormLayoutFactory extends ProjectFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        public FormLayoutFactory() {
            super((project.getId() == null) ? AppContext.getMessage(ProjectI18nEnum.VIEW_NEW_TITLE) : project.getName());
        }

        private Layout createButtonControls() {
            final HorizontalLayout controlPanel = new HorizontalLayout();
            final Layout controlButtons;

            if (project.getId() == null) {
                controlButtons = (new EditFormControlsGenerator<>(editForm)).createButtonControls();
            } else {
                controlButtons = (new EditFormControlsGenerator<>(editForm)).createButtonControls(true, false, true);
            }
            controlButtons.setSizeUndefined();
            controlPanel.addComponent(controlButtons);
            controlPanel.setWidthUndefined();
            controlPanel.setComponentAlignment(controlButtons, Alignment.MIDDLE_CENTER);

            return controlPanel;
        }

        @Override
        protected Layout createTopPanel() {
            return this.createButtonControls();
        }

        @Override
        protected Layout createBottomPanel() {
            return null;
        }
    }

    private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Project> {
        private static final long serialVersionUID = 1L;

        public EditFormFieldFactory(GenericBeanForm<Project> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
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
