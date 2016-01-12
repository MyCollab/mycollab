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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.EmailViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleContact> {
    private static final long serialVersionUID = 1L;

    public ContactReadFormFieldFactory(GenericBeanForm<SimpleContact> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("accountid")) {
            return new DefaultViewField(attachForm.getBean().getAccountName());
        } else if (propertyId.equals("email")) {
            return new EmailViewField(attachForm.getBean().getEmail());
        } else if (propertyId.equals("assignuser")) {
            return new DefaultViewField(attachForm.getBean().getAssignUserFullName());
        } else if (propertyId.equals("iscallable")) {
            if (Boolean.FALSE.equals(attachForm.getBean().getIscallable())) {
                return new DefaultViewField(AppContext.getMessage(GenericI18Enum.BUTTON_NO));
            } else {
                return new DefaultViewField(AppContext.getMessage(GenericI18Enum.BUTTON_YES));
            }
        } else if (propertyId.equals("birthday")) {
            return new DefaultViewField(AppContext.formatDate(attachForm.getBean().getBirthday()));
        } else if (propertyId.equals("firstname")) {
            return new DefaultViewField(attachForm.getBean().getFirstname());
        }

        return null;
    }
}
