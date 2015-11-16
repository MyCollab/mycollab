/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email.user.service

import com.esofthead.mycollab.core.arguments.{SearchRequest, SetSearchField}
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.esofthead.mycollab.module.user.esb.SendUserInvitationEvent
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.jobs.GenericQuartzJobBean
import com.google.common.eventbus.AsyncEventBus
import org.quartz.{JobExecutionContext, JobExecutionException}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE) class SendUserInvitationEmailJob extends GenericQuartzJobBean {
    @Autowired var userService: UserService = _
    @Autowired var contentGenerator: IContentGenerator = _
    @Autowired var extMailService: ExtMailService = _
    @Autowired var asyncEventBus: AsyncEventBus = _

    @throws(classOf[JobExecutionException])
    def executeJob(context: JobExecutionContext) {
        val searchCriteria = new UserSearchCriteria()
        searchCriteria.setRegisterStatuses(new SetSearchField[String](RegisterStatusConstants.VERIFICATING))
        searchCriteria.setSaccountid(null)
        import scala.collection.JavaConverters._
        val inviteUsers: List[Any] = userService.findPagableListByCriteria(new
                SearchRequest[UserSearchCriteria](searchCriteria, 0, Integer.MAX_VALUE)).asScala.toList
        for (item <- inviteUsers) {
            val invitedUser: SimpleUser = item.asInstanceOf[SimpleUser]
            val inviteUserEvent = new SendUserInvitationEvent(invitedUser.getUsername, invitedUser
                .getInviteUserFullName, invitedUser.getSubdomain, invitedUser.getAccountId)
            asyncEventBus.post(inviteUserEvent)
        }
    }
}
