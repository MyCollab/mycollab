package com.mycollab.vaadin.ui;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DateSelectionField extends CustomField<Date> {
    private static final long serialVersionUID = 1L;

    private ComboBox cboYear;
    private ComboBox cboMonth;
    private ComboBox cboDate;

    private Map<String, Integer> mapNumberMonth = new HashMap<>();

    public DateSelectionField() {
        cboMonth = new ComboBox();
        cboMonth.setNullSelectionAllowed(true);
        cboMonth.setPageLength(12);
        cboMonth.setImmediate(true);

        addMonthItems();
        cboMonth.setWidth("117px");

        cboDate = new ComboBox();
        cboDate.setNullSelectionAllowed(true);
        cboDate.setImmediate(true);

        addDayItems();
        cboDate.setWidth("80px");

        cboYear = new ComboBox();
        cboYear.setNullSelectionAllowed(true);
        cboYear.setImmediate(true);

        addYearItems();
        cboYear.setWidth("80px");
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout layout = new MHorizontalLayout().with(cboMonth, cboDate, cboYear);
        layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        return layout;
    }

    private String formatMonth(String month) {
        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM", UserUIContext.getUserLocale());
        try {
            return monthDisplay.format(monthParse.parse(month));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addMonthItems() {
        for (int i = 1; i <= 12; i++) {
            cboMonth.addItem(formatMonth(i + ""));
            mapNumberMonth.put(formatMonth(i + ""), i);
        }
    }

    private void addDayItems() {
        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                cboDate.addItem("0" + i);
            } else {
                cboDate.addItem(i + "");
            }
        }
    }

    private void addYearItems() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        for (int i = (currentYear - 10); i >= 1900; i--) {
            cboYear.addItem(i + "");
        }
    }

    public void setDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int dateVal = calendar.get(Calendar.DATE);
            if (dateVal < 10) {
                cboDate.select("0" + dateVal);
            } else {
                cboDate.select(dateVal + "");
            }

            cboMonth.select(formatMonth(calendar.get(Calendar.MONTH) + 1 + ""));
            cboYear.select(calendar.get(Calendar.YEAR) + "");
        } else {
            cboDate.select("");
            cboMonth.select("");
            cboYear.select("");
        }
    }

    public Date getDate() {
        if ((cboDate.getValue() != null) && (cboMonth.getValue() != null)
                && (cboYear.getValue() != null)) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Integer.parseInt(cboYear.getValue().toString()),
                    mapNumberMonth.get(cboMonth.getValue().toString()) - 1,
                    Integer.parseInt(cboDate.getValue().toString()));
            return calendar.getTime();
        } else {
            return null;
        }

    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Date value = (Date) newDataSource.getValue();
        if (value != null) {
            this.setDate(value);
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void commit() throws SourceException, InvalidValueException {
        Date value = getDate();
        setInternalValue(value);
        super.commit();
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }
}
