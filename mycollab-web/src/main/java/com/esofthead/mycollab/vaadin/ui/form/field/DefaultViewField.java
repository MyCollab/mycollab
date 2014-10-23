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

import org.apache.commons.lang3.StringUtils;

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
public class DefaultViewField extends CustomField<String> {

	private String value;
	private ContentMode contentMode;

	private static final long serialVersionUID = 1L;

	public DefaultViewField(final String value) {
		this(value, ContentMode.TEXT);
	}

	public DefaultViewField(final String value, final ContentMode contentMode) {
		this.value = value;
		this.contentMode = contentMode;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	protected Component initContent() {
		final Label label = new Label();
		label.setWidth("100%");
		label.setContentMode(contentMode);

		if (StringUtils.isNotBlank(value)) {
			label.setValue(value);
		} else {
			label.setValue("");
		}

		return label;
	}
}
