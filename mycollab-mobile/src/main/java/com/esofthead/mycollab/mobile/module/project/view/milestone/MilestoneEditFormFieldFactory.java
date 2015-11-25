/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.milestone;

import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import java.util.Arrays;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MilestoneEditFormFieldFactory<B extends Milestone> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    MilestoneEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("owner")) {
            final ProjectMemberSelectionField userbox = new ProjectMemberSelectionField();
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
        } else if (propertyId.equals("startdate") || propertyId.equals("enddate")) {
            return new DatePicker();
        } else if (propertyId.equals("description")) {
            final TextArea descArea = new TextArea();
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
        }
    }

}
