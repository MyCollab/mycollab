/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.opportunity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.event.OpportunityEvent
import com.mycollab.module.crm.view.CrmUrlResolver

class OpportunityUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", OpportunityListUrlResolver())
        this.addSubResolver("add", OpportunityAddUrlResolver())
        this.addSubResolver("edit", OpportunityEditUrlResolver())
        this.addSubResolver("preview", OpportunityPreviewUrlResolver())
    }

    class OpportunityListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(OpportunityEvent.GotoList(this, null))

    }

    class OpportunityAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(OpportunityEvent.GotoAdd(this, Account()))
    }

    class OpportunityEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val opportunityId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(OpportunityEvent.GotoEdit(this, opportunityId))
        }
    }

    class OpportunityPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val opportunityId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(OpportunityEvent.GotoRead(this, opportunityId))
        }
    }
}