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
package com.mycollab.vaadin.ui;

import com.vaadin.ui.DateField;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class PopupDateFieldExt extends DateField {
    private static final long serialVersionUID = 1L;

    public PopupDateFieldExt() {
        this(null);
    }

    public PopupDateFieldExt(Date value) {
        this(null, value);
    }

    public PopupDateFieldExt(String caption, Date value) {
//        super(null, value);
//        this.setTimeZone(UserUIContext.getUserTimeZone());
//        this.setDateFormat(AppUI.getDateFormat());
    }

    public PopupDateFieldExt withWidth(String width) {
//        this.setWidth(width);
        return this;
    }
}
