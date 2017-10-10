/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.ui;

import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class FormSectionBuilder {
    public static MHorizontalLayout build(FontAwesome icon, Component comp) {
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withStyleName(MobileUIConstants.FORM_SECTION);
        layout.with(ELabel.fontIcon(icon), comp).expand(comp).alignAll(Alignment.MIDDLE_LEFT);
        return layout;
    }

    public static MHorizontalLayout build(FontAwesome icon, Enum messageEnum) {
        return build(icon, new Label(UserUIContext.getMessage(messageEnum)));
    }

    public static MCssLayout build(String title) {
        Label header = new Label(title);
        return new MCssLayout(header).withFullWidth().withStyleName(MobileUIConstants.FORM_SECTION);
    }
}
