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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.ui.RelatedReadItemField;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.module.crm.domain.SimpleTask;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.field.DateTimeViewField;
import com.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class AssignmentReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
    private static final long serialVersionUID = 1L;

    public AssignmentReadFormFieldFactory(GenericBeanForm<SimpleTask> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("assignuser")) {
            return new DefaultViewField(attachForm.getBean().getAssignUserFullName());
        } else if (propertyId.equals("startdate")) {
            if (attachForm.getBean().getStartdate() == null)
                return null;
            return new DateTimeViewField(attachForm.getBean().getStartdate());
        } else if (propertyId.equals("duedate")) {
            if (attachForm.getBean().getDuedate() == null)
                return null;
            return new DateTimeViewField(attachForm.getBean().getDuedate());
        } else if (propertyId.equals("contactid")) {
            return new DefaultViewField(attachForm.getBean().getContactName());
        } else if (propertyId.equals("typeid")) {
            return new RelatedReadItemField(attachForm.getBean());

        }

        return null;
    }

}
