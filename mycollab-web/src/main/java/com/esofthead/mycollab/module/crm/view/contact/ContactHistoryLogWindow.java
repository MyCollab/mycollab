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
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class ContactHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public ContactHistoryLogWindow(String module, String type) {
		super(module, type);

		this.generateFieldDisplayHandler("firstname", "First Name");
		this.generateFieldDisplayHandler("lastname", "Last Name");
		this.generateFieldDisplayHandler("accountId", "Account");
		this.generateFieldDisplayHandler("title", "Title");
		this.generateFieldDisplayHandler("department", "Department");
		this.generateFieldDisplayHandler("email", "Email");
		this.generateFieldDisplayHandler("assistant", "Assistant");
		this.generateFieldDisplayHandler("assistantphone", "Assistant Phone");
		this.generateFieldDisplayHandler("leadsource", "Leade Source");
		this.generateFieldDisplayHandler("officephone", AppContext
				.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD));
		this.generateFieldDisplayHandler("mobile", "Mobile");
		this.generateFieldDisplayHandler("homephone", "Home Phone");
		this.generateFieldDisplayHandler("otherphone", "Other Phone");
		this.generateFieldDisplayHandler("birthday", "Birthday",
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("iscallable", "Callable");
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
