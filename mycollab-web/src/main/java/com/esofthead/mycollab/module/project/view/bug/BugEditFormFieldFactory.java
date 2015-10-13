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

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.module.project.view.bug.components.BugPriorityComboBox;
import com.esofthead.mycollab.module.project.view.bug.components.BugSeverityComboBox;
import com.esofthead.mycollab.module.project.view.milestone.MilestoneComboBox;
import com.esofthead.mycollab.module.project.view.settings.component.ComponentMultiSelectField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.project.view.settings.component.VersionMultiSelectField;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.NumberField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextEditField;
import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
class BugEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleBug> {
    private static final long serialVersionUID = 1L;

    private ComponentMultiSelectField componentSelect;
    private VersionMultiSelectField affectedVersionSelect;
    private VersionMultiSelectField fixedVersionSelect;
    private ProjectSubscribersComp subcribersComp;
    private ProjectFormAttachmentUploadField attachmentUploadField;

    BugEditFormFieldFactory(GenericBeanForm<SimpleBug> form) {
        super(form);
        subcribersComp = new ProjectSubscribersComp(false, CurrentProjectVariables.getProjectId(), AppContext.getUsername());
    }

    @Override
    protected Field<?> onCreateField(final Object propertyId) {
        final SimpleBug beanItem = attachForm.getBean();
        if (propertyId.equals("environment")) {
            return new RichTextEditField();
        } else if (propertyId.equals("description")) {
            return new RichTextEditField();
        } else if (propertyId.equals("priority")) {
            if (beanItem.getPriority() == null) {
                beanItem.setPriority(OptionI18nEnum.BugPriority.Major.name());
            }
            return new BugPriorityComboBox();
        } else if (propertyId.equals("assignuser")) {
            ProjectMemberSelectionField field = new ProjectMemberSelectionField();
            field.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    Property property = valueChangeEvent.getProperty();
                    SimpleProjectMember member = (SimpleProjectMember) property.getValue();
                    if (member != null) {
                        subcribersComp.addFollower(member.getUsername());
                    }
                }
            });
            return field;
        } else if (propertyId.equals("id")) {
            attachmentUploadField = new ProjectFormAttachmentUploadField();
            if (beanItem.getId() != null) {
                attachmentUploadField.getAttachments(beanItem.getProjectid(), ProjectTypeConstants.BUG, beanItem.getId());
            }
            return attachmentUploadField;
        } else if (propertyId.equals("severity")) {
            if (beanItem.getSeverity() == null) {
                beanItem.setSeverity(OptionI18nEnum.BugSeverity.Major.name());
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
            milestoneBox.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    String milestoneName = milestoneBox.getItemCaption(milestoneBox.getValue());
                    beanItem.setMilestoneName(milestoneName);
                }
            });
            return milestoneBox;
        } else if (propertyId.equals("estimatetime") || (propertyId.equals("estimateremaintime"))) {
            return new NumberField();
        } else if (propertyId.equals("selected")) {
            return subcribersComp;
        }

        return null;
    }

    public ComponentMultiSelectField getComponentSelect() {
        return componentSelect;
    }

    public VersionMultiSelectField getAffectedVersionSelect() {
        return affectedVersionSelect;
    }

    public VersionMultiSelectField getFixedVersionSelect() {
        return fixedVersionSelect;
    }

    public ProjectSubscribersComp getSubcribersComp() {
        return subcribersComp;
    }

    public ProjectFormAttachmentUploadField getAttachmentUploadField() {
        return attachmentUploadField;
    }
}
