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
package com.esofthead.mycollab.module.crm.view.opportunity;

import java.util.Arrays;
import java.util.Collection;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.CustomizedTableWindow;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class OpportunityListCustomizeWindow extends CustomizedTableWindow {
	private static final long serialVersionUID = 1L;

	public OpportunityListCustomizeWindow(String viewId,
			AbstractPagedBeanTable table) {
		super(viewId, table);
	}

	@Override
	protected Collection<TableViewField> getAvailableColumns() {
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
