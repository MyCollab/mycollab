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
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
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

		this.generateFieldDisplayHandler("prefixname",
				AppContext.getMessage(LeadI18nEnum.FORM_PREFIX));
		this.generateFieldDisplayHandler("firstname",
				AppContext.getMessage(LeadI18nEnum.FORM_FIRSTNAME));
		this.generateFieldDisplayHandler("lastname",
				AppContext.getMessage(LeadI18nEnum.FORM_LASTNAME));
		this.generateFieldDisplayHandler("title",
				AppContext.getMessage(LeadI18nEnum.FORM_TITLE));
		this.generateFieldDisplayHandler("department",
				AppContext.getMessage(LeadI18nEnum.FORM_DEPARTMENT));
		this.generateFieldDisplayHandler("accountname",
				AppContext.getMessage(LeadI18nEnum.FORM_ACCOUNT_NAME));
		this.generateFieldDisplayHandler("source",
				AppContext.getMessage(LeadI18nEnum.FORM_LEAD_SOURCE));
		this.generateFieldDisplayHandler("industry",
				AppContext.getMessage(LeadI18nEnum.FORM_INDUSTRY));
		this.generateFieldDisplayHandler("noemployees",
				AppContext.getMessage(LeadI18nEnum.FORM_NO_EMPLOYEES));
		this.generateFieldDisplayHandler("email",
				AppContext.getMessage(LeadI18nEnum.FORM_EMAIL));
		this.generateFieldDisplayHandler("officephone",
				AppContext.getMessage(LeadI18nEnum.FORM_OFFICE_PHONE));
		this.generateFieldDisplayHandler("mobile",
				AppContext.getMessage(LeadI18nEnum.FORM_MOBILE));
		this.generateFieldDisplayHandler("otherphone",
				AppContext.getMessage(LeadI18nEnum.FORM_OTHER_PHONE));
		this.generateFieldDisplayHandler("fax",
				AppContext.getMessage(LeadI18nEnum.FORM_FAX));
		this.generateFieldDisplayHandler("website",
				AppContext.getMessage(LeadI18nEnum.FORM_WEBSITE));
		this.generateFieldDisplayHandler("status",
				AppContext.getMessage(LeadI18nEnum.FORM_STATUS));
		this.generateFieldDisplayHandler("assignuser",
				AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new UserHistoryFieldFormat());
		this.generateFieldDisplayHandler("primaddress",
				AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_ADDRESS));
		this.generateFieldDisplayHandler("primcity",
				AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_CITY));
		this.generateFieldDisplayHandler("primstate",
				AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_STATE));
		this.generateFieldDisplayHandler("primpostalcode",
				AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE));
		this.generateFieldDisplayHandler("primcountry",
				AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_COUNTRY));
		this.generateFieldDisplayHandler("otheraddress",
				AppContext.getMessage(LeadI18nEnum.FORM_OTHER_ADDRESS));
		this.generateFieldDisplayHandler("othercity",
				AppContext.getMessage(LeadI18nEnum.FORM_OTHER_CITY));
		this.generateFieldDisplayHandler("otherstate",
				AppContext.getMessage(LeadI18nEnum.FORM_OTHER_STATE));
		this.generateFieldDisplayHandler("otherpostalcode",
				AppContext.getMessage(LeadI18nEnum.FORM_OTHER_POSTAL_CODE));
		this.generateFieldDisplayHandler("othercountry",
				AppContext.getMessage(LeadI18nEnum.FORM_OTHER_COUNTRY));
		this.generateFieldDisplayHandler("description",
				AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION));
	}

}
