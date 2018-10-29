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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.OpportunityTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedGrid;
import com.mycollab.vaadin.web.ui.table.CustomizedGridWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunityListCustomizeWindow extends CustomizedGridWindow {
    private static final long serialVersionUID = 1L;

    public OpportunityListCustomizeWindow(AbstractPagedGrid table) {
        super(CrmTypeConstants.OPPORTUNITY, table);
    }

    @Override
    protected Collection<GridFieldMeta> getAvailableColumns() {
        return Arrays.asList(OpportunityTableFieldDef.accountName,
                OpportunityTableFieldDef.amount,
                OpportunityTableFieldDef.assignUser,
                OpportunityTableFieldDef.campaignName,
                OpportunityTableFieldDef.currency,
                OpportunityTableFieldDef.expectedCloseDate,
                OpportunityTableFieldDef.leadSource,
                OpportunityTableFieldDef.opportunityName,
                OpportunityTableFieldDef.probability,
                OpportunityTableFieldDef.type);
    }

}
