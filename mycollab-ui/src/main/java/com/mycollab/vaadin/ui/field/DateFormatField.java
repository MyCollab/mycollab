package com.mycollab.vaadin.ui.field;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class DateFormatField extends CustomField<String> {
    private String dateFormat;
    private TextField dateInput;
    private Label dateExample;
    private DateTime now;
    private DateTimeFormatter dateFormatInstance;

    public DateFormatField(final String initialFormat) {
        this.dateFormat = initialFormat;
        dateInput = new TextField(null, initialFormat);
        dateInput.setImmediate(true);
        dateInput.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        now = new DateTime();
        dateExample = new Label();
        dateFormatInstance = DateTimeFormat.forPattern(dateFormat);
        dateExample.setValue("(" + dateFormatInstance.print(now) + ")");
        dateExample.setWidthUndefined();
        dateInput.addTextChangeListener(textChangeEvent -> {
            try {
                String newFormat = textChangeEvent.getText();
                dateFormatInstance = DateTimeFormat.forPattern(newFormat);
                dateExample.setValue("(" + dateFormatInstance.print(now) + ")");
            } catch (Exception e) {
                NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.INVALID_FORMAT));
                dateInput.setValue(initialFormat);
                dateFormatInstance = DateTimeFormat.forPattern(initialFormat);
                dateExample.setValue("(" + dateFormatInstance.print(now) + ")");
            }
        });
    }

    @Override
    public String getValue() {
        return dateInput.getValue();
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
