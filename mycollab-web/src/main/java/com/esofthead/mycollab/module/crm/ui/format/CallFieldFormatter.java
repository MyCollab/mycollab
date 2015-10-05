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
import com.esofthead.mycollab.module.crm.i18n.CallI18nEnum;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class CallFieldFormatter extends FieldGroupFormatter {
    private static final CallFieldFormatter _instance = new CallFieldFormatter();

    private CallFieldFormatter() {
        generateFieldDisplayHandler("subject", CallI18nEnum.FORM_SUBJECT);
        generateFieldDisplayHandler("startdate", CallI18nEnum.FORM_START_DATE_TIME, FieldGroupFormatter.DATE_FIELD);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("status", CallI18nEnum.FORM_STATUS);
        generateFieldDisplayHandler("purpose", CallI18nEnum.FORM_PURPOSE);
    }

    public static CallFieldFormatter instance() {
        return _instance;
    }
}
