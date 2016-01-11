/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UIUtils;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class FormSectionBuilder {
    public static final MHorizontalLayout build(FontAwesome icon, Component comp) {
        MHorizontalLayout layout = new MHorizontalLayout().withStyleName(UIConstants.FORM_SECTION);
        layout.with(new ELabel(icon.getHtml(), ContentMode.HTML).withWidthUndefined(), comp).expand(comp);
        layout.setWidth(UIUtils.getBrowserWidthInPixels());
        return layout;
    }

    public static final MCssLayout build(String title) {
        Label header = new Label(title);
        return new MCssLayout(header).withStyleName(UIConstants.FORM_SECTION);
    }
}
