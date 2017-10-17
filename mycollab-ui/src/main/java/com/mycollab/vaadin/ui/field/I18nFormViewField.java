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

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class I18nFormViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private ELabel label;

    public I18nFormViewField(final String key, Class<? extends Enum> enumCls) {
        label = new ELabel("", ContentMode.TEXT).withWidthUndefined().withStyleName(UIConstants.LABEL_WORD_WRAP);

        if (StringUtils.isNotBlank(key)) {
            try {
                String value = UserUIContext.getMessage(enumCls, key);
                label.setValue(value);
            } catch (Exception ignored) { }
        }
    }

    public I18nFormViewField withStyleName(String styleName) {
        label.addStyleName(styleName);
        return this;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        return label;
    }
}
