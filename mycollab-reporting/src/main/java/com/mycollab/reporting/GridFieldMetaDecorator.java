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
package com.mycollab.reporting;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.reporting.expression.PrimaryTypeFieldExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class GridFieldMetaDecorator extends GridFieldMeta {
    private GridFieldMeta tableField;

    private ComponentBuilder componentBuilder;

    GridFieldMetaDecorator(GridFieldMeta tableField) {
        super(null, "", -1);
        this.tableField = tableField;
    }

    @Override
    public String getField() {
        return tableField.getField();
    }

    @Override
    public void setField(String field) {
        tableField.setField(field);
    }

    public Enum getDescKey() {
        return tableField.getDescKey();
    }

    public void setDescKey(Enum descKey) {
        tableField.setDescKey(descKey);
    }

    @Override
    public Integer getDefaultWidth() {
        return tableField.getDefaultWidth();
    }

    @Override
    public void setDefaultWidth(Integer defaultWidth) {
        tableField.setDefaultWidth(defaultWidth);
    }

    public ComponentBuilder getComponentBuilder() {
        if (componentBuilder == null) {
            componentBuilder = cmp.text(new PrimaryTypeFieldExpression(tableField.getField())).setWidth(
                    tableField.getDefaultWidth());
        }
        return componentBuilder;
    }

    public void setComponentBuilder(ComponentBuilder componentBuilder) {
        this.componentBuilder = componentBuilder;
    }
}
