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

import com.vaadin.ui.ComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class ValueComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public ValueComboBox() {
        this.setPageLength(20);
    }

    /**
     * @param nullIsAllowable
     * @param values
     */
    public ValueComboBox(boolean nullIsAllowable, String... values) {
        this();
        this.setEmptySelectionAllowed(nullIsAllowable);
        this.loadData(values);
    }

    public final void loadData(String... values) {
        this.setItems(values);

        if (!this.isEmptySelectionAllowed() && values.length > 0) {
            this.setSelectedItem(values[0]);
        }
    }
}
