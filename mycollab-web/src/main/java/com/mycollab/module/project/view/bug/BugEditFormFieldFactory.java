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
package com.mycollab.module.project.view.bug;

import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.ui.components.PriorityComboBox;
import com.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.mycollab.module.project.view.milestone.MilestoneComboBox;
import com.mycollab.module.project.view.settings.component.ComponentMultiSelectField;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.module.project.view.settings.component.VersionMultiSelectField;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.data.HasValue;
import com.vaadin.ui.DateField;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.fields.DoubleField;
import org.vaadin.viritin.fields.MTextField;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
class BugEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleBug> {
    private static final long serialVersionUID = 1L;

    private ComponentMultiSelectField componentSelect;
    private VersionMultiSelectField affectedVersionSelect;
    private VersionMultiSelectField fixedVersionSelect;
    private ProjectSubscribersComp subscribersComp;
    private AttachmentUploadField attachmentUploadField;

    BugEditFormFieldFactory(GenericBeanForm<SimpleBug> form, Integer prjId) {
        super(form);
        subscribersComp = new ProjectSubscribersComp(false, prjId, UserUIContext.getUsername());
    }

    public BugEditFormFieldFactory(GenericBeanForm<SimpleBug> form) {
        super(form);
    }

    @Override
    protected HasValue<?> onCreateField(final Object propertyId) {
        final SimpleBug beanItem = attachForm.getBean();
        if (propertyId.equals("environment")) {
            return new RichTextArea();
        } else if (propertyId.equals("description")) {
            return new RichTextArea();
        } else if (propertyId.equals("priority")) {
            return new PriorityComboBox();
        } else if (propertyId.equals("assignuser")) {
            ProjectMemberSelectionField field = new ProjectMemberSelectionField();
            field.addValueChangeListener(valueChangeEvent -> {
                String username = valueChangeEvent.getValue();
                if (username != null) {
                    subscribersComp.addFollower(username);
                }
            });
            return field;
        } else if (propertyId.equals("id")) {
            if (beanItem.getId() != null) {
                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(),
                        beanItem.getProjectid(), ProjectTypeConstants.BUG, "" + beanItem.getId());
                attachmentUploadField = new AttachmentUploadField(attachmentPath);
            } else {
                attachmentUploadField = new AttachmentUploadField();
            }
            return attachmentUploadField;
        } else if (propertyId.equals("severity")) {
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
        } else if (propertyId.equals("name")) {
            return new MTextField().withRequiredIndicatorVisible(true);
        } else if (propertyId.equals("milestoneid")) {
            return new MilestoneComboBox();
        } else if (BugWithBLOBs.Field.originalestimate.equalTo(propertyId) ||
                (BugWithBLOBs.Field.remainestimate.equalTo(propertyId))) {
            return new DoubleField().withWidth(WebThemes.FORM_CONTROL_WIDTH);
        } else if (propertyId.equals("selected")) {
            return subscribersComp;
        } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId) || BugWithBLOBs.Field.enddate.equalTo(propertyId)
                || BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
            return new DateField();
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

    public ProjectSubscribersComp getSubscribersComp() {
        return subscribersComp;
    }

    public AttachmentUploadField getAttachmentUploadField() {
        return attachmentUploadField;
    }
}
