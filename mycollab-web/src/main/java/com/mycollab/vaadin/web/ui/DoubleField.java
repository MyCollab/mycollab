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

import org.vaadin.viritin.fields.AbstractNumberField;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
// TODO
public class DoubleField extends AbstractNumberField {
    private static final long serialVersionUID = 1L;

    @Override
    protected void userInputToValue(String str) {
        try {
            this.setValue(Double.parseDouble(str));
        } catch (Exception e) {
            this.setValue(0d);
        }
    }

    @Override
    public void setWidth(String width) {
        tf.setWidth(width);
    }

    @Override
    public Double getValue() {
//        Double value = super.getValue();
//        return (value == null || value < 0) ? 0d : value;
        return 0d;
    }
}
