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
package com.esofthead.mycollab.module.project.view.task;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.server.GenericFontIcon;

/**
 * Created by baohan on 7/7/15.
 */
public enum TaskPriorityResource implements FontIcon {
    URGENT(FontAwesome.ADJUST.getCodepoint()), HIGH(FontAwesome.ADJUST.getCodepoint()),
    MEDIUM(FontAwesome.ADJUST.getCodepoint()), LOW(FontAwesome.ADJUST.getCodepoint()),
    NONE(FontAwesome.ADJUST.getCodepoint());;

    private int codepoint;

    private TaskPriorityResource(int codepoint) {
        this.codepoint = codepoint;
    }

    public String getMIMEType() {
        throw new UnsupportedOperationException(FontIcon.class.getSimpleName() + " should not be used where a MIME type is needed.");
    }

    public String getFontFamily() {
        return "FontAwesome";
    }

    public int getCodepoint() {
        return this.codepoint;
    }

    public String getHtml() {
        return GenericFontIcon.getHtml("FontAwesome", this.codepoint);
    }
}
