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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.vaadin.ui.CssLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class EmailTokenField extends CssLayout {
	private final List<MailRecipientField> lstMailToken = new ArrayList<>();

	public EmailTokenField() {
		super();
	}

	private int getItemIndexInListToEmail(MailRecipientField item) {
		for (int i = 0; i < lstMailToken.size(); i++) {
			MailRecipientField recipient = lstMailToken.get(i);
			if (item.getEmail().equals(recipient.getEmail())
					&& item.getName().equals(recipient.getName())) {
				return i;
			}
		}
		return -1;
	}



	public List<MailRecipientField> getListRecipient() {
		return lstMailToken;
	}

}
