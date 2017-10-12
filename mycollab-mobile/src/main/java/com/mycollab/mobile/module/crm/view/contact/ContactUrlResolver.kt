/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.contact

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.ContactEvent
import com.mycollab.mobile.module.crm.event.CrmEvent.GotoActivitiesView
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.Contact

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ContactUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ContactListUrlResolver())
        this.addSubResolver("preview", ContactPreviewUrlResolver())
        this.addSubResolver("add", ContactAddUrlResolver())
        this.addSubResolver("edit", ContactEditUrlResolver())
    }

    class ContactListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(GotoActivitiesView(this,
                        CrmModuleScreenData.GotoModule(arrayOf())))
    }

    class ContactAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(ContactEvent.GotoAdd(this, Contact()))
        }
    }

    class ContactEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val contactId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ContactEvent.GotoEdit(this, contactId))
        }
    }

    class ContactPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val contactId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ContactEvent.GotoRead(this, contactId))
        }
    }
}