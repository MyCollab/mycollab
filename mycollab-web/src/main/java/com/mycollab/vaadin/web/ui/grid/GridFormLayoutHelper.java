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
import com.mycollab.form.view.LayoutType;
import com.mycollab.vaadin.ui.ELabel;
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

    private LayoutType layoutType;
    private ResponsiveLayout responsiveLayout;

    private Map<String, GridCellWrapper> fieldCaptionMappings = new HashMap<>();

    public GridFormLayoutHelper(LayoutType layoutType) {
        this.layoutType = layoutType;
        responsiveLayout = new ResponsiveLayout(ResponsiveLayout.ContainerType.FIXED);
    }

    public static GridFormLayoutHelper defaultFormLayoutHelper(LayoutType layoutType) {
        GridFormLayoutHelper helper = new GridFormLayoutHelper(layoutType);
        helper.getLayout().setWidth("100%");
        helper.getLayout().addStyleName(WebThemes.GRIDFORM_STANDARD);
        return helper;
    }

    public <T> T addComponent(T field, String caption, int columns, int rows, int colSpan) {
        return this.addComponent(field, caption, null, columns, rows, colSpan);
    }

    public <T> T addComponent(T field, String caption, int columns, int rows) {
        return this.addComponent(field, caption, null, columns, rows, 1);
    }

    public <T> T addComponent(T field, String caption, String contextHelp, int columns, int rows) {
        return this.addComponent(field, caption, contextHelp, columns, rows, 1);
    }

    public <T> T addComponent(T field, String caption, String contextHelp, int columns, int rows, int colSpan) {
        GridCellWrapper cell = buildCell(caption, caption, contextHelp, columns, rows, colSpan);
        cell.addField((Component) field);
        return field;
    }

    public GridCellWrapper buildCell(String fieldId, String caption, String contextHelp, int columns, int rows) {
        return buildCell(fieldId, caption, contextHelp, columns, rows, 1);
    }

    public GridCellWrapper buildCell(String fieldId, String caption, String contextHelp, int columns, int rows, int colSpan) {
        MHorizontalLayout captionWrapper = null;
        if (!caption.equals("")) {
            ELabel captionLbl = new ELabel(caption).withStyleName(WebThemes.LABEL_WORD_WRAP).withDescription(caption);
            captionWrapper = new MHorizontalLayout(captionLbl).withSpacing(false).withMargin(new MarginInfo(false, true, false, false))
                    .withStyleName("gridform-caption");
            if (StringUtils.isNotBlank(contextHelp)) {
                ELabel contextHelpLbl = ELabel.html("&nbsp;" + VaadinIcons.QUESTION_CIRCLE.getHtml())
                        .withStyleName(WebThemes.INLINE_HELP).withDescription(contextHelp).withUndefinedWidth();
                captionWrapper.with(contextHelpLbl);
            }
        }

        GridCellWrapper fieldWrapper = new GridCellWrapper(captionWrapper);
        int rowCount = responsiveLayout.getComponentCount();
        for (int i = 0; i < rows - rowCount + 1; i++) {
            responsiveLayout.addRow();
        }
        ResponsiveRow responsiveRow = (ResponsiveRow) responsiveLayout.getComponent(rows);

        if (layoutType == LayoutType.ONE_COLUMN) {
            ResponsiveColumn column = new ResponsiveColumn(12, 12, 12, 12);
            column.setContent(fieldWrapper);
            responsiveRow.addColumn(column);
        } else {
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
        }

        fieldCaptionMappings.put(fieldId, fieldWrapper);
        return fieldWrapper;
    }

    /**
     * @param fieldId
     * @return null if it can not find the component wrapper associates with
     * <code>caption</code>
     */
    public GridCellWrapper getComponentWrapper(String fieldId) {
        return fieldCaptionMappings.get(fieldId);
    }

    public ResponsiveLayout getLayout() {
        return responsiveLayout;
    }
}
