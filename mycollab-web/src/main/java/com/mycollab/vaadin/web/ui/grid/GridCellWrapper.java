/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui.grid;

import com.mycollab.vaadin.web.ui.MultiSelectComp;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
public class GridCellWrapper extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private Component captionComp;
    private MHorizontalLayout fieldWrapper;

    GridCellWrapper(Component captionComp) {
        this.captionComp = captionComp;
        fieldWrapper = new MHorizontalLayout().withFullWidth();
        this.withSpacing(true).with(captionComp, fieldWrapper).withAlign(captionComp, Alignment.TOP_LEFT).withMargin(true).withFullWidth().withStyleName("gridform-field");
    }

    public void addField(Component field) {
        if (field instanceof HasValue && ((HasValue)field).isRequiredIndicatorVisible()) {
            captionComp.addStyleName(WebThemes.REQUIRED_FIELD_INDICATOR);
        }
        fieldWrapper.removeAllComponents();
        field.setCaption(null);

        if (field.getWidth() != -1) {
            // continue
        } else if (field instanceof MultiSelectComp || field instanceof DateField) {
            field.setWidth(WebThemes.FORM_CONTROL_WIDTH);
        } else {
            field.setWidth("100%");
        }
        fieldWrapper.withComponent(field);
    }
}