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
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
public class GridCellWrapper extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    private String fieldWidth;
    private MHorizontalLayout fieldWrapper;

    GridCellWrapper(Component caption, String fieldWidth) {
        fieldWrapper = new MHorizontalLayout();
        this.withSpacing(true).with(caption, fieldWrapper).expand(fieldWrapper).withAlign(caption, Alignment.TOP_RIGHT).withMargin(true).withFullWidth().withStyleName("gridform-field");
        this.fieldWidth = fieldWidth;
    }

    public void addField(Component field) {
        fieldWrapper.removeAllComponents();
        if (!(field instanceof Button)) {
            field.setCaption(null);
            this.setWidth(fieldWidth);
        }

        if (field instanceof MultiSelectComp || field instanceof DateField) {
            field.setWidth("200px");
        } else  {
            field.setWidth("100%");
        }
        fieldWrapper.with(field);
    }
}