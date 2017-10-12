/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.LeadTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public LeadListCustomizeWindow(AbstractPagedBeanTable table) {
        super(CrmTypeConstants.LEAD, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(LeadTableFieldDef.accountName,
                LeadTableFieldDef.assignedUser, LeadTableFieldDef.department,
                LeadTableFieldDef.email, LeadTableFieldDef.phoneoffice,
                LeadTableFieldDef.fax, LeadTableFieldDef.industry,
                LeadTableFieldDef.leadSource, LeadTableFieldDef.mobile,
                LeadTableFieldDef.name, LeadTableFieldDef.status,
                LeadTableFieldDef.title, LeadTableFieldDef.website);
    }

}
