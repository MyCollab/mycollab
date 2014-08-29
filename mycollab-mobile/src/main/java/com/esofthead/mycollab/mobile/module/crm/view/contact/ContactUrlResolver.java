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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.CrmModuleScreenData;
import com.esofthead.mycollab.mobile.module.crm.CrmUrlResolver;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
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
			EventBusFactory
					.getInstance()
					.post(new CrmEvent.GotoContainer(
							this,
							new CrmModuleScreenData.GotoModule(
									AppContext
											.getMessage(CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER))));
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
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int contactId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new ContactEvent.GotoEdit(this, contactId));
		}
	}

	public static class ContactPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int contactId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new ContactEvent.GotoRead(this, contactId));
		}
	}

}
