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

import com.esofthead.mycollab.vaadin.ui.EmailLink;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.3
 *
 */
public class EmailViewField extends CustomField<String> {
	private static final long serialVersionUID = 1L;

	private String email;

	public EmailViewField(String email) {
		this.email = email;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	protected Component initContent() {
		EmailLink emailLink = new EmailLink(email);
		emailLink.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
		return emailLink;
	}
}
