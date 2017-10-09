package com.mycollab.reporting;

import com.mycollab.common.TableViewField;
import com.mycollab.reporting.expression.PrimaryTypeFieldExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TableViewFieldDecorator extends TableViewField {
    private TableViewField tableField;

    private ComponentBuilder componentBuilder;

    TableViewFieldDecorator(TableViewField tableField) {
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
