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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.module.project.view.bug.components.BugPriorityComboBox;
import com.esofthead.mycollab.module.project.view.bug.components.BugSeverityComboBox;
import com.esofthead.mycollab.module.project.view.bug.components.ComponentMultiSelectField;
import com.esofthead.mycollab.module.project.view.bug.components.VersionMultiSelectField;
import com.esofthead.mycollab.module.project.view.milestone.MilestoneComboBox;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextEditField;
import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class BugAddViewImpl extends AbstractEditItemComp<SimpleBug> implements
        BugAddView {

    private static final long serialVersionUID = 1L;

    private ProjectFormAttachmentUploadField attachmentUploadField;

    private ComponentMultiSelectField componentSelect;
    private VersionMultiSelectField affectedVersionSelect;
    private VersionMultiSelectField fixedVersionSelect;

    @Override
    public ProjectFormAttachmentUploadField getAttachUploadField() {
        return this.attachmentUploadField;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Component> getComponents() {
        return this.componentSelect.getSelectedItems();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Version> getAffectedVersions() {
        return this.affectedVersionSelect.getSelectedItems();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Version> getFixedVersion() {
        return this.fixedVersionSelect.getSelectedItems();
    }

    private class EditFormFieldFactory extends
            AbstractBeanFieldGroupEditFieldFactory<SimpleBug> {

        private static final long serialVersionUID = 1L;

        public EditFormFieldFactory(GenericBeanForm<SimpleBug> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {

            if (propertyId.equals("environment")) {
                return new RichTextEditField();
            } else if (propertyId.equals("description")) {
                return new RichTextEditField();
            } else if (propertyId.equals("priority")) {
                if (beanItem.getPriority() == null) {
                    beanItem.setPriority(BugPriority.Major.name());
                }
                return new BugPriorityComboBox();
            } else if (propertyId.equals("assignuser")) {
                return new ProjectMemberSelectionField();
            } else if (propertyId.equals("id")) {
                attachmentUploadField = new ProjectFormAttachmentUploadField();
                if (beanItem.getId() != null) {
                    attachmentUploadField.getAttachments(
                            beanItem.getProjectid(),
                            AttachmentType.PROJECT_BUG_TYPE, beanItem.getId());
                }
                return attachmentUploadField;
            } else if (propertyId.equals("severity")) {
                if (beanItem.getSeverity() == null) {
                    beanItem.setSeverity(BugSeverity.Major.name());
                }
                return new BugSeverityComboBox();
            } else if (propertyId.equals("components")) {
                componentSelect = new ComponentMultiSelectField();
                return componentSelect;
            } else if (propertyId.equals("affectedVersions")) {
                affectedVersionSelect = new VersionMultiSelectField();
                return affectedVersionSelect;
            } else if (propertyId.equals("fixedVersions")) {
                fixedVersionSelect = new VersionMultiSelectField();
                return fixedVersionSelect;
            } else if (propertyId.equals("summary")) {
                final TextField tf = new TextField();
                if (isValidateForm) {
                    tf.setNullRepresentation("");
                    tf.setRequired(true);
                    tf.setRequiredError("Summary must be not null");
                }

                return tf;
            } else if (propertyId.equals("milestoneid")) {
                final MilestoneComboBox milestoneBox = new MilestoneComboBox();
                milestoneBox
                        .addValueChangeListener(new Property.ValueChangeListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void valueChange(
                                    Property.ValueChangeEvent event) {
                                String milestoneName = milestoneBox
                                        .getItemCaption(milestoneBox.getValue());
                                beanItem.setMilestoneName(milestoneName);
                            }
                        });
                return milestoneBox;
            } else if (propertyId.equals("estimatetime")
                    || (propertyId.equals("estimateremaintime"))) {
                return new NumberField();
            }

            return null;
        }
    }

    @Override
    public HasEditFormHandlers<SimpleBug> getEditFormHandlers() {
        return this.editForm;
    }

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? AppContext
                .getMessage(BugI18nEnum.FORM_NEW_BUG_TITLE) : AppContext
                .getMessage(BugI18nEnum.FORM_EDIT_BUG_TITLE);
    }

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getSummary();
    }

    @Override
    protected Resource initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return (new EditFormControlsGenerator<>(editForm)).createButtonControls();
    }

    @Override
    protected AdvancedEditBeanForm<SimpleBug> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new BugAddFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<SimpleBug> initBeanFormFieldFactory() {
        return new EditFormFieldFactory(editForm);
    }
}
