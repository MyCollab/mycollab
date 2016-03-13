/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class ELabel extends Label {

    public ELabel() {
        super();
    }

    public ELabel(String content) {
        super(content);
    }

    public ELabel(String content, ContentMode mode) {
        super(content, mode);
    }

    public ELabel withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public ELabel withWidthUndefined() {
        return withWidth("-1px");
    }

    public ELabel withWidth(String width) {
        this.setWidth(width);
        return this;
    }

    public ELabel withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            this.addStyleName(styleName);
        }
        return this;
    }

    public ELabel prettyDate(Date date) {
        this.setValue(AppContext.formatPrettyTime(date));
        this.setDescription(AppContext.formatDate(date));
        return this;
    }

    public ELabel prettyDateTime(Date date) {
        this.setValue(AppContext.formatPrettyTime(date));
        this.setDescription(AppContext.formatDateTime(date));
        return this;
    }

    public static final ELabel h2(String value) {
        return new ELabel(value, ContentMode.HTML).withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN);
    }

    public static final ELabel h3(String value) {
        return new ELabel(value, ContentMode.HTML).withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
    }

    public static final ELabel fontIcon(FontAwesome icon) {
        return new ELabel(icon.getHtml(), ContentMode.HTML).withWidthUndefined();
    }

    public static final ELabel EMPTY_SPACE() {
        return new ELabel("&nbsp;", ContentMode.HTML).withWidthUndefined();
    }

    public static final ELabel hr() {
        return new ELabel("<hr/>", ContentMode.HTML).withStyleName("hr");
    }
}
