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
package com.mycollab.vaadin.web.ui.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
// TODO
public class RoundNumberField extends CustomField<Number> {
    private static final long serialVersionUID = 1L;

    private Number value;

    public RoundNumberField(final Number value) {
        this.value = value;
    }

    @Override
    protected Component initContent() {
        final Label label = new Label();
        label.setWidth("100%");

        if (value != null) {
            double d = value.doubleValue();
            d = Math.round(d * 100);
            d = d / 100;
            label.setValue(d + "");
        } else {
            label.setValue("");
        }

        return label;
    }

    @Override
    protected void doSetValue(Number number) {

    }

    @Override
    public Number getValue() {
        return null;
    }
}
