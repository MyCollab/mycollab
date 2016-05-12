/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.web.ui.field;

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class DateFormatField extends CustomField<String> {
    private String dateFormat;
    private TextField dateInput;
    private Label dateExample;
    private Date now;
    private SimpleDateFormat dateFormatInstance;

    public DateFormatField(final String initialFormat) {
        this.dateFormat = initialFormat;
        dateInput = new TextField(null, initialFormat);
        dateInput.setImmediate(true);
        dateInput.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        now = new GregorianCalendar().getTime();
        dateExample = new Label();
        dateFormatInstance = DateTimeUtils.getDateFormat(dateFormat);
        dateExample.setValue("(" + dateFormatInstance.format(now) + ")");
        dateExample.setWidthUndefined();
        dateInput.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                try {
                    dateFormatInstance.applyPattern(event.getText());
                    dateExample.setValue("(" + dateFormatInstance.format(now) + ")");
                } catch (Exception e) {
                    NotificationUtil.showErrorNotification("Invalid format");
                    dateInput.setValue(initialFormat);
                    dateFormatInstance.applyPattern(initialFormat);
                    dateExample.setValue("(" + dateFormatInstance.format(now) + ")");
                }
            }
        });
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        setInternalValue(dateInput.getValue());
        super.commit();
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(dateInput, dateExample);
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
