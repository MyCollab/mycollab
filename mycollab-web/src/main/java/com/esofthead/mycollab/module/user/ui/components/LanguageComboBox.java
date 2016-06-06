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
package com.esofthead.mycollab.module.user.ui.components;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
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
public class LanguageComboBox extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private ComboBox languageBox = new ComboBox();
    private Label languageCode;

    public LanguageComboBox() {
        languageBox.setNullSelectionAllowed(false);
        languageBox.setImmediate(true);
        languageBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        languageBox.setFilteringMode(FilteringMode.CONTAINS);

        languageCode = new ELabel().withStyleName(UIConstants.LABEL_META_INFO);

        Locale[] supportedLanguage = LocalizationHelper.getAvailableLocales();
        for (Locale locale : supportedLanguage) {
            String language = locale.toLanguageTag();
            languageBox.addItem(language);
            languageBox.setItemCaption(language, locale.getDisplayName(AppContext.getUserLocale()));
        }
        languageBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String value = (String) valueChangeEvent.getProperty().getValue();
                languageCode.setValue(value != null ? value : "");
            }
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
