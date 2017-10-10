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
package com.mycollab.mobile.ui.grid;

import com.mycollab.mobile.ui.MobileUIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class GridFormLayoutHelper implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GridLayout layout;
    private final String fieldControlWidth;
    private final String defaultCaptionWidth;
    private final Alignment captionAlignment;
    private Map<String, GridCellWrapper> fieldCaptionMappings = new HashMap<>();

    public GridFormLayoutHelper(final int columns, final int rows, final String fieldControlWidth, final String defaultCaptionWidth, final Alignment captionAlignment) {
        this.fieldControlWidth = fieldControlWidth;
        this.defaultCaptionWidth = defaultCaptionWidth;
        this.captionAlignment = captionAlignment;

        this.layout = new GridLayout(2 * columns, rows);
        this.layout.setMargin(false);
        this.layout.setSpacing(false);
        this.layout.setRowExpandRatio(0, 0);
    }

    public static GridFormLayoutHelper defaultFormLayoutHelper(int columns, int rows) {
        GridFormLayoutHelper helper = new GridFormLayoutHelper(columns, rows, "100%", "150px", Alignment.TOP_LEFT);
        helper.getLayout().setWidth("100%");
        helper.getLayout().addStyleName(MobileUIConstants.GRIDFORM_STANDARD);
        return helper;
    }

    public Component addComponent(final Component field, final String caption, final int columns, final int rows) {
        return this.addComponent(field, caption, columns, rows, fieldControlWidth, captionAlignment);
    }

    public Component addComponent(final Component field, final String caption, final int columns, final int rows,
                                  final String width, final Alignment alignment) {
        GridCellWrapper cell = buildCell(caption, columns, rows, width, alignment);
        cell.addComponent(field);
        return cell;
    }

    private GridCellWrapper buildCell(String caption, int columns, int rows, String width, Alignment alignment) {
        if (StringUtils.isNotBlank(caption)) {
            Label captionLbl = new Label(caption);
            MHorizontalLayout captionWrapper = new MHorizontalLayout().withSpacing(false).withMargin(true)
                    .withWidth(defaultCaptionWidth).withFullHeight().withStyleName("gridform-caption").with(captionLbl)
                    .withAlign(captionLbl, alignment);
            layout.addComponent(captionWrapper, 2 * columns, rows);
        }
        GridCellWrapper fieldWrapper = new GridCellWrapper();
        fieldWrapper.setWidth(width);
        layout.addComponent(fieldWrapper, 2 * columns + 1, rows);
        layout.setColumnExpandRatio(2 * columns + 1, 1.0f);

        if (StringUtils.isNotBlank(caption)) {
            fieldCaptionMappings.put(caption, fieldWrapper);
        }
        return fieldWrapper;
    }

    public Component getComponent(final int column, final int row) {
        return this.layout.getComponent(2 * column + 1, row);
    }

    public GridLayout getLayout() {
        return this.layout;
    }
}
