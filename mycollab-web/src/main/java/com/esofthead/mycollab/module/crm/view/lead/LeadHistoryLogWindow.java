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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFomatter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class LeadHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public static final FieldGroupFomatter leadFormatter;

	static {
		leadFormatter = new FieldGroupFomatter();

		leadFormatter.generateFieldDisplayHandler("prefixname",
				LeadI18nEnum.FORM_PREFIX);
		leadFormatter.generateFieldDisplayHandler("firstname",
				LeadI18nEnum.FORM_FIRSTNAME);
		leadFormatter.generateFieldDisplayHandler("lastname",
				LeadI18nEnum.FORM_LASTNAME);
		leadFormatter.generateFieldDisplayHandler("title",
				LeadI18nEnum.FORM_TITLE);
		leadFormatter.generateFieldDisplayHandler("department",
				LeadI18nEnum.FORM_DEPARTMENT);
		leadFormatter.generateFieldDisplayHandler("accountname",
				LeadI18nEnum.FORM_ACCOUNT_NAME);
		leadFormatter.generateFieldDisplayHandler("source",
				LeadI18nEnum.FORM_LEAD_SOURCE);
		leadFormatter.generateFieldDisplayHandler("industry",
				LeadI18nEnum.FORM_INDUSTRY);
		leadFormatter.generateFieldDisplayHandler("noemployees",
				LeadI18nEnum.FORM_NO_EMPLOYEES);
		leadFormatter.generateFieldDisplayHandler("email",
				LeadI18nEnum.FORM_EMAIL);
		leadFormatter.generateFieldDisplayHandler("officephone",
				LeadI18nEnum.FORM_OFFICE_PHONE);
		leadFormatter.generateFieldDisplayHandler("mobile",
				LeadI18nEnum.FORM_MOBILE);
		leadFormatter.generateFieldDisplayHandler("otherphone",
				LeadI18nEnum.FORM_OTHER_PHONE);
		leadFormatter.generateFieldDisplayHandler("fax", LeadI18nEnum.FORM_FAX);
		leadFormatter.generateFieldDisplayHandler("website",
				LeadI18nEnum.FORM_WEBSITE);
		leadFormatter.generateFieldDisplayHandler("status",
				LeadI18nEnum.FORM_STATUS);
		leadFormatter.generateFieldDisplayHandler("assignuser",
				GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
		leadFormatter.generateFieldDisplayHandler("primaddress",
				LeadI18nEnum.FORM_PRIMARY_ADDRESS);
		leadFormatter.generateFieldDisplayHandler("primcity",
				LeadI18nEnum.FORM_PRIMARY_CITY);
		leadFormatter.generateFieldDisplayHandler("primstate",
				LeadI18nEnum.FORM_PRIMARY_STATE);
		leadFormatter.generateFieldDisplayHandler("primpostalcode",
				LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE);
		leadFormatter.generateFieldDisplayHandler("primcountry",
				LeadI18nEnum.FORM_PRIMARY_COUNTRY);
		leadFormatter.generateFieldDisplayHandler("otheraddress",
				LeadI18nEnum.FORM_OTHER_ADDRESS);
		leadFormatter.generateFieldDisplayHandler("othercity",
				LeadI18nEnum.FORM_OTHER_CITY);
		leadFormatter.generateFieldDisplayHandler("otherstate",
				LeadI18nEnum.FORM_OTHER_STATE);
		leadFormatter.generateFieldDisplayHandler("otherpostalcode",
				LeadI18nEnum.FORM_OTHER_POSTAL_CODE);
		leadFormatter.generateFieldDisplayHandler("othercountry",
				LeadI18nEnum.FORM_OTHER_COUNTRY);
		leadFormatter.generateFieldDisplayHandler("description",
				GenericI18Enum.FORM_DESCRIPTION);
	}

	public LeadHistoryLogWindow(String module, String type) {
		super(module, type);
	}

	@Override
	protected FieldGroupFomatter buildFormatter() {
		return leadFormatter;
	}

}
