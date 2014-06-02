/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common.ui.components;

import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class TimezoneNotification extends AbstractNotification {

	/**
	 * @param message
	 * @param type
	 */
	public TimezoneNotification() {
		super("You haven't chosen preferred timezone. Please set it ",
				AbstractNotification.WARNING);
	}

	@Override
	public String renderContent() {
		Span spanEl = new Span();
		spanEl.appendText(getMessage());

		A link = new A(AccountLinkUtils.generatePreviewFullUserLink(
				AppContext.getSiteUrl(), AppContext.getSession().getUsername()));
		link.appendText("here");
		spanEl.appendChild(link);
		return spanEl.write();
	}

}
