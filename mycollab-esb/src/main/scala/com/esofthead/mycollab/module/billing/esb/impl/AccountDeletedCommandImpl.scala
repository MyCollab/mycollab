/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.billing.esb.impl

import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.billing.esb.DeleteAccountEvent
import com.esofthead.mycollab.module.ecm.service.ResourceService
import com.esofthead.mycollab.module.page.service.PageService
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@Component class AccountDeletedCommandImpl extends GenericCommand {
    @Autowired private val resourceService: ResourceService = null
    @Autowired private val pageService: PageService = null

    @AllowConcurrentEvents
    @Subscribe
    def deleteAccount(event: DeleteAccountEvent): Unit = {
        val rootPath: String = event.accountId + ""
        resourceService.removeResource(rootPath, "", event.accountId)
        pageService.removeResource(rootPath)

        val feedback = event.feedback

    }
}