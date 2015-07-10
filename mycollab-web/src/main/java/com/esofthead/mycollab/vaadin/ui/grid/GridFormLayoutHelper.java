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
package com.esofthead.mycollab.vaadin.ui.grid;

import com.esofthead.mycollab.vaadin.ui.MultiSelectComp;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

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

    public GridFormLayoutHelper() {
        this(0, 0);
    }

    public GridFormLayoutHelper(int columns, int rows) {
        this(columns, rows, UIConstants.DEFAULT_CONTROL_WIDTH, UIConstants.DEFAULT_CAPTION_FORM_WIDTH, Alignment.TOP_LEFT);
    }

    public GridFormLayoutHelper(int columns, int rows, String fieldControlWidth, String defaultCaptionWidth) {
        this(columns, rows, fieldControlWidth, defaultCaptionWidth, Alignment.TOP_RIGHT);
    }

    public GridFormLayoutHelper(int columns, int rows, String fieldControlWidth,
                                String defaultCaptionWidth, Alignment captionAlignment) {
        this.fieldControlWidth = fieldControlWidth;
        this.defaultCaptionWidth = defaultCaptionWidth;
        this.captionAlignment = captionAlignment;

        this.layout = new GridLayout(2 * columns, rows);
        this.layout.setMargin(false);
        this.layout.setSpacing(false);
        this.layout.setRowExpandRatio(0, 0);
    }

    public static GridFormLayoutHelper defaultFormLayoutHelper(int columns, int rows) {
        GridFormLayoutHelper helper = new GridFormLayoutHelper(columns, rows, "100%", "167px", Alignment.TOP_LEFT);
        helper.getLayout().setWidth("100%");
        helper.getLayout().addStyleName("colored-gridlayout");
        return helper;
    }

    public void appendRow() {
        this.layout.setRows(layout.getRows() + 1);
    }

    public int getRows() {
        return this.layout.getRows();
    }

    public void addComponent(Component field, String caption, int columns,
                             int rows, int colspan, String width) {
        this.addComponent(field, caption, columns, rows, colspan, width,
                this.captionAlignment);
    }

    public void addComponent(Component field, String caption, int columns, int rows) {
        this.addComponent(field, caption, columns, rows, 1,
                this.fieldControlWidth, captionAlignment);
    }

    public void addComponent(Component field, String caption,
                             int columns, int rows, Alignment alignment) {
        this.addComponent(field, caption, columns, rows, 1,
                this.fieldControlWidth, alignment);
    }

    public void addComponent(Component field, String caption, int columns,
                             int rows, int colspan, String width, Alignment alignment) {
        GridCellWrapper cell = buildCell(caption, columns, rows, colspan,
                width, alignment);
        cell.addComponent(field);
    }

    public GridCellWrapper buildCell(String caption, int columns, int rows) {
        return buildCell(caption, columns, rows, 1, fieldControlWidth,
                captionAlignment);
    }

    public GridCellWrapper buildCell(String caption, int columns, int rows,
                                     int colspan, String width, Alignment alignment) {
        if (StringUtils.isNotBlank(caption)) {
            Label captionLbl = new Label(caption);
            MHorizontalLayout captionWrapper = new MHorizontalLayout().withSpacing(false).withMargin(true)
                    .withWidth(this.defaultCaptionWidth).withHeight("100%").withStyleName("gridform-caption").with(captionLbl)
                    .withAlign(captionLbl, alignment);
            if (columns == 0) {
                captionWrapper.addStyleName("first-col");
            }
            if (rows == 0) {
                captionWrapper.addStyleName("first-row");
            }
            this.layout.addComponent(captionWrapper, 2 * columns, rows);
        }
        GridCellWrapper fieldWrapper = new GridCellWrapper();

        if (rows == 0) {
            fieldWrapper.addStyleName("first-row");
        }
        fieldWrapper.setWidth(width);
        this.layout.addComponent(fieldWrapper, 2 * columns + 1, rows, 2 * (columns + colspan - 1) + 1, rows);
        this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);

        if (StringUtils.isNotBlank(caption)) {
            fieldCaptionMappings.put(caption, fieldWrapper);
        }
        return fieldWrapper;
    }

    /**
     * @param caption
     * @return null if it can not find the component wrapper associates with
     * <code>caption</code>
     */
    public GridCellWrapper getComponentWrapper(String caption) {
        return fieldCaptionMappings.get(caption);
    }

    public Component addComponentNoWrapper(Component field, String caption, int columns, int rows) {
        if (caption != null) {
            Label l = new Label(caption);
            l.setWidth(this.defaultCaptionWidth);
            this.layout.addComponent(l, 2 * columns, rows);
            this.layout.setComponentAlignment(l, this.captionAlignment);
        }
        if (!(field instanceof Button))
            field.setCaption(null);

        if (field instanceof MultiSelectComp) {
            field.setWidth("200px");
        } else {
            field.setWidth(fieldControlWidth);
        }

        this.layout.addComponent(field, 2 * columns + 1, rows);
        this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
        return field;
    }

    public GridLayout getLayout() {
        return this.layout;
    }
}
