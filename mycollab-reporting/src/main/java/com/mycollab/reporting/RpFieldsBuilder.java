package com.mycollab.reporting;

import com.mycollab.common.TableViewField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RpFieldsBuilder {
    private List<TableViewFieldDecorator> viewFields;

    public RpFieldsBuilder(Collection<TableViewField> fields) {
        viewFields = new ArrayList<>();

        for (TableViewField field : fields) {
            TableViewFieldDecorator fieldDecorator = new TableViewFieldDecorator(field);
            viewFields.add(fieldDecorator);
        }
    }

    public List<TableViewFieldDecorator> getFields() {
        return viewFields;
    }

}
