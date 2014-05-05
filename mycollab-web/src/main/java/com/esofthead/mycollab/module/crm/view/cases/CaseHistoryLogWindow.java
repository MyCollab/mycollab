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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class CaseHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public CaseHistoryLogWindow(String module, String type) {
		super(module, type);
		
		this.generateFieldDisplayHandler("priority", "Priority");
		this.generateFieldDisplayHandler("status", "Status");
		this.generateFieldDisplayHandler("accountid", "Account Name");
		this.generateFieldDisplayHandler("phonenumber", "Phone Number");
		this.generateFieldDisplayHandler("origin", "Origin");
		this.generateFieldDisplayHandler("type", "Type");
		this.generateFieldDisplayHandler("reason", "Reason");
		this.generateFieldDisplayHandler("subject", "Subject");
		this.generateFieldDisplayHandler("email", "Email");
		this.generateFieldDisplayHandler("assignuser", AppContext
				.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		this.generateFieldDisplayHandler("description", "Description");
		this.generateFieldDisplayHandler("resolution", "Resolution");
	}

}
