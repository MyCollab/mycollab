/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.contact;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.ContactTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public ContactListCustomizeWindow(AbstractPagedBeanTable table) {
        super(CrmTypeConstants.CONTACT, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(ContactTableFieldDef.account, ContactTableFieldDef.assignUser,
                ContactTableFieldDef.assistant, ContactTableFieldDef.assistantPhone,
                ContactTableFieldDef.birthday, ContactTableFieldDef.department,
                ContactTableFieldDef.email, ContactTableFieldDef.fax,
                ContactTableFieldDef.isCallable, ContactTableFieldDef.mobile,
                ContactTableFieldDef.name, ContactTableFieldDef.phoneOffice,
                ContactTableFieldDef.title);
    }

}
