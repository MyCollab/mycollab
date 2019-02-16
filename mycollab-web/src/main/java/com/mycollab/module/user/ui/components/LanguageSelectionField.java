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
package com.mycollab.module.user.ui.components;

import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class LanguageSelectionField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private ComboBox<Locale> languageBox = new ComboBox<>();
    private Label languageCode;

    public LanguageSelectionField() {
        languageBox.setEmptySelectionAllowed(false);
        languageBox.setWidth("200px");

        languageCode = new ELabel().withStyleName(WebThemes.META_INFO);

        Locale[] supportedLanguages = new Locale[]{Locale.US, Locale.SIMPLIFIED_CHINESE, Locale.GERMANY,
                new Locale("es", "ES"),
                new Locale("vi", "VI"),
                new Locale("pt", "BR"),
                new Locale("fr"),
                new Locale("ru"),
                new Locale("tr", "TR"),
                new Locale("fi", "FI"),
                Locale.JAPAN};
        languageBox.setItems(supportedLanguages);
        languageBox.setItemCaptionGenerator((ItemCaptionGenerator<Locale>) locale -> locale.getDisplayLanguage(locale));
        languageBox.addValueChangeListener(event -> {
            Locale locale = event.getValue();
            languageCode.setValue(locale != null ? locale.getDisplayLanguage(locale) : "");
        });
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(languageBox, languageCode).alignAll(Alignment.MIDDLE_LEFT);
    }

    @Override
    public String getValue() {
        Locale locale = languageBox.getValue();
        return (locale != null) ? locale.toLanguageTag() : "";
    }

    @Override
    protected void doSetValue(String value) {
        Locale locale = (value != null) ? Locale.forLanguageTag(value) : Locale.US;
        languageBox.setValue(locale);
    }
}