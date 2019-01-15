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

import com.jarektoro.responsivelayout.ResponsiveColumn;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
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

    private ResponsiveLayout responsiveLayout;

    private String fieldControlWidth;
    private String defaultCaptionWidth;

    private Map<String, GridCellWrapper> fieldCaptionMappings = new HashMap<>();

    public GridFormLayoutHelper(int columns, int rows, String fieldControlWidth, String defaultCaptionWidth) {
        this.fieldControlWidth = fieldControlWidth;
        this.defaultCaptionWidth = defaultCaptionWidth;

        responsiveLayout = new ResponsiveLayout(ResponsiveLayout.ContainerType.FIXED);
    }

    public static GridFormLayoutHelper defaultFormLayoutHelper(int columns, int rows) {
        return defaultFormLayoutHelper(columns, rows, "167px");
    }

    public static GridFormLayoutHelper defaultFormLayoutHelper(int columns, int rows, String controlWidth) {
        GridFormLayoutHelper helper = new GridFormLayoutHelper(columns, rows, "100%", controlWidth);
        helper.getLayout().setWidth("100%");
        helper.getLayout().addStyleName(WebThemes.GRIDFORM_STANDARD);
        return helper;
    }

    public GridFormLayoutHelper withCaptionWidth(String width) {
        this.defaultCaptionWidth = width;
        return this;
    }

    public <T> T addComponent(T field, String caption, int columns, int rows, int colSpan, String width) {
        return this.addComponent(field, caption, null, columns, rows, colSpan, width);
    }

    public <T> T addComponent(T field, String caption, int columns, int rows) {
        return this.addComponent(field, caption, null, columns, rows, 1, fieldControlWidth);
    }

    public <T> T addComponent(T field, String caption, String contextHelp, int columns, int rows) {
        return this.addComponent(field, caption, contextHelp, columns, rows, 1, fieldControlWidth);
    }

    public GridCellWrapper buildCell(String caption, String contextHelp, int columns, int rows) {
        return buildCell(caption, contextHelp, columns, rows, 1, fieldControlWidth);
    }

    public <T> T addComponent(T field, String caption, String contextHelp, int columns, int rows, int
            colSpan, String width) {
        GridCellWrapper cell = buildCell(caption, contextHelp, columns, rows, colSpan, width);
        cell.addField((Component) field);
        return field;
    }

    public GridCellWrapper buildCell(String caption, String contextHelp, int columns, int rows, int colSpan, String controlWidth) {
        ELabel captionLbl = new ELabel(caption).withStyleName(UIConstants.LABEL_WORD_WRAP).withDescription(caption);
        MHorizontalLayout captionWrapper = new MHorizontalLayout(captionLbl).withSpacing(false).withMargin(new MarginInfo(false, true, false, false))
                .withWidth(defaultCaptionWidth).withStyleName("gridform-caption").expand(captionLbl);
        if (StringUtils.isNotBlank(contextHelp)) {
            ELabel contextHelpLbl = ELabel.html("&nbsp;" + VaadinIcons.QUESTION_CIRCLE.getHtml())
                    .withStyleName(WebThemes.INLINE_HELP).withDescription(contextHelp).withUndefinedWidth();
            captionWrapper.with(contextHelpLbl);
        }

        GridCellWrapper fieldWrapper = new GridCellWrapper(captionWrapper, controlWidth);
        int rowCount = responsiveLayout.getComponentCount();
        for (int i = 0; i < rows - rowCount + 1; i++) {
            responsiveLayout.addRow();
        }
        ResponsiveRow responsiveRow = (ResponsiveRow) responsiveLayout.getComponent(rows);

        if (columns == 0) {
            if (responsiveRow.getComponentCount() == 0) {
                ResponsiveColumn column = (colSpan == 1) ? new ResponsiveColumn(12, 12, 6, 6) : new ResponsiveColumn(12, 12, 12, 12);
                column.setContent(fieldWrapper);
                responsiveRow.addColumn(column);
            } else {
                ResponsiveColumn column = (ResponsiveColumn) responsiveRow.getComponent(0);
                column.setContent(fieldWrapper);
            }
        } else if (columns == 1) {
            int columnCount = responsiveRow.getComponentCount();
            for (int i = 0; i < columns - columnCount + 1; i++) {
                ResponsiveColumn column = new ResponsiveColumn(12, 12, 6, 6);
                responsiveRow.addColumn(column);
            }

            ResponsiveColumn column = (ResponsiveColumn) responsiveRow.getComponent(columns);
            column.setContent(fieldWrapper);

        } else {
            throw new MyCollabException("Not support form 2 columns only");
        }

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

    public ResponsiveLayout getLayout() {
        return responsiveLayout;
    }
}
