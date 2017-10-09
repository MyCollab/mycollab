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
