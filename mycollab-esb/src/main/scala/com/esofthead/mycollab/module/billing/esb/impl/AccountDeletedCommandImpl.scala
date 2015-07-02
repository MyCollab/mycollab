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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.esofthead.mycollab.common.domain.CustomerFeedbackWithBLOBs
import com.esofthead.mycollab.module.billing.esb.AccountDeletedCommand
import com.esofthead.mycollab.module.ecm.service.ResourceService
import com.esofthead.mycollab.module.page.service.PageService

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@Component object AccountDeletedCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[AccountDeletedCommandImpl])
}

@Component class AccountDeletedCommandImpl extends AccountDeletedCommand {
    @Autowired private val resourceService: ResourceService = null
    @Autowired private val pageService: PageService = null

    def accountDeleted(accountId: Int, feedback: CustomerFeedbackWithBLOBs) {
        deleteAccountFiles(accountId)
    }

    private def deleteAccountFiles(accountId: Int) {
        AccountDeletedCommandImpl.LOG.debug("Delete account files of account {}", accountId)
        val rootPath: String = accountId + ""
        resourceService.removeResource(rootPath, "", accountId)
        pageService.removeResource(rootPath)
    }
}