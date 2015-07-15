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

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class OptionPopupContent extends MVerticalLayout {
    public OptionPopupContent() {
        withSpacing(false).withMargin(false).withStyleName(UIConstants.OPTION_POPUP_CONTENT).withWidth("200px");
    }

    @Override
    public OptionPopupContent withWidth(String width) {
        return (OptionPopupContent) super.withWidth(width);
    }

    public void addOption(Component btn) {
        CssLayout wrap = new CssLayout();
        btn.setWidth("100%");
        btn.setStyleName("action");
        wrap.addStyleName("action-wrap");
        wrap.addComponent(btn);
        super.addComponent(wrap);
    }

    @Override
    public void addComponent(Component c) {
        this.addOption(c);
    }
}
