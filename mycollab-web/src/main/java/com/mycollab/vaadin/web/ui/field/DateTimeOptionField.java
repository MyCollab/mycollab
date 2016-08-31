/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui.field;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.PopupDateFieldExt;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.ValueComboBox;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.joda.time.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class DateTimeOptionField extends CustomField<Date> {
    private static final long serialVersionUID = 1L;

    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    private MHorizontalLayout container;
    private PopupDateFieldExt popupDateField;
    private HourPickerComboBox hourPickerComboBox;
    private MinutePickerComboBox minutePickerComboBox;
    private ValueComboBox timeFormatComboBox;
    private Button toggleTimeBtn;
    private boolean hideTimeOption = true;
    private boolean trickModified = false;

    public DateTimeOptionField() {
        this(false);
    }

    public DateTimeOptionField(boolean hideTimeOptionVal) {
        this.hideTimeOption = hideTimeOptionVal;
        popupDateField = new PopupDateFieldExt();
        popupDateField.addValueChangeListener(valueChangeEvent -> {
            Date date = (Date) valueChangeEvent.getProperty().getValue();
            if (date != null) {
                trickModified = true;
                popupDateField.setPropertyDataSource(new ObjectProperty(date));
            }
        });
        popupDateField.setImmediate(true);
        popupDateField.setResolution(Resolution.DAY);
        hourPickerComboBox = new HourPickerComboBox();
        hourPickerComboBox.setWidth("60px");

        minutePickerComboBox = new MinutePickerComboBox();
        minutePickerComboBox.setImmediate(true);
        minutePickerComboBox.setWidth("60px");

        timeFormatComboBox = new ValueComboBox();
        timeFormatComboBox.setWidth("65px");
        timeFormatComboBox.setImmediate(true);
        timeFormatComboBox.setCaption(null);
        timeFormatComboBox.loadData("AM", "PM");
        timeFormatComboBox.setNullSelectionAllowed(false);

        toggleTimeBtn = new Button("", clickEvent -> toggleHideTimeOption(!hideTimeOption));
        toggleTimeBtn.addStyleName(WebUIConstants.BUTTON_LINK);

        container = new MHorizontalLayout();
        container.addStyleName(WebUIConstants.FLEX_DISPLAY);

        if (hideTimeOption) {
            toggleTimeBtn.setCaption(AppContext.getMessage(GenericI18Enum.ACTION_SET_TIME));
            container.with(popupDateField, toggleTimeBtn);
        } else {
            toggleTimeBtn.setCaption(AppContext.getMessage(GenericI18Enum.ACTION_HIDE_TIME));
            container.with(popupDateField, new MHorizontalLayout(hourPickerComboBox, minutePickerComboBox,
                    timeFormatComboBox), toggleTimeBtn);
        }
    }

    @Override
    public boolean isModified() {
        return popupDateField.isModified() || hourPickerComboBox.isModified() || minutePickerComboBox.isModified() ||
                timeFormatComboBox.isModified() || trickModified;
    }

    @Override
    public void setBuffered(boolean buffered) {
        popupDateField.setBuffered(buffered);
        hourPickerComboBox.setBuffered(buffered);
        minutePickerComboBox.setBuffered(buffered);
        timeFormatComboBox.setBuffered(buffered);
        super.setBuffered(buffered);
    }

    @Override
    protected Component initContent() {
        return container;
    }

    private void toggleHideTimeOption(boolean isHideOption) {
        this.hideTimeOption = isHideOption;
        trickModified = true;
        container.removeAllComponents();
        if (hideTimeOption) {
            toggleTimeBtn.setCaption(AppContext.getMessage(GenericI18Enum.ACTION_SET_TIME));
            container.with(popupDateField, toggleTimeBtn);
        } else {
            toggleTimeBtn.setCaption(AppContext.getMessage(GenericI18Enum.ACTION_HIDE_TIME));
            container.with(popupDateField, new MHorizontalLayout(hourPickerComboBox, minutePickerComboBox,
                    timeFormatComboBox), toggleTimeBtn);
        }
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Date value = (Date) newDataSource.getValue();
        if (value != null) {
            DateTime jodaTime = new DateTime(value);
            jodaTime = jodaTime.toDateTime(DateTimeZone.forTimeZone(AppContext.getUserTimeZone()));
            int hrs = jodaTime.getHourOfDay();
            int min = jodaTime.getMinuteOfHour();
            String timeFormat = "AM";
            if (hrs > 12) {
                hrs -= 12;
                timeFormat = "PM";
            }

            if ((hrs > 0 || min > 0) && hideTimeOption) {
                toggleHideTimeOption(false);
            }

            popupDateField.setPropertyDataSource(new ObjectProperty(jodaTime.toDate()));
            if (!hideTimeOption) {
                hourPickerComboBox.setPropertyDataSource(new ObjectProperty((hrs < 10) ? "0" + hrs : "" + hrs));
                minutePickerComboBox.setPropertyDataSource(new ObjectProperty((min < 10) ? "0" + min : "" + min));
                timeFormatComboBox.setPropertyDataSource(new ObjectProperty(timeFormat));
            }
        }
        super.setPropertyDataSource(newDataSource);
    }

    private long calculateMilliSeconds(Integer hour, Integer minus, String timeFormat) {
        long allMinus = 0;
        if (timeFormat.equals("AM")) {
            allMinus = (((hour == 12) ? 0 : hour) * 60 + minus) * ONE_MINUTE_IN_MILLIS;
        } else if (timeFormat.equals("PM")) {
            allMinus = (((hour == 12) ? 12 : hour + 12) * 60 + minus) * ONE_MINUTE_IN_MILLIS;
        }
        return allMinus;
    }

    @Override
    public void commit() throws SourceException, InvalidValueException {
        Date internalValue = getDateValue();
        super.setInternalValue(internalValue);
        super.commit();
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    private Date getDateValue() {
        Date selectDate = popupDateField.getValue();
        if (selectDate == null) {
            return null;
        }

        DateTime jodaSelectDate = new DateTime(selectDate).toDateTime(DateTimeZone.forTimeZone(AppContext.getUserTimeZone()));
        Date baseDate = new LocalDate(jodaSelectDate).toDate();
        if (hideTimeOption) {
            return new LocalDateTime(baseDate).toDateTime(DateTimeZone.forTimeZone(AppContext.getUserTimeZone())).toDate();
        } else {
            Integer hour = (hourPickerComboBox.getValue() != null) ? Integer.parseInt((String) hourPickerComboBox.getValue()) : 0;
            Integer minus = (minutePickerComboBox.getValue() != null) ? Integer.parseInt((String) minutePickerComboBox.getValue()) : 0;
            String timeFormat = (timeFormatComboBox.getValue() != null) ? (String) timeFormatComboBox.getValue() : "AM";
            long milliseconds = calculateMilliSeconds(hour, minus, timeFormat);
            DateTime jodaTime = new DateTime(baseDate).plus(new Duration(milliseconds));
            return new LocalDateTime(jodaTime.getMillis()).toDateTime(DateTimeZone.forTimeZone(AppContext.getUserTimeZone())).toDate();
        }
    }

    private static class HourPickerComboBox extends ValueComboBox {
        private static final long serialVersionUID = 1L;
        private final String[] HOURS = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};

        HourPickerComboBox() {
            super();
            setCaption(null);
            this.loadData(HOURS);
        }
    }

    private static class MinutePickerComboBox extends ValueComboBox {
        private static final long serialVersionUID = 1L;
        private String[] MINUS = new String[]{"00", "15", "30", "45"};

        MinutePickerComboBox() {
            super();
            setCaption(null);
            this.loadData(MINUS);
        }
    }
}