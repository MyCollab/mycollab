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
package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.peter.buttongroup.ButtonGroup;

import java.util.Iterator;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ToggleButtonGroup extends ButtonGroup {
    private static final long serialVersionUID = 1L;

    private Button selectedBtn;

    public ToggleButtonGroup() {
        this.addStyleName("toggle-btn-group");
    }

    @Override
    public Button addButton(Button button) {
        super.addButton(button);
        button.addClickListener(clickEvent -> {
            if (!clickEvent.getButton().equals(selectedBtn)) {
                selectedBtn = clickEvent.getButton();
                for (Component component : ToggleButtonGroup.this) {
                    component.removeStyleName(WebThemes.BTN_ACTIVE);
                }
                selectedBtn.addStyleName(WebThemes.BTN_ACTIVE);
            }
        });
        return button;
    }

    public ButtonGroup withDefaultButton(Button button) {
        for (Component component : ToggleButtonGroup.this) {
            Button currentBtn = (Button) component;
            if (currentBtn.equals(button)) {
                selectedBtn = button;
                selectedBtn.addStyleName(WebThemes.BTN_ACTIVE);
            } else {
                currentBtn.removeStyleName(WebThemes.BTN_ACTIVE);
            }
        }
        return this;
    }

}
