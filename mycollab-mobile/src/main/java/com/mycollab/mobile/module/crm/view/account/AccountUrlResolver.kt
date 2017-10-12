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
package com.mycollab.mobile.module.crm.view.account

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.AccountEvent
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.Account

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class AccountUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", AccountListUrlResolver())
        this.addSubResolver("preview", AccountPreviewUrlResolver())
        this.addSubResolver("add", AccountAddUrlResolver())
        this.addSubResolver("edit", AccountEditUrlResolver())
    }

    class AccountListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountEvent.GotoList(this, null))
    }

    class AccountAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountEvent.GotoAdd(this, Account()))
    }

    class AccountEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(AccountEvent.GotoEdit(this, accountId))
        }
    }

    class AccountPreviewUrlResolver() : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(AccountEvent.GotoRead(this, accountId))
        }
    }
}