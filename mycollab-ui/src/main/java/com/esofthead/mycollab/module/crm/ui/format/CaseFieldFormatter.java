/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;
import com.esofthead.mycollab.module.user.ui.format.UserHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * @author MyCollab LTd
 * @since 5.1.4
 */
public class CaseFieldFormatter extends FieldGroupFormatter {
    private static final CaseFieldFormatter _instance = new CaseFieldFormatter();

    private CaseFieldFormatter() {
        generateFieldDisplayHandler("priority", CaseI18nEnum.FORM_PRIORITY);
        generateFieldDisplayHandler("status", CaseI18nEnum.FORM_STATUS);
        generateFieldDisplayHandler("accountid", CaseI18nEnum.FORM_ACCOUNT, new AccountHistoryFieldFormat());
        generateFieldDisplayHandler("phonenumber", CaseI18nEnum.FORM_PHONE);
        generateFieldDisplayHandler("origin", CaseI18nEnum.FORM_ORIGIN);
        generateFieldDisplayHandler("type", CaseI18nEnum.FORM_TYPE);
        generateFieldDisplayHandler("reason", CaseI18nEnum.FORM_REASON);
        generateFieldDisplayHandler("subject", CaseI18nEnum.FORM_SUBJECT);
        generateFieldDisplayHandler("email", CaseI18nEnum.FORM_EMAIL);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        generateFieldDisplayHandler("resolution", CaseI18nEnum.FORM_RESOLUTION, TRIM_HTMLS);
    }

    public static CaseFieldFormatter instance() {
        return _instance;
    }
}
