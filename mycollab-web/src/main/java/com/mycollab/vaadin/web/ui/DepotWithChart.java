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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public abstract class DepotWithChart extends Depot {
    private Button toggleViewBtn;
    private boolean isPlainMode = true;

    public DepotWithChart() {
        super("", new MVerticalLayout());

        toggleViewBtn = new Button(null, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                isPlainMode = !isPlainMode;
                if (isPlainMode) {
                    toggleViewBtn.setIcon(FontAwesome.BAR_CHART_O);
                    toggleViewBtn.setDescription("Chart mode");
                    displayPlainMode();
                } else {
                    toggleViewBtn.setIcon(FontAwesome.LIST);
                    toggleViewBtn.setDescription("Simple mode");
                    displayChartMode();
                }
            }
        });
        toggleViewBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);
        toggleViewBtn.setIcon(FontAwesome.BAR_CHART_O);
        toggleViewBtn.setDescription("Simple mode");
        addHeaderElement(toggleViewBtn);
        setContentBorder(true);
        this.setMargin(new MarginInfo(false, false, true, false));
    }

    abstract protected void displayPlainMode();

    abstract protected void displayChartMode();
}
