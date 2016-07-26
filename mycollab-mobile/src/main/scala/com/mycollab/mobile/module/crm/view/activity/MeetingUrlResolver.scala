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
package com.mycollab.mobile.module.crm.view.activity

import com.mycollab.common.UrlTokenizer
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.mobile.module.crm.CrmUrlResolver
import com.mycollab.mobile.module.crm.events.ActivityEvent
import com.mycollab.module.crm.domain.MeetingWithBLOBs

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class MeetingUrlResolver extends CrmUrlResolver {
  this.addSubResolver("add", new MeetingAddUrlResolver)
  this.addSubResolver("edit", new MeetingEditUrlResolver)
  this.addSubResolver("preview", new MeetingPreviewUrlResolver)
  
  class MeetingAddUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new ActivityEvent.MeetingAdd(this, new MeetingWithBLOBs))
    }
  }
  
  class MeetingEditUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val meetingId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(this, meetingId))
    }
  }
  
  class MeetingPreviewUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val accountId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new ActivityEvent.MeetingRead(this, accountId))
    }
  }
  
}
