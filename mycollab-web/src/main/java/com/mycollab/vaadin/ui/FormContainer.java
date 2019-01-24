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
package com.mycollab.vaadin.ui;

import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class FormContainer extends MVerticalLayout {
    public FormContainer() {
        this.withStyleName("form").withFullWidth().withMargin(false);
        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);
    }

    public FormSection addSection(String sectionName, ComponentContainer container) {
        FormSection formSection = new FormSection(sectionName, container);
        this.addComponent(formSection);
        return formSection;
    }

    public void addSection(Component sectionHeader, ComponentContainer container) {
        sectionHeader.addStyleName(WebThemes.FORM_SECTION);
        sectionHeader.setWidth("100%");
        container.setWidth("100%");
        this.addComponent(sectionHeader);
        this.addComponent(container);
    }
}
