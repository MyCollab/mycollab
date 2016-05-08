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
package com.esofthead.mycollab.mobile.module.crm.view.lead

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.mobile.module.crm.{CrmModuleScreenData, CrmUrlResolver}
import com.esofthead.mycollab.mobile.module.crm.events.{CrmEvent, LeadEvent}
import com.esofthead.mycollab.module.crm.domain.Lead
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum
import com.esofthead.mycollab.vaadin.AppContext

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class LeadUrlResolver extends CrmUrlResolver {
    this.addSubResolver("list", new LeadListUrlResolver)
    this.addSubResolver("preview", new LeadPreviewUrlResolver)
    this.addSubResolver("add", new LeadAddUrlResolver)
    this.addSubResolver("edit", new LeadEditUrlResolver)

    class LeadListUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance().post(new CrmEvent.GotoContainer(this,
                new CrmModuleScreenData.GotoModule(AppContext.getMessage(LeadI18nEnum.LIST))))
        }
    }

    class LeadAddUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, new Lead))
        }
    }

    class LeadEditUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            val leadId = new UrlTokenizer(params(0)).getInt
            EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(this, leadId))
        }
    }

    class LeadPreviewUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            val leadId = new UrlTokenizer(params(0)).getInt
            EventBusFactory.getInstance().post(new LeadEvent.GotoRead(this, leadId))
        }
    }

}
