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

import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.mycollab.core.utils.StringUtils.isNotBlank;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public abstract class SearchTextField extends MHorizontalLayout {
    private TextField innerField;

    public SearchTextField() {
        this.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        ELabel icon = ELabel.fontIcon(FontAwesome.SEARCH);
        innerField = new TextField();
        innerField.setImmediate(true);
        innerField.setInputPrompt("Search");
        innerField.setWidth("180px");
        this.with(icon, innerField).withStyleName("searchfield");
        ShortcutListener shortcutListener = new ShortcutListener("searchfield", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                String value = ((TextField) target).getValue();
                if (isNotBlank(value)) {
                    doSearch(value);
                } else {
                    emptySearch();
                }
            }
        };
        ShortcutExtension.installShortcutAction(innerField, shortcutListener);
    }

    abstract public void doSearch(String value);

    abstract public void emptySearch();

    public void setInputPrompt(String value) {
        innerField.setInputPrompt(value);
    }
}
