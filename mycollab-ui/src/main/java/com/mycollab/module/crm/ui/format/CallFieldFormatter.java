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
package com.mycollab.module.crm.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class CallFieldFormatter extends FieldGroupFormatter {
    private static final CallFieldFormatter _instance = new CallFieldFormatter();

    private CallFieldFormatter() {
        generateFieldDisplayHandler("subject", CallI18nEnum.FORM_SUBJECT);
        generateFieldDisplayHandler("startdate", CallI18nEnum.FORM_START_DATE_TIME, DATE_FIELD);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS);
        generateFieldDisplayHandler("purpose", CallI18nEnum.FORM_PURPOSE);
    }

    public static CallFieldFormatter instance() {
        return _instance;
    }
}
