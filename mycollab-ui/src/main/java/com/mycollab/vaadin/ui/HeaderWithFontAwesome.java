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
package com.mycollab.vaadin.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public class HeaderWithFontAwesome extends CssLayout {
    private Label wrappedLbl;
    private FontAwesome iconFont;
    private String title;

    private HeaderWithFontAwesome(FontAwesome iconFont, String title, String primaryStyle) {
        super();
        wrappedLbl = new Label();
        wrappedLbl.setContentMode(ContentMode.HTML);
        wrappedLbl.setStyleName(primaryStyle);
        wrappedLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        this.iconFont = iconFont;
        updateTitle(title);
        this.addComponent(wrappedLbl);
    }

    public static final HeaderWithFontAwesome h2(FontAwesome iconFont, String title) {
        return new HeaderWithFontAwesome(iconFont, title, ValoTheme.LABEL_H2);
    }

    public static final HeaderWithFontAwesome h3(FontAwesome iconFont, String title) {
        return new HeaderWithFontAwesome(iconFont, title, ValoTheme.LABEL_H3);
    }

    public void updateTitle(String value) {
        this.title = value;
        wrappedLbl.setValue(iconFont.getHtml() + " " + value);
    }

    public void appendToTitle(String value) {
        wrappedLbl.setValue(iconFont.getHtml() + " " + title + " " + value);
    }
}
