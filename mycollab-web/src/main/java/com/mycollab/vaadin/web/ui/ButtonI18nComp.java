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

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class ButtonI18nComp extends MButton {
    private static final long serialVersionUID = 1L;

    private String key;

    public ButtonI18nComp(String key) {
        this.key = key;
    }

    public ButtonI18nComp(String key, Enum<?> caption, Button.ClickListener listener) {
        this.key = key;
        this.setCaption(UserUIContext.getMessage(caption));
        this.setDescription(UserUIContext.getMessage(caption));
        this.addClickListener(listener);
    }

    public String getKey() {
        return key;
    }
}
