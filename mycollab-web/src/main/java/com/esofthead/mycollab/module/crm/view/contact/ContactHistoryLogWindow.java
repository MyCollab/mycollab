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
import com.esofthead.mycollab.module.crm.localization.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
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

		this.generateFieldDisplayHandler("firstname",
				AppContext.getMessage(ContactI18nEnum.FORM_FIRSTNAME));
		this.generateFieldDisplayHandler("lastname",
				AppContext.getMessage(ContactI18nEnum.FORM_LASTNAME));
		this.generateFieldDisplayHandler("accountId",
				AppContext.getMessage(ContactI18nEnum.FORM_ACCOUNTS));
		this.generateFieldDisplayHandler("title",
				AppContext.getMessage(ContactI18nEnum.FORM_TITLE));
		this.generateFieldDisplayHandler("department",
				AppContext.getMessage(ContactI18nEnum.FORM_DEPARTMENT));
		this.generateFieldDisplayHandler("email",
				AppContext.getMessage(ContactI18nEnum.FORM_EMAIL));
		this.generateFieldDisplayHandler("assistant",
				AppContext.getMessage(ContactI18nEnum.FORM_ASSISTANT));
		this.generateFieldDisplayHandler("assistantphone",
				AppContext.getMessage(ContactI18nEnum.FORM_ASSISTANT_PHONE));
		this.generateFieldDisplayHandler("leadsource",
				AppContext.getMessage(ContactI18nEnum.FORM_LEAD_SOURCE));
		this.generateFieldDisplayHandler("officephone", AppContext
				.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD));
		this.generateFieldDisplayHandler("mobile",
				AppContext.getMessage(ContactI18nEnum.FORM_MOBILE));
		this.generateFieldDisplayHandler("homephone",
				AppContext.getMessage(ContactI18nEnum.FORM_HOME_PHONE));
		this.generateFieldDisplayHandler("otherphone",
				AppContext.getMessage(ContactI18nEnum.FORM_OTHER_PHONE));
		this.generateFieldDisplayHandler("birthday",
				AppContext.getMessage(ContactI18nEnum.FORM_BIRTHDAY),
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("iscallable",
				AppContext.getMessage(ContactI18nEnum.FORM_IS_CALLABLE));
		this.generateFieldDisplayHandler("assignuser",
				AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new UserHistoryFieldFormat());
		this.generateFieldDisplayHandler("primaddress",
				AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_ADDRESS));
		this.generateFieldDisplayHandler("primcity",
				AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_CITY));
		this.generateFieldDisplayHandler("primstate",
				AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_STATE));
		this.generateFieldDisplayHandler("primpostalcode",
				AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_POSTAL_CODE));
		this.generateFieldDisplayHandler("primcountry",
				AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_COUNTRY));
		this.generateFieldDisplayHandler("otheraddress",
				AppContext.getMessage(ContactI18nEnum.FORM_OTHER_ADDRESS));
		this.generateFieldDisplayHandler("othercity",
				AppContext.getMessage(ContactI18nEnum.FORM_OTHER_CITY));
		this.generateFieldDisplayHandler("otherstate",
				AppContext.getMessage(ContactI18nEnum.FORM_OTHER_STATE));
		this.generateFieldDisplayHandler("otherpostalcode",
				AppContext.getMessage(ContactI18nEnum.FORM_OTHER_POSTAL_CODE));
		this.generateFieldDisplayHandler("othercountry",
				AppContext.getMessage(ContactI18nEnum.FORM_OTHER_COUNTRY));
		this.generateFieldDisplayHandler("description",
				AppContext.getMessage(ContactI18nEnum.FORM_DESCRIPTION));

	}

}
