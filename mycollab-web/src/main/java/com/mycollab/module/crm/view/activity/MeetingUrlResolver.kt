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
package com.mycollab.module.crm.view.activity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.MeetingWithBLOBs
import com.mycollab.module.crm.event.ActivityEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MeetingUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("add", MeetingAddUrlResolver())
        this.addSubResolver("edit", MeetingEditUrlResolver())
        this.addSubResolver("preview", MeetingPreviewUrlResolver())
    }

    class MeetingAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ActivityEvent.MeetingAdd(this, MeetingWithBLOBs()))
    }

    class MeetingEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val meetingId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.MeetingEdit(this, meetingId))
        }
    }

    class MeetingPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val meetingId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.MeetingRead(this, meetingId))
        }
    }
}