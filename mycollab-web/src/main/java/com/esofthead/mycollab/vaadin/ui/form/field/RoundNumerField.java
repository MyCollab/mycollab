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

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class RoundNumerField extends CustomField<Number> {
    private Number value;

    private static final long serialVersionUID = 1L;

    public RoundNumerField(final Number value) {
        this.value = value;
    }

    @Override
    public Class<Number> getType() {
        return Number.class;
    }

    @Override
    protected Component initContent() {
        final Label label = new Label();
        label.setWidth("100%");

        if (value != null) {
            double d = value.doubleValue();
            d = Math.round(d*100);
            d = d/100;
            label.setValue(d + "");
        } else {
            label.setValue("");
        }

        return label;
    }
}
