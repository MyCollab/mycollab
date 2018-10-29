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

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class SortButton extends Button {
    private static final long serialVersionUID = 6899070243378436412L;

    private boolean isDesc = true;

    public SortButton() {
        this.setIcon(VaadinIcons.CARET_DOWN);
        this.addClickListener(clickEvent -> toggleSortOrder());
    }

    public SortButton(String caption, ClickListener listener) {
        this();
        this.setCaption(caption);
        this.addClickListener(listener);
    }

    public void toggleSortOrder() {
        this.isDesc = !this.isDesc;
        if (this.isDesc) {
            this.setIcon(VaadinIcons.CARET_DOWN);
        } else {
            this.setIcon(VaadinIcons.CARET_UP);
        }
    }

    public boolean isDesc() {
        return this.isDesc;
    }

}
