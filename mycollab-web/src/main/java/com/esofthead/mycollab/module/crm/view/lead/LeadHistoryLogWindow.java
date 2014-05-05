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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class LeadHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public LeadHistoryLogWindow(String module, String type) {
		super(module, type);

		this.generateFieldDisplayHandler("prefixname", "Prefix name");
		this.generateFieldDisplayHandler("firstname", "First name");
		this.generateFieldDisplayHandler("lastname", "Last Name");
		this.generateFieldDisplayHandler("title", "Title");
		this.generateFieldDisplayHandler("department", "Department");
		this.generateFieldDisplayHandler("accountname", "Account Name");
		this.generateFieldDisplayHandler("source", "Lead Source");
		this.generateFieldDisplayHandler("industry", "Industry");
		this.generateFieldDisplayHandler("noemployees", "No of Employees");
		this.generateFieldDisplayHandler("email", "Email");
		this.generateFieldDisplayHandler("officephone", "Office Phone");
		this.generateFieldDisplayHandler("mobile", "Mobile");
		this.generateFieldDisplayHandler("otherphone", "Other Phone");
		this.generateFieldDisplayHandler("fax", "Fax");
		this.generateFieldDisplayHandler("website", "Web Site");
		this.generateFieldDisplayHandler("status", "Status");
		this.generateFieldDisplayHandler("assignuser", AppContext
				.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		this.generateFieldDisplayHandler("primaddress", "Address");
		this.generateFieldDisplayHandler("primcity", "City");
		this.generateFieldDisplayHandler("primstate", "State");
		this.generateFieldDisplayHandler("primpostalcode", "Postal Code");
		this.generateFieldDisplayHandler("primcountry", "Country");
		this.generateFieldDisplayHandler("otheraddress", "Other Address");
		this.generateFieldDisplayHandler("othercity", "Other City");
		this.generateFieldDisplayHandler("otherstate", "Other State");
		this.generateFieldDisplayHandler("otherpostalcode", "Other Postal Code");
		this.generateFieldDisplayHandler("othercountry", "Other Country");
		this.generateFieldDisplayHandler("description", "Description");
	}

}
