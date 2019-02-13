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

import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public final class DefaultViewField extends CustomField<Object> {
    private static final long serialVersionUID = 1L;

    private ELabel label;
    private String value;

    public DefaultViewField() {
        this("");
    }

    public DefaultViewField(String value) {
        this(value, ContentMode.TEXT);
    }

    public DefaultViewField(String value, ContentMode contentMode) {
        this.value = value;
        label = new ELabel(value, contentMode).withFullWidth().withStyleName(WebThemes.LABEL_WORD_WRAP)
                .withUndefinedWidth().withDescription(value);
    }

    public DefaultViewField withStyleName(String styleName) {
        label.addStyleName(styleName);
        return this;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    protected void doSetValue(Object value) {
        label.setValue((value != null) ? value.toString() : "");
    }
}
