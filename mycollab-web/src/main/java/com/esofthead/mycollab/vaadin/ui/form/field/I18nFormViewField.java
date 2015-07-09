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
package com.esofthead.mycollab.vaadin.ui.form.field;

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.3
 *
 */
public class I18nFormViewField extends CustomField<String> {
	private static final long serialVersionUID = 1L;

	private String key;
	@SuppressWarnings("rawtypes")
	private Class<? extends Enum> enumClass;

	@SuppressWarnings("rawtypes")
	public I18nFormViewField(final String key, Class<? extends Enum> enumCls) {
		this.key = key;
		this.enumClass = enumCls;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	protected Component initContent() {
		Label label = new Label();
		label.setWidth("100%");
		label.setContentMode(ContentMode.TEXT);

		if (org.apache.commons.lang3.StringUtils.isNotBlank(key)) {
			try {
				String value = AppContext.getMessage(enumClass, key);
				label.setValue(value);
			} catch (Exception e) {
				label.setValue("");
			}
		} else {
			label.setValue("");
		}

		return label;
	}
}
