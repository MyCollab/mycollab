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
package com.esofthead.mycollab.vaadin.ui.form.field;

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class PrettyDateViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private Date date;

    public PrettyDateViewField(Date date) {
        this.date = date;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        final Label l = new Label();
        l.setWidth("100%");
        if (date == null) {
            l.setValue("&nbsp;");
            l.setContentMode(ContentMode.HTML);
        } else {
            l.setValue(AppContext.formatPrettyTime(date));
            l.setDescription(AppContext.formatDate(date));
        }
        return l;
    }
}
