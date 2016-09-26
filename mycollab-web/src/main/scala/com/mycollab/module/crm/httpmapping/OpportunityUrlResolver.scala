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
package com.mycollab.module.crm.httpmapping

import com.mycollab.common.UrlTokenizer
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.event.OpportunityEvent

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class OpportunityUrlResolver extends CrmUrlResolver {
  this.addSubResolver("list", new OpportunityListUrlResolver)
  this.addSubResolver("add", new OpportunityAddUrlResolver)
  this.addSubResolver("edit", new OpportunityEditUrlResolver)
  this.addSubResolver("preview", new OpportunityPreviewUrlResolver)

  class OpportunityListUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null))
    }
  }

  class OpportunityAddUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(this, new Account))
    }
  }

  class OpportunityEditUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val opportunityId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(this, opportunityId))
    }
  }

  class OpportunityPreviewUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val opportunityId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, opportunityId))
    }
  }

}
