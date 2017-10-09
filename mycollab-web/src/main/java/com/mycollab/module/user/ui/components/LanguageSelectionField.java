package com.mycollab.module.user.ui.components;

import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class LanguageSelectionField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private ComboBox languageBox = new ComboBox();
    private Label languageCode;

    public LanguageSelectionField() {
        languageBox.setNullSelectionAllowed(false);
        languageBox.setImmediate(true);
        languageBox.setWidth("200px");
        languageBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        languageBox.setFilteringMode(FilteringMode.CONTAINS);

        languageCode = new ELabel().withStyleName(UIConstants.META_INFO);

        Locale[] supportedLanguage = LocalizationHelper.getAvailableLocales();
        for (Locale locale : supportedLanguage) {
            String language = locale.toLanguageTag();
            languageBox.addItem(language);
            languageBox.setItemCaption(language, locale.getDisplayName(locale));
        }
        languageBox.addValueChangeListener(valueChangeEvent -> {
            String value = (String) valueChangeEvent.getProperty().getValue();
            languageCode.setValue(value != null ? value : "");
        });
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(languageBox, languageCode).alignAll(Alignment.MIDDLE_LEFT);
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        languageBox.setValue(newDataSource.getValue());
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        String value = (String) languageBox.getValue();
        setInternalValue(value);
        super.commit();
    }

    @Override
    public void setValue(String newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        languageBox.setValue(newFieldValue);
        super.setValue(newFieldValue);
    }

    @Override
    public String getValue() {
        return (String) languageBox.getValue();
    }
}
