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
package com.esofthead.mycollab.module.crm.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class LeadFieldFormatter extends FieldGroupFormatter {
    private static final LeadFieldFormatter _instance = new LeadFieldFormatter();

    private LeadFieldFormatter() {
        generateFieldDisplayHandler("prefixname", LeadI18nEnum.FORM_PREFIX);
        generateFieldDisplayHandler("firstname", LeadI18nEnum.FORM_FIRSTNAME);
        generateFieldDisplayHandler("lastname", LeadI18nEnum.FORM_LASTNAME);
        generateFieldDisplayHandler("title", LeadI18nEnum.FORM_TITLE);
        generateFieldDisplayHandler("department", LeadI18nEnum.FORM_DEPARTMENT);
        generateFieldDisplayHandler("accountname", LeadI18nEnum.FORM_ACCOUNT_NAME);
        generateFieldDisplayHandler("source", LeadI18nEnum.FORM_LEAD_SOURCE);
        generateFieldDisplayHandler("industry", LeadI18nEnum.FORM_INDUSTRY);
        generateFieldDisplayHandler("noemployees", LeadI18nEnum.FORM_NO_EMPLOYEES);
        generateFieldDisplayHandler("email", LeadI18nEnum.FORM_EMAIL);
        generateFieldDisplayHandler("officephone", LeadI18nEnum.FORM_OFFICE_PHONE);
        generateFieldDisplayHandler("mobile", LeadI18nEnum.FORM_MOBILE);
        generateFieldDisplayHandler("otherphone", LeadI18nEnum.FORM_OTHER_PHONE);
        generateFieldDisplayHandler("fax", LeadI18nEnum.FORM_FAX);
        generateFieldDisplayHandler("website", LeadI18nEnum.FORM_WEBSITE);
        generateFieldDisplayHandler("status", LeadI18nEnum.FORM_STATUS);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("primaddress", LeadI18nEnum.FORM_PRIMARY_ADDRESS);
        generateFieldDisplayHandler("primcity", LeadI18nEnum.FORM_PRIMARY_CITY);
        generateFieldDisplayHandler("primstate", LeadI18nEnum.FORM_PRIMARY_STATE);
        generateFieldDisplayHandler("primpostalcode", LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE);
        generateFieldDisplayHandler("primcountry", LeadI18nEnum.FORM_PRIMARY_COUNTRY);
        generateFieldDisplayHandler("otheraddress", LeadI18nEnum.FORM_OTHER_ADDRESS);
        generateFieldDisplayHandler("othercity", LeadI18nEnum.FORM_OTHER_CITY);
        generateFieldDisplayHandler("otherstate", LeadI18nEnum.FORM_OTHER_STATE);
        generateFieldDisplayHandler("otherpostalcode", LeadI18nEnum.FORM_OTHER_POSTAL_CODE);
        generateFieldDisplayHandler("othercountry", LeadI18nEnum.FORM_OTHER_COUNTRY);
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION);
    }

    public static LeadFieldFormatter instance() {
        return _instance;
    }
}
