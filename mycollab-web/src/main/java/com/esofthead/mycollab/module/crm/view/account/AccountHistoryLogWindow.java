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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
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

		this.generateFieldDisplayHandler("accountname",
				AppContext.getMessage(AccountI18nEnum.FORM_ACCOUNT_NAME));

		this.generateFieldDisplayHandler("assignuser",
				AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new UserHistoryFieldFormat());
		this.generateFieldDisplayHandler("phoneoffice", AppContext
				.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD));
		this.generateFieldDisplayHandler("website",
				AppContext.getMessage(AccountI18nEnum.FORM_WEBSITE));
		this.generateFieldDisplayHandler("fax",
				AppContext.getMessage(AccountI18nEnum.FORM_FAX));
		this.generateFieldDisplayHandler("numemployees",
				AppContext.getMessage(AccountI18nEnum.FORM_EMPLOYEES));
		this.generateFieldDisplayHandler("alternatephone",
				AppContext.getMessage(AccountI18nEnum.FORM_OTHER_PHONE));
		this.generateFieldDisplayHandler("industry",
				AppContext.getMessage(AccountI18nEnum.FORM_INDUSTRY));
		this.generateFieldDisplayHandler("email",
				AppContext.getMessage(AccountI18nEnum.FORM_EMAIL));
		this.generateFieldDisplayHandler("type",
				AppContext.getMessage(AccountI18nEnum.FORM_TYPE));
		this.generateFieldDisplayHandler("ownership",
				AppContext.getMessage(AccountI18nEnum.FORM_OWNERSHIP));
		this.generateFieldDisplayHandler("annualrevenue",
				AppContext.getMessage(AccountI18nEnum.FORM_ANNUAL_REVENUE));
		this.generateFieldDisplayHandler("billingaddress",
				AppContext.getMessage(AccountI18nEnum.FORM_BILLING_ADDRESS));
		this.generateFieldDisplayHandler("shippingaddress",
				AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_ADDRESS));
		this.generateFieldDisplayHandler("city",
				AppContext.getMessage(AccountI18nEnum.FORM_BILLING_CITY));
		this.generateFieldDisplayHandler("shippingcity",
				AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_CITY));
		this.generateFieldDisplayHandler("state",
				AppContext.getMessage(AccountI18nEnum.FORM_BILLING_STATE));
		this.generateFieldDisplayHandler("shippingstate",
				AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_STATE));
		this.generateFieldDisplayHandler("postalcode",
				AppContext.getMessage(AccountI18nEnum.FORM_BILLING_POSTAL_CODE));
		this.generateFieldDisplayHandler("shippingpostalcode", AppContext
				.getMessage(AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE));
		this.generateFieldDisplayHandler("description",
				AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION));
	}
}
