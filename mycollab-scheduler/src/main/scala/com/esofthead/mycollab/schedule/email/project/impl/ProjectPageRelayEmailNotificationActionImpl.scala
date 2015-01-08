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
package com.esofthead.mycollab.schedule.email.project.impl

import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.module.page.domain.Page
import com.esofthead.mycollab.module.page.service.PageService
import com.esofthead.mycollab.schedule.email.project.ProjectPageRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ProjectPageRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[Page] with ProjectPageRelayEmailNotificationAction {

  @Autowired var wikiService: PageService = _

  protected def getBeanInContext(context: MailContext[Page]): Page = null

  protected def buildExtraTemplateVariables(context: MailContext[Page]) {}

  protected def getItemName: String = StringUtils.trim(bean.getSubject, 100)

  protected def getCreateSubject(context: MailContext[Page]): String = null

  protected def getUpdateSubject(context: MailContext[Page]): String = null

  protected def getCommentSubject(context: MailContext[Page]): String = null

  protected def getItemFieldMapper: ItemFieldMapper = null
}
