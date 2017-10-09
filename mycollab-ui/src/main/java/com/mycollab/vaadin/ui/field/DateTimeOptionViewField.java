package com.mycollab.vaadin.ui.field;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.3.3
 */
public class DateTimeOptionViewField extends CustomField<String> {
    private Date date;

    public DateTimeOptionViewField(Date dateVal) {
        this.date = dateVal;
    }

    @Override
    protected Component initContent() {
        String dateValue = (date == null) ? "" : UserUIContext.formatDateTime(date);
        return new Label(dateValue);
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
