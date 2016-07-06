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
package com.mycollab.mobile.module.crm.view.contact

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.mobile.module.crm.events.CrmEvent
import com.mycollab.mobile.module.crm.CrmUrlResolver
import com.mycollab.vaadin.AppContext
import com.mycollab.common.UrlTokenizer
import com.mycollab.mobile.module.crm.CrmModuleScreenData
import com.mycollab.mobile.module.crm.events.{ContactEvent, CrmEvent}
import com.mycollab.module.crm.domain.Contact
import com.mycollab.module.crm.i18n.ContactI18nEnum

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class ContactUrlResolver extends CrmUrlResolver {
    this.addSubResolver("list", new ContactListUrlResolver)
    this.addSubResolver("preview", new ContactPreviewUrlResolver)
    this.addSubResolver("add", new ContactAddUrlResolver)
    this.addSubResolver("edit", new ContactEditUrlResolver)

    class ContactListUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance().post(new CrmEvent.GotoContainer(this,
                new CrmModuleScreenData.GotoModule(AppContext.getMessage(ContactI18nEnum.LIST))))
        }
    }

    class ContactAddUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, new Contact))
        }
    }

    class ContactEditUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            val contactId = new UrlTokenizer(params(0)).getInt
            EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(this, contactId))
        }
    }

    class ContactPreviewUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            val contactId = new UrlTokenizer(params(0)).getInt
            EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, contactId))
        }
    }

}
