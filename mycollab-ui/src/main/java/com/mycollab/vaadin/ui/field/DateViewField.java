package com.mycollab.vaadin.ui.field;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class DateViewField extends CustomField<Date> {
    private static final long serialVersionUID = 1L;

    private Date date;
    private Label dateLbl;

    public DateViewField(Date date) {
        this.date = date;
        dateLbl = new Label();
        dateLbl.setWidth("100%");
    }

    @Override
    public Class<? extends Date> getType() {
        return Date.class;
    }

    @Override
    protected Component initContent() {
        if (date == null) {
            dateLbl.setValue("&nbsp;");
            dateLbl.setContentMode(ContentMode.HTML);
        } else {
            dateLbl.setValue(UserUIContext.formatDate(date));
            dateLbl.setDescription(UserUIContext.formatPrettyTime(date));
        }
        return dateLbl;
    }
}
