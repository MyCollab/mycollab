/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
