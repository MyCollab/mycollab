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
package com.esofthead.mycollab.mobile.module.crm.view.lead;

import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.EmailViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class LeadReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleLead> {
    private static final long serialVersionUID = 1L;

    public LeadReadFormFieldFactory(GenericBeanForm<SimpleLead> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleLead lead = attachForm.getBean();
        if (propertyId.equals("firstname")) {
            String prefix = "", firstName = "";
            if (lead.getPrefixname() != null) {
                prefix = lead.getPrefixname();
            }

            if (lead.getFirstname() != null) {
                firstName = lead.getFirstname();
            }

            return new DefaultViewField(prefix + firstName);
        } else if (propertyId.equals("website")) {
            return new DefaultViewField(lead.getWebsite());
        } else if (propertyId.equals("email")) {
            return new EmailViewField(lead.getEmail());
        } else if (propertyId.equals("accountid")) {
            return new DefaultViewField(lead.getAccountname());
        } else if (propertyId.equals("assignuser")) {
            return new DefaultViewField(lead.getAssignUserFullName());
        }

        return null;
    }

}
