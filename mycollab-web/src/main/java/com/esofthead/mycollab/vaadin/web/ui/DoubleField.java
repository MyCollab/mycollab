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
package com.esofthead.mycollab.vaadin.web.ui;

import org.vaadin.viritin.fields.AbstractNumberField;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class DoubleField extends AbstractNumberField<Double> {
    private static final long serialVersionUID = 1L;

    @Override
    protected void userInputToValue(String str) {
        try {
            this.setValue(Double.valueOf(Double.parseDouble(str)));
        } catch (Exception e) {
            this.setValue(0d);
        }
    }

    @Override
    public Class<? extends Double> getType() {
        return Double.class;
    }

    @Override
    public void setWidth(String width) {
        tf.setWidth(width);
    }

    @Override
    public Double getValue() {
        Double value = super.getValue();
        return (value == null || value < 0) ? 0d : value;
    }
}
