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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.risto.stylecalendar.StyleCalendarField;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class WeeklyCalendarFieldExp extends StyleCalendarField {
    private static final long serialVersionUID = 1L;

    public void setPopupClose() {
        this.setShowPopup(false);
    }

    protected String getPaintValue() {
        Object value = getValue();

        if (value == null) {
            if (getNullRepresentation() != null) {
                return getNullRepresentation();
            } else {
                return UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED);
            }
        } else {
            Date selectedDate = (Date) value;
            Date[] bounceDateofWeek = DateTimeUtils.getBounceDatesOfWeek(selectedDate);
            return UserUIContext.formatDate(bounceDateofWeek[0]) + " - " + UserUIContext.formatDate(bounceDateofWeek[1]);
        }
    }
}
