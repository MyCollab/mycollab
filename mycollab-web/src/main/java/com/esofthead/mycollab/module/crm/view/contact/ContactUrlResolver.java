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
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.view.CrmUrlResolver;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class ContactUrlResolver extends CrmUrlResolver {
	public ContactUrlResolver() {
		this.addSubResolver("list", new ContactListUrlResolver());
		this.addSubResolver("preview", new ContactPreviewUrlResolver());
		this.addSubResolver("add", new ContactAddUrlResolver());
		this.addSubResolver("edit", new ContactEditUrlResolver());
	}

	public static class ContactListUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ContactEvent.GotoList(this, null));
		}
	}

	public static class ContactAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ContactEvent.GotoAdd(this, new Contact()));
		}
	}

	public static class ContactEditUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int contactId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ContactEvent.GotoEdit(this, contactId));
		}
	}

	public static class ContactPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int contactId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ContactEvent.GotoRead(this, contactId));
		}
	}

}
