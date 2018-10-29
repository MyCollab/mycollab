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
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
// TODO
public class DateViewField extends CustomField<Date> {
    private static final long serialVersionUID = 1L;

    private Date date;
    private Label dateLbl;

    public DateViewField(Date date) {
        this.date = date;
        dateLbl = new Label();
        dateLbl.setWidth("100%");
    }

    @Override
    protected Component initContent() {
        if (date == null) {
            dateLbl.setValue("&nbsp;");
            dateLbl.setContentMode(ContentMode.HTML);
        } else {
            dateLbl.setValue(UserUIContext.formatDate(date));
            dateLbl.setDescription(UserUIContext.formatPrettyTime(date));
        }
        return dateLbl;
    }

    @Override
    protected void doSetValue(Date date) {

    }

    @Override
    public Date getValue() {
        return null;
    }
}
