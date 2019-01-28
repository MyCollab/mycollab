/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui.field;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class DateFormatField extends CustomField<String> {
    private TextField dateInput;
    private Label dateExample;
    private LocalDateTime now;
    private DateTimeFormatter dateFormatInstance;

    public DateFormatField(final String initialFormat) {
        dateInput = new TextField(null, initialFormat);
        now = LocalDateTime.now();
        dateExample = new Label();
        dateFormatInstance = DateTimeFormatter.ofPattern(initialFormat);
        dateExample.setValue(String.format("(%s)", dateFormatInstance.format(now)));
        dateExample.setWidthUndefined();

        dateInput.addValueChangeListener((ValueChangeListener<String>) event -> {
            try {
                String newFormat = event.getValue();
                dateFormatInstance = DateTimeFormatter.ofPattern(newFormat);
                dateExample.setValue(String.format("(%s)", dateFormatInstance.format(now)));
            } catch (Exception e) {
                NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.INVALID_FORMAT));
                dateInput.setValue(initialFormat);
                dateFormatInstance = DateTimeFormatter.ofPattern(initialFormat);
                dateExample.setValue(String.format("(%s)", dateFormatInstance.format(now)));
            }
        });
    }

    @Override
    public String getValue() {
        return dateInput.getValue();
    }


    @Override
    protected Component initContent() {
        return dateInput;
    }

    @Override
    protected void doSetValue(String s) {

    }
}
