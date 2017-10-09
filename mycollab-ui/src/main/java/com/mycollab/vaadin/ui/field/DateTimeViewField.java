package com.mycollab.vaadin.ui.field;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class DateTimeViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private Date date;

    public DateTimeViewField(Date date) {
        this.date = date;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        String dateValue = (date == null) ? "" : UserUIContext.formatDateTime(date);
        return new Label(dateValue);
    }
}
