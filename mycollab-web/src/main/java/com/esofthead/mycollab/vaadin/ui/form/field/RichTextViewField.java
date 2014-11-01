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

import com.esofthead.mycollab.core.utils.StringUtils;
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RichTextViewField extends CustomField {
	private static final long serialVersionUID = 1L;

	private String value;

	public RichTextViewField(String value) {
		this.value = value;
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

	@Override
	protected Component initContent() {
		if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
			Label lbl = new Label("&nbsp;");
			lbl.setContentMode(ContentMode.HTML);
			lbl.setWidth("100%");
			return lbl;
		} else {
			final Label link = new Label(StringUtils.formatRichText(value),
					ContentMode.HTML);
			return link;
		}
	}
}
