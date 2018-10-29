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
package com.mycollab.module.project.view.milestone;

import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

import java.util.Arrays;

import static com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus.*;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
// TODO
public class MilestoneEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleMilestone> {
    private static final long serialVersionUID = 1L;

    private AttachmentUploadField attachmentUploadField;

    MilestoneEditFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
        super(form);
    }

    @Override
    protected HasValue<?> onCreateField(Object propertyId) {
        if (Milestone.Field.assignuser.equalTo(propertyId)) {
            ProjectMemberSelectionField memberSelectionField = new ProjectMemberSelectionField();
//            memberSelectionField.setRequired(true);
//            memberSelectionField.setRequiredError("Please select an assignee");
            return memberSelectionField;
        } else if (propertyId.equals("status")) {
            return new ProgressStatusComboBox();
        } else if (propertyId.equals("name")) {
            final TextField tf = new TextField();
//            if (isValidateForm) {
//                tf.setNullRepresentation("");
//                tf.setRequired(true);
//                tf.setRequiredError("Please enter name");
//            }
            return tf;
        } else if (propertyId.equals("description")) {
            return new RichTextArea();
        } else if (Milestone.Field.saccountid.equalTo(propertyId)) {
            Milestone beanItem = attachForm.getBean();
            if (beanItem.getId() != null) {
                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(),
                        beanItem.getProjectid(), ProjectTypeConstants.MILESTONE, "" + beanItem.getId());
                attachmentUploadField = new AttachmentUploadField(attachmentPath);
            } else {
                attachmentUploadField = new AttachmentUploadField();
            }
            return attachmentUploadField;
        }

        return null;
    }

    public AttachmentUploadField getAttachmentUploadField() {
        return attachmentUploadField;
    }

    private static class ProgressStatusComboBox extends I18nValueComboBox {
        private static final long serialVersionUID = 1L;

        ProgressStatusComboBox() {
            setCaption(null);
            this.setEmptySelectionAllowed(false);
            this.loadData(Arrays.asList(InProgress, Future, Closed));
            this.setItemIconGenerator((IconGenerator<String>) it -> {
                if (it.equals(InProgress.name())) {
                    return VaadinIcons.SPINNER;
                } else if (it.equals(Future.name())) {
                    return VaadinIcons.CLOCK;
                } else {
                    return VaadinIcons.MINUS_CIRCLE;
                }
            });
        }

//        @Override
//        public void setPropertyDataSource(Property newDataSource) {
//            Object value = newDataSource.getValue();
//            if (value == null) {
//                newDataSource.setValue(MilestoneStatus.InProgress.name());
//            }
//            super.setPropertyDataSource(newDataSource);
//        }
    }

}
