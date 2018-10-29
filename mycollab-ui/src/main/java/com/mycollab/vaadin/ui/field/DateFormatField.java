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
package com.mycollab.vaadin.ui.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class DateFormatField extends CustomField<String> {
    private TextField dateInput;
    private Label dateExample;
    private DateTime now;
    private DateTimeFormatter dateFormatInstance;

    public DateFormatField(final String initialFormat) {
//        dateInput = new TextField(null, initialFormat);
//        dateInput.setImmediate(true);
//        dateInput.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
//        now = new DateTime();
//        dateExample = new Label();
//        dateFormatInstance = DateTimeFormat.forPattern(initialFormat);
//        dateExample.setValue(String.format("(%s)", dateFormatInstance.print(now)));
//        dateExample.setWidthUndefined();
//        dateInput.addTextChangeListener(textChangeEvent -> {
//            try {
//                String newFormat = textChangeEvent.getText();
//                dateFormatInstance = DateTimeFormat.forPattern(newFormat);
//                dateExample.setValue(String.format("(%s)", dateFormatInstance.print(now)));
//            } catch (Exception e) {
//                NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.INVALID_FORMAT));
//                dateInput.setValue(initialFormat);
//                dateFormatInstance = DateTimeFormat.forPattern(initialFormat);
//                dateExample.setValue(String.format("(%s)", dateFormatInstance.print(now)));
//            }
//        });
    }

    @Override
    public String getValue() {
        return dateInput.getValue();
    }

//    @Override
//    public void commit() throws SourceException, Validator.InvalidValueException {
//        setInternalValue(dateInput.getValue());
//        super.commit();
//    }
//
//    @Override
//    protected Component initContent() {
//        return new MHorizontalLayout(dateInput, dateExample);
//    }
//
//    @Override
//    public Class<? extends String> getType() {
//        return String.class;
//    }


    @Override
    protected Component initContent() {
        return null;
    }

    @Override
    protected void doSetValue(String s) {

    }
}
