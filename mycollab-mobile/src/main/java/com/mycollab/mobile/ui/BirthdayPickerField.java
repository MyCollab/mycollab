package com.mycollab.mobile.ui;

import com.vaadin.addon.touchkit.ui.DatePicker;

import java.util.Calendar;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class BirthdayPickerField extends DatePicker {
    private static final long serialVersionUID = -7552080488000737394L;

    @Override
    public void attach() {
        super.attach();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        this.setMax(cal.getTime());
    }
}
