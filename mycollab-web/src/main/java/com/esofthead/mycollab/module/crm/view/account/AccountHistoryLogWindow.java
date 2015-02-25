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
import com.esofthead.mycollab.module.crm.i18n.OptionI18nEnum.AccountType;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;
import com.esofthead.mycollab.utils.FieldGroupFormatter.I18nHistoryFieldFormat;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public static final FieldGroupFormatter accountFormatter;

	static {
		accountFormatter = new FieldGroupFormatter();

		accountFormatter.generateFieldDisplayHandler("accountname",
				AccountI18nEnum.FORM_ACCOUNT_NAME);

		accountFormatter.generateFieldDisplayHandler("assignuser",
				GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
		accountFormatter.generateFieldDisplayHandler("phoneoffice",
				AccountI18nEnum.FORM_OFFICE_PHONE);
		accountFormatter.generateFieldDisplayHandler("website",
				AccountI18nEnum.FORM_WEBSITE);
		accountFormatter.generateFieldDisplayHandler("fax",
				AccountI18nEnum.FORM_FAX);
		accountFormatter.generateFieldDisplayHandler("numemployees",
				AccountI18nEnum.FORM_EMPLOYEES);
		accountFormatter.generateFieldDisplayHandler("alternatephone",
				AccountI18nEnum.FORM_OTHER_PHONE);
		accountFormatter.generateFieldDisplayHandler("industry",
				AccountI18nEnum.FORM_INDUSTRY);
		accountFormatter.generateFieldDisplayHandler("email",
				AccountI18nEnum.FORM_EMAIL);
		accountFormatter.generateFieldDisplayHandler("type",
				AccountI18nEnum.FORM_TYPE, new I18nHistoryFieldFormat(
						AccountType.class));
		accountFormatter.generateFieldDisplayHandler("ownership",
				AccountI18nEnum.FORM_OWNERSHIP);
		accountFormatter.generateFieldDisplayHandler("annualrevenue",
				AccountI18nEnum.FORM_ANNUAL_REVENUE);
		accountFormatter.generateFieldDisplayHandler("billingaddress",
				AccountI18nEnum.FORM_BILLING_ADDRESS);
		accountFormatter.generateFieldDisplayHandler("shippingaddress",
				AccountI18nEnum.FORM_SHIPPING_ADDRESS);
		accountFormatter.generateFieldDisplayHandler("city",
				AccountI18nEnum.FORM_BILLING_CITY);
		accountFormatter.generateFieldDisplayHandler("shippingcity",
				AccountI18nEnum.FORM_SHIPPING_CITY);
		accountFormatter.generateFieldDisplayHandler("state",
				AccountI18nEnum.FORM_BILLING_STATE);
		accountFormatter.generateFieldDisplayHandler("shippingstate",
				AccountI18nEnum.FORM_SHIPPING_STATE);
		accountFormatter.generateFieldDisplayHandler("postalcode",
				AccountI18nEnum.FORM_BILLING_POSTAL_CODE);
		accountFormatter.generateFieldDisplayHandler("shippingpostalcode",
				AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE);
		accountFormatter.generateFieldDisplayHandler("description",
				GenericI18Enum.FORM_DESCRIPTION);
	}

	public AccountHistoryLogWindow(String module, String type, int typeid) {
		super(module, type);
	}

	@Override
	protected FieldGroupFormatter buildFormatter() {
		return accountFormatter;
	}
}
