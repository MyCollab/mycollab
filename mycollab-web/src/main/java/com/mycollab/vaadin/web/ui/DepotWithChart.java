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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public abstract class DepotWithChart extends Depot {
    private MButton toggleViewBtn;
    private boolean isPlainMode = true;

    public DepotWithChart() {
        super("", new MVerticalLayout());

        toggleViewBtn = new MButton("", clickEvent -> {
            isPlainMode = !isPlainMode;
            if (isPlainMode) {
                toggleViewBtn.setIcon(FontAwesome.BAR_CHART_O);
                toggleViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_CHART_MODE));
                displayPlainMode();
            } else {
                toggleViewBtn.setIcon(FontAwesome.LIST);
                toggleViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_SIMPLE_MODE));
                displayChartMode();
            }
        }).withIcon(FontAwesome.BAR_CHART_O).withStyleName(WebUIConstants.BUTTON_ICON_ONLY);
        toggleViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_SIMPLE_MODE));
        addHeaderElement(toggleViewBtn);
        setContentBorder(true);
        this.setMargin(new MarginInfo(false, false, true, false));
    }

    abstract protected void displayPlainMode();

    abstract protected void displayChartMode();
}
