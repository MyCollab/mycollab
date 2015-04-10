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

import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.RichTextArea;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.3
 *
 */
public class RichTextEditField extends CustomField<String> {
	private static final long serialVersionUID = 1L;

	private RichTextArea textArea = new RichTextArea();

	@Override
	protected Component initContent() {
		textArea.setWidth("100%");
		return textArea;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setPropertyDataSource(Property newDataSource) {
		String value = (String) newDataSource.getValue();
		if (value != null) {
			textArea.setValue(value);
		} else {
			textArea.setValue("");
		}
		super.setPropertyDataSource(newDataSource);
	}

	@Override
	public String getValue() {
		return textArea.getValue();
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		String value = textArea.getValue();
		value = Jsoup.clean(value, Whitelist.relaxed());
		this.setInternalValue(value);
		super.commit();
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}
}
