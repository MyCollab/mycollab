/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.time.LocalDate;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class RangeDateField extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    private DateField startDateField;
    private DateField endDateField;

    public RangeDateField() {
        startDateField = new DateField();
        endDateField = new DateField();
        with(ELabel.html(UserUIContext.getMessage(DayI18nEnum.OPT_FROM)), startDateField,
                ELabel.html(UserUIContext.getMessage(DayI18nEnum.OPT_TO)), endDateField).alignAll(Alignment.MIDDLE_LEFT);
    }

    public LocalDate[] getBounds() {
        LocalDate start = startDateField.getValue();
        LocalDate end = endDateField.getValue();

        if (start == null || end == null || start.isAfter(end)) {
            throw new UserInvalidInputException(UserUIContext.getMessage(DayI18nEnum.ERROR_INVALID_DATES));
        }

        return new LocalDate[]{start, end};
    }
}
