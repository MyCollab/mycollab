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
package com.esofthead.mycollab.vaadin.web.ui.field;

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DateViewField extends CustomField {
    private static final long serialVersionUID = 1L;

    private Date date;
    private Label dateLbl;

    public DateViewField(Date date) {
        this.date = date;
        dateLbl = new Label();
        dateLbl.setWidth("100%");
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        if (date == null) {
            dateLbl.setValue("&nbsp;");
            dateLbl.setContentMode(ContentMode.HTML);
        } else {
            dateLbl.setValue(AppContext.formatDate(date));
            dateLbl.setDescription(AppContext.formatPrettyTime(date));
        }
        return dateLbl;
    }
}
