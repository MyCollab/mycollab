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
package com.mycollab.mobile.ui;

import com.vaadin.ui.ListSelect;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ValueListSelect extends ListSelect {
    private static final long serialVersionUID = 1L;

    public ValueListSelect() {
        super();
        this.setRows(1);
    }

    /**
     * @param nullIsAllowable
     * @param values
     */
    public ValueListSelect(boolean nullIsAllowable, String... values) {
        super();
        this.setNullSelectionAllowed(nullIsAllowable);
        this.setImmediate(true);
        this.loadData(values);

        this.setRows(1);
    }

    public ValueListSelect(boolean nullIsAllowable, Number... values) {
        super();
        this.setRows(1);
        this.setNullSelectionAllowed(nullIsAllowable);
        this.setImmediate(true);
        this.loadData(values);

        if (!this.isNullSelectionAllowed()) {
            this.select(this.getItemIds().iterator().next());
        }
    }

    public final void loadData(String... values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        for (String value: values) {
            this.addItem(value);
        }

        if (!this.isNullSelectionAllowed()) {
            this.select(this.getItemIds().iterator().next());
        }
    }

    public final void loadData(Number... values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        for (Number value: values) {
            this.addItem(value);
        }
    }
}
