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
package com.mycollab.vaadin.web.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class SortButton extends Button {
    private static final long serialVersionUID = 6899070243378436412L;

    private boolean isDesc = true;

    public SortButton() {
        super();
        this.setIcon(FontAwesome.CARET_DOWN);
        this.addClickListener(clickEvent -> {
            toggleSortOrder();
        });
    }

    public SortButton(String caption, ClickListener listener) {
        this();
        this.setCaption(caption);
        this.addClickListener(listener);
    }

    public void toggleSortOrder() {
        this.isDesc = !this.isDesc;
        if (this.isDesc) {
            this.setIcon(FontAwesome.CARET_DOWN);
        } else {
            this.setIcon(FontAwesome.CARET_UP);
        }
    }

    public boolean isDesc() {
        return this.isDesc;
    }

}
