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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

import java.util.Arrays;

/**
 * @param
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MilestoneEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleMilestone> {
    private static final long serialVersionUID = 1L;

    MilestoneEditFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("owner")) {
            ProjectMemberSelectionField userbox = new ProjectMemberSelectionField();
            userbox.setRequired(true);
            userbox.setRequiredError("Please select an assignee");
            return userbox;
        } else if (propertyId.equals("status")) {
            if (attachForm.getBean().getStatus() == null) {
                attachForm.getBean().setStatus(MilestoneStatus.InProgress.toString());
            }
            return new ProgressStatusComboBox();
        } else if (propertyId.equals("name")) {
            final TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("Please enter name");
            }
            return tf;
        } else if (propertyId.equals("description")) {
            RichTextArea descArea = new RichTextArea();
            descArea.setNullRepresentation("");
            return descArea;
        }

        return null;
    }

    private static class ProgressStatusComboBox extends I18nValueComboBox {
        private static final long serialVersionUID = 1L;

        public ProgressStatusComboBox() {
            super();
            setCaption(null);
            this.setNullSelectionAllowed(false);
            this.loadData(Arrays.asList(MilestoneStatus.InProgress, MilestoneStatus.Future, MilestoneStatus.Closed));

            this.setItemIcon(MilestoneStatus.InProgress.name(), FontAwesome.SPINNER);
            this.setItemIcon(MilestoneStatus.Future.name(), FontAwesome.CLOCK_O);
            this.setItemIcon(MilestoneStatus.Closed.name(), FontAwesome.MINUS);
        }
    }

}
