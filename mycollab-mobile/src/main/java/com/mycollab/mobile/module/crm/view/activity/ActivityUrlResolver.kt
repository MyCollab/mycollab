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
package com.mycollab.mobile.module.crm.view.activity

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ActivityUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ActivityListUrlResolver())
        this.addSubResolver("task", ActivityTaskUrlResolver())
        this.addSubResolver("meeting", MeetingUrlResolver())
        this.addSubResolver("call", CallUrlResolver())
    }

    class ActivityListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this,
                        CrmModuleScreenData.GotoModule(arrayOf())))
    }
}