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
import com.mycollab.module.crm.domain.CrmTask
import com.mycollab.module.crm.event.ActivityEvent

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class ActivityTaskUrlResolver extends CrmUrlResolver {
  this.addSubResolver("add", new TaskAddUrlResolver)
  this.addSubResolver("edit", new TaskEditUrlResolver)
  this.addSubResolver("preview", new TaskPreviewUrlResolver)

  class TaskAddUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, new CrmTask))
    }
  }

  class TaskEditUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val meetingId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(this, meetingId))
    }
  }

  class TaskPreviewUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val accountId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new ActivityEvent.TaskRead(this, accountId))
    }
  }

}
