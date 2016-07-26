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
package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.EmailViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class CaseReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCase> {
    private static final long serialVersionUID = 1L;

    public CaseReadFormFieldFactory(GenericBeanForm<SimpleCase> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        final SimpleCase cases = attachForm.getBean();
        if (propertyId.equals("accountid")) {
            return new DefaultViewField(cases.getAccountName());
        } else if (propertyId.equals("email")) {
            return new EmailViewField(cases.getEmail());
        } else if (propertyId.equals("assignuser")) {
            return new DefaultViewField(cases.getAssignUserFullName());
        }
        return null;
    }
}
