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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class I18nFormViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private Class<? extends Enum> enumCls;
    private Enum defaultValue;
    private ELabel label;

    public I18nFormViewField(Class<? extends Enum> enumCls) {
        this(enumCls, null);
    }

    public I18nFormViewField(Class<? extends Enum> enumCls, Enum defaultValue) {
        this.enumCls = enumCls;
        this.defaultValue = defaultValue;
        label = new ELabel("", ContentMode.TEXT).withUndefinedWidth().withStyleName(WebThemes.LABEL_WORD_WRAP);
    }

    public I18nFormViewField withStyleName(String styleName) {
        label.addStyleName(styleName);
        return this;
    }

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    protected void doSetValue(String value) {
        if (StringUtils.isNotBlank(value)) {
            label.setValue(UserUIContext.getMessage(enumCls, value));
        } else if (defaultValue != null) {
            label.setValue(UserUIContext.getMessage(defaultValue));
        }
    }

    @Override
    public String getValue() {
        return null;
    }
}
