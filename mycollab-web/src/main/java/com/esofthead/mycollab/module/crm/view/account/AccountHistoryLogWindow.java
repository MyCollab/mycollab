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

package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
class AccountHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public AccountHistoryLogWindow(String module, String type, int typeid) {
		super(module, type);

		this.generateFieldDisplayHandler("accountname", "Account Name");
		this.generateFieldDisplayHandler("phoneoffice", AppContext
				.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD));
		this.generateFieldDisplayHandler("website", "Website");
		this.generateFieldDisplayHandler("fax", "Fax");
		this.generateFieldDisplayHandler("numemployees", "Employees");
		this.generateFieldDisplayHandler("alternatephone", "Other Phone");
		this.generateFieldDisplayHandler("industry", "Industry");
		this.generateFieldDisplayHandler("email", "Email");
		this.generateFieldDisplayHandler("type", "Type");
		this.generateFieldDisplayHandler("ownership", "Ownership");
		this.generateFieldDisplayHandler("annualrevenue", "Annual Revenue");
		this.generateFieldDisplayHandler("billingaddress", "Billing Address");
		this.generateFieldDisplayHandler("shippingaddress", "Shipping Address");
		this.generateFieldDisplayHandler("city", "Billing City");
		this.generateFieldDisplayHandler("shippingcity", "Shipping City");
		this.generateFieldDisplayHandler("state", "Billing State");
		this.generateFieldDisplayHandler("shippingstate", "Shipping State");
		this.generateFieldDisplayHandler("postalcode", "Postal Code");
		this.generateFieldDisplayHandler("shippingpostalcode",
				"Shipping Postal Code");
		this.generateFieldDisplayHandler("description", "Description");
	}
}
