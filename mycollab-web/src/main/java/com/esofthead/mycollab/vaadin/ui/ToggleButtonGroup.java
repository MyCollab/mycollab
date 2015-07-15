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
package com.esofthead.mycollab.vaadin.ui;

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
        super();
        this.addStyleName("toggle-btn-group");
    }

    @Override
    public Button addButton(Button button) {
        super.addButton(button);
        button.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = -411946964127842982L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!event.getButton().equals(selectedBtn)) {
                    selectedBtn = event.getButton();
                    Iterator<Component> iterator = ToggleButtonGroup.this.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().removeStyleName(UIConstants.BTN_ACTIVE);
                    }
                    selectedBtn.addStyleName(UIConstants.BTN_ACTIVE);
                }
            }
        });
        return button;
    }

    public void setDefaultButton(Button button) {
        Iterator<Component> iterator = ToggleButtonGroup.this.iterator();
        while (iterator.hasNext()) {
            Button currentBtn = (Button) iterator.next();
            if (currentBtn.equals(button)) {
                selectedBtn = button;
                selectedBtn.addStyleName(UIConstants.BTN_ACTIVE);
            } else {
                currentBtn.removeStyleName(UIConstants.BTN_ACTIVE);
            }
        }
    }

}
