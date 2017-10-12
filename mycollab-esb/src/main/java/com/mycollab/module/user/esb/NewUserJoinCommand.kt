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
package com.mycollab.module.user.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.hp.gagawa.java.elements.A
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.db.arguments.*
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.billing.RegisterStatusConstants
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.mycollab.module.user.service.BillingAccountService
import com.mycollab.module.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class NewUserJoinCommand(private val billingAccountService: BillingAccountService,
                         private val extMailService: ExtMailService,
                         private val contentGenerator: IContentGenerator,
                         private val userService: UserService,
                         private val deploymentMode: IDeploymentMode) : GenericCommand() {

    companion object {
        val LOG = LoggerFactory.getLogger(NewUserJoinCommand::class.java)

        class Formatter {
            fun formatMemberLink(siteUrl: String, newMember: SimpleUser): String {
                return A(AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, newMember.username)).
                        appendText(newMember.displayName).write()
            }

            fun formatRoleName(siteUrl: String, newMember: SimpleUser): String {
                return if (newMember.isAccountOwner == true) "Account Owner"
                else A(AccountLinkGenerator.generatePreviewFullRoleLink(siteUrl, newMember.roleid)).appendText(newMember.roleName).write()
            }
        }
    }

    @AllowConcurrentEvents
    @Subscribe
    fun execute(event: NewUserJoinEvent) {
        val username = event.username
        val sAccountId = event.sAccountId
        val searchCriteria = UserSearchCriteria()
        searchCriteria.saccountid = NumberSearchField(sAccountId)
        searchCriteria.registerStatuses = SetSearchField<String>(RegisterStatusConstants.ACTIVE)
        searchCriteria.addExtraField(OneValueSearchField(SearchField.AND, "s_user_account.isAccountOwner = ", 1))

        val accountOwners = userService.findPageableListByCriteria(BasicSearchRequest<UserSearchCriteria>(searchCriteria))
        val newUser = userService.findUserInAccount(username, sAccountId)
        if (newUser != null) {
            val recipients = accountOwners
                    .map { it as SimpleUser }
                    .map { MailRecipientField(it.username, it.displayName) }

            val account = billingAccountService.getAccountById(sAccountId)
            contentGenerator.putVariable("siteUrl", deploymentMode.getSiteUrl(account.subdomain))
            contentGenerator.putVariable("newUser", newUser)
            contentGenerator.putVariable("formatter", Formatter())
            contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(Locale.US, MailI18nEnum.Copyright,
                    DateTimeUtils.getCurrentYear()))
            contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(account.id, account.logopath))
            extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), recipients,
                    "${newUser.displayName} has just joined on MyCollab workspace",
                    contentGenerator.parseFile("mailNewUserJoinAccountNotifier.ftl", Locale.US))
        } else {
            LOG.error("Can not find the user $username in account $sAccountId")
        }
    }
}