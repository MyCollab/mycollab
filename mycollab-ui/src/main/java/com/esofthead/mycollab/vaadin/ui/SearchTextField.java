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

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TextField;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public abstract class SearchTextField extends MHorizontalLayout {
    private FontIconLabel icon;
    private TextField innerField;
    private ShortcutListener shortcutListener = new ShortcutListener("searchfield", ShortcutAction.KeyCode.ENTER,
            null) {
        @Override
        public void handleAction(Object sender, Object target) {
            String value = ((TextField) target).getValue();
                if (StringUtils.isNotBlank(value)) {
                    doSearch(value);
                }
        }
    };

    public SearchTextField() {
        this.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        icon = new FontIconLabel(FontAwesome.SEARCH);
        innerField = new TextField();
        innerField.setImmediate(true);
        innerField.setInputPrompt("Search");
        innerField.setWidth("180px");
        this.with(icon, innerField).withStyleName("searchfield");
        ShortcutExtension.installShortcutAction(innerField, shortcutListener);
    }

    abstract public void doSearch(String value);

    public void setInputPrompt(String value) {
        innerField.setInputPrompt(value);
    }
}
