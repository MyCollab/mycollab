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

import java.util.Arrays

import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.common.{GenericLinkUtils, UrlEncodeDecoder}
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.core.arguments.{SearchRequest, SetSearchField}
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.billing.UserStatusConstants
import com.esofthead.mycollab.module.mail.service.{IContentGenerator, ExtMailService}
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.jobs.GenericQuartzJobBean
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
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class UserSignUpEmailNotificationJob extends GenericQuartzJobBean {
    @Autowired var userService: UserService = _
    @Autowired var extMailService: ExtMailService = _
    @Autowired var contentGenerator: IContentGenerator = _
    private val CONFIRM_EMAIL_TEMPLATE: String = "templates/email/billing/confirmUserSignUpNotification.mt"

    @SuppressWarnings(Array("unchecked"))
    @throws(classOf[JobExecutionException])
    def executeJob(context: JobExecutionContext) {
        val criteria = new UserSearchCriteria
        val statusField = new SetSearchField[String](UserStatusConstants.EMAIL_NOT_VERIFIED)
        criteria.setStatuses(statusField)
        criteria.setSaccountid(null)

        import scala.collection.JavaConverters._
        val users: List[SimpleUser] = userService.findPagableListByCriteria(new SearchRequest[UserSearchCriteria](criteria,
            0, Integer.MAX_VALUE)).asScala.toList.asInstanceOf[List[SimpleUser]]
        if (users != null && users.nonEmpty) {
            for (user <- users) {
                sendConfirmEmailToUser(user)
                user.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST)
                userService.updateWithSession(user, user.getUsername)
            }
        }
    }

    def sendConfirmEmailToUser(user: SimpleUser) {
        contentGenerator.putVariable("user", user)
        val siteUrl = GenericLinkUtils.generateSiteUrlByAccountId(user.getAccountId)
        contentGenerator.putVariable("siteUrl", siteUrl)
        val confirmLink = siteUrl + "user/confirm_signup/" + UrlEncodeDecoder.encode(user.getUsername + "/" + user.getAccountId)
        contentGenerator.putVariable("linkConfirm", confirmLink)
        extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getDefaultSiteName,
            Arrays.asList(new MailRecipientField(user.getEmail, user.getDisplayName)), null, null,
            contentGenerator.parseString(LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale,
                UserI18nEnum.MAIL_CONFIRM_PASSWORD_SUBJECT)),
            contentGenerator.parseFile(CONFIRM_EMAIL_TEMPLATE, SiteConfiguration.getDefaultLocale), null)
    }
}
