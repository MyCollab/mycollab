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
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class MeetingFieldFormatter extends FieldGroupFormatter {
    private static final MeetingFieldFormatter _instance = new MeetingFieldFormatter();

    private MeetingFieldFormatter() {
        generateFieldDisplayHandler("subject", MeetingI18nEnum.FORM_SUBJECT);
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS);
        generateFieldDisplayHandler("startdate", MeetingI18nEnum.FORM_START_DATE_TIME, DATETIME_FIELD);
        generateFieldDisplayHandler("enddate", MeetingI18nEnum.FORM_END_DATE_TIME, DATETIME_FIELD);
        generateFieldDisplayHandler("location", MeetingI18nEnum.FORM_LOCATION);
    }

    public static MeetingFieldFormatter instance() {
        return _instance;
    }
}
