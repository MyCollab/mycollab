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

import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MCssLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class FormContainer extends VerticalLayout {
    public FormContainer() {
        this.addStyleName("form");
        this.setWidth("100%");
        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);
    }

    public void addSection(String sectionName, ComponentContainer container) {
        this.addSection(new MCssLayout(new Label(sectionName)), container);
    }

    public void addSection(Component sectionHeader, ComponentContainer container) {
        sectionHeader.addStyleName("section");
        sectionHeader.setWidth("100%");
        container.setWidth("100%");
        this.addComponent(sectionHeader);
        this.addComponent(container);
    }
}
