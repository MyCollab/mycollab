/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui.field;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
// TODO
public class DateTimeViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private Date date;

    public DateTimeViewField(Date date) {
        this.date = date;
    }

    @Override
    protected Component initContent() {
        String dateValue = (date == null) ? "" : UserUIContext.formatDateTime(date);
        return new Label(dateValue);
    }

    @Override
    protected void doSetValue(String s) {

    }

    @Override
    public String getValue() {
        return null;
    }
}
