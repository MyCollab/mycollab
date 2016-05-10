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
package com.esofthead.mycollab.module.user.esb.impl

import java.util.Locale

import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.core.arguments._
import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.esofthead.mycollab.module.user.esb.NewUserJoinEvent
import com.esofthead.mycollab.module.user.esb.impl.NewUserJoinCommand.Formatter
import com.esofthead.mycollab.module.user.service.{BillingAccountService, UserService}
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.hp.gagawa.java.elements.A
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.mutable.ListBuffer

/**
  * @author MyCollab Ltd
  * @since 5.2.6
  */
object NewUserJoinCommand {

  class Formatter {
    def formatMemberLink(siteUrl: String, newMember: SimpleUser): String = {
      new A(AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, newMember.getUsername)).
        appendText(newMember.getDisplayName).write()
    }

    def formatRoleName(siteUrl: String, newMember: SimpleUser): String = {
      if (newMember.getIsAccountOwner == true) "Account Owner"
      else new A(AccountLinkGenerator.generatePreviewFullRoleLink(siteUrl, newMember.getRoleid)).appendText(newMember.getRoleName).write()
    }
  }

}

@Component class NewUserJoinCommand extends GenericCommand {
  @Autowired private val billingAccountService: BillingAccountService = null
  @Autowired private val extMailService: ExtMailService = null
  @Autowired private val contentGenerator: IContentGenerator = null
  @Autowired private val userService: UserService = null

  @AllowConcurrentEvents
  @Subscribe
  def execute(event: NewUserJoinEvent): Unit = {
    val username = event.username
    val sAccountId = event.sAccountId
    val searchCriteria = new UserSearchCriteria
    searchCriteria.setSaccountid(new NumberSearchField(sAccountId))
    searchCriteria.setRegisterStatuses(new SetSearchField[String](RegisterStatusConstants.ACTIVE))
    searchCriteria.addExtraField(new OneValueSearchField(SearchField.AND, "s_user_account.isAccountOwner = ", 1))
    import scala.collection.JavaConverters._
    val accountOwners = userService.findPagableListByCriteria(new BasicSearchRequest[UserSearchCriteria](searchCriteria, 0,
      Integer.MAX_VALUE)).asScala.toList
    val newUser = userService.findUserByUserNameInAccount(username, sAccountId)
    val recipients = ListBuffer[MailRecipientField]()
    accountOwners.foreach(user => {
      val inst = user.asInstanceOf[SimpleUser]
      recipients.append(new MailRecipientField(inst.getUsername, inst.getDisplayName))
    })
    val account = billingAccountService.getAccountById(sAccountId)
    contentGenerator.putVariable("siteUrl", SiteConfiguration.getSiteUrl(account.getSubdomain))
    contentGenerator.putVariable("newUser", newUser)
    contentGenerator.putVariable("formatter", new Formatter)
    extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getDefaultSiteName, recipients.asJava,
      null, null, String.format("%s has just joined on MyCollab workspace", newUser.getDisplayName),
      contentGenerator.parseFile("templates/email/user/newUserJoinAccountNotifier.mt", Locale.US), null)
  }
}
