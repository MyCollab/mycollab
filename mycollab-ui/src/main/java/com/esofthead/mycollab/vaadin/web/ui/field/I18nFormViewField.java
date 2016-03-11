/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.web.ui.field;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.ui.UIClientRpc;
import com.vaadin.shared.ui.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import org.vaadin.jouni.restrain.Restrain;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class I18nFormViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private CssLayout wrapper;
    private Label label;
    private String key;
    private Class<? extends Enum> enumClass;

    public I18nFormViewField(final String key, Class<? extends Enum> enumCls) {
        this.key = key;
        this.enumClass = enumCls;
        wrapper = new CssLayout();
        wrapper.setWidth("100%");
        label = new Label();
        label.setContentMode(ContentMode.TEXT);
        new Restrain(label).setMaxWidth("100%");
        wrapper.addComponent(label);

        if (StringUtils.isNotBlank(key)) {
            try {
                String value = AppContext.getMessage(enumClass, key);
                label.setValue(value);
            } catch (Exception e) {
                label.setValue("");
            }
        } else {
            label.setValue("");
        }
    }

    public I18nFormViewField withStylename(String stylename) {
        label.addStyleName(stylename);
        return this;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        return wrapper;
    }
}
