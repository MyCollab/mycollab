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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.service.mybatis

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.configuration.EnDecryptHelper
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.utils.StringUtils
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.billing.RegisterStatusConstants
import com.mycollab.module.billing.UserStatusConstants
import com.mycollab.module.billing.esb.AccountCreatedEvent
import com.mycollab.module.user.dao.BillingAccountMapper
import com.mycollab.module.user.dao.BillingAccountMapperExt
import com.mycollab.module.user.dao.UserAccountMapper
import com.mycollab.module.user.dao.UserMapper
import com.mycollab.module.user.domain.*
import com.mycollab.module.user.esb.SendUserEmailVerifyRequestEvent
import com.mycollab.module.user.service.BillingAccountService
import com.mycollab.module.user.service.RoleService
import com.mycollab.security.PermissionMap
import org.apache.commons.collections.CollectionUtils
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class BillingAccountServiceImpl(private val billingAccountMapper: BillingAccountMapper,
                                private val billingAccountMapperExt: BillingAccountMapperExt,
                                private val asyncEventBus: AsyncEventBus,
                                private val userMapper: UserMapper,
                                private val userAccountMapper: UserAccountMapper,
                                private val roleService: RoleService,
                                private val deploymentMode: IDeploymentMode) : DefaultCrudService<Int, BillingAccount>(), BillingAccountService {

    override val crudMapper: ICrudGenericDAO<Int, BillingAccount>
        get() = billingAccountMapper as ICrudGenericDAO<Int, BillingAccount>

    override fun getBillingAccountById(accountId: Int): SimpleBillingAccount? =
            billingAccountMapperExt.getBillingAccountById(accountId)

    override fun updateSelectiveWithSession(record: BillingAccount, username: String?): Int? {
        try {
            return super.updateSelectiveWithSession(record, username)
        } catch (e: DuplicateKeyException) {
            throw UserInvalidInputException("The domain ${record.subdomain} is already used")
        }

    }

    override fun getAccountByDomain(domain: String): SimpleBillingAccount? =
            if (deploymentMode.isDemandEdition) {
                billingAccountMapperExt.getAccountByDomain(domain)
            } else {
                billingAccountMapperExt.defaultAccountByDomain
            }

    override fun getAccountById(accountId: Int): BillingAccount? {
        val ex = BillingAccountExample()

        if (deploymentMode.isDemandEdition) {
            ex.createCriteria().andIdEqualTo(accountId)
        }

        val accounts = billingAccountMapper.selectByExample(ex)
        return if (accounts.isEmpty()) {
            null
        } else {
            accounts[0]
        }
    }

    override fun createDefaultAccountData(username: String, password: String, timezoneId: String, language: String, isEmailVerified: Boolean?, isCreatedDefaultData: Boolean?, sAccountId: Int) {
        // Check whether user has registered to the system before
        val encryptedPassword = EnDecryptHelper.encryptSaltPassword(password)
        val ex = UserExample()
        ex.createCriteria().andUsernameEqualTo(username)
        val users = userMapper.selectByExample(ex)

        val now = GregorianCalendar().time

        if (CollectionUtils.isNotEmpty(users)) {
            for (tmpUser in users) {
                if (encryptedPassword != tmpUser.password) {
                    throw UserInvalidInputException("There is already user " + username
                            + " in the MyCollab database. If it is yours, you must enter the same password you registered to MyCollab. Otherwise " +
                            "you must use the different email.")
                }
            }
        } else {
            // Register the new user to this account
            val user = User()
            user.email = username
            user.password = encryptedPassword
            user.timezone = timezoneId
            user.username = username
            user.registeredtime = now
            user.lastaccessedtime = now
            user.language = language

            if (isEmailVerified!!) {
                user.status = UserStatusConstants.EMAIL_VERIFIED
            } else {
                user.status = UserStatusConstants.EMAIL_NOT_VERIFIED
            }

            if (user.firstname == null) {
                user.firstname = ""
            }

            if (StringUtils.isBlank(user.lastname)) {
                user.lastname = StringUtils.extractNameFromEmail(username)
            }
            userMapper.insert(user)
            if (!isEmailVerified) {
                asyncEventBus.post(SendUserEmailVerifyRequestEvent(user))
            }
        }

        // save default roles
        saveEmployeeRole(sAccountId)
        val adminRoleId = saveAdminRole(sAccountId)
        saveGuestRole(sAccountId)

        // save user account
        val userAccount = UserAccount()
        userAccount.accountid = sAccountId
        userAccount.isaccountowner = true
        userAccount.registeredtime = now
        userAccount.registerstatus = RegisterStatusConstants.ACTIVE
        userAccount.registrationsource = "Web"
        userAccount.roleid = adminRoleId
        userAccount.username = username

        userAccountMapper.insert(userAccount)
        asyncEventBus.post(AccountCreatedEvent(sAccountId, username, isCreatedDefaultData!!))
    }

    private fun saveEmployeeRole(accountId: Int): Int {
        // Register default role for account
        val role = Role()
        role.rolename = SimpleRole.EMPLOYEE
        role.description = ""
        role.saccountid = accountId
        role.issystemrole = true
        role.isdefault = java.lang.Boolean.FALSE
        val roleId = roleService.saveWithSession(role, "")
        roleService.savePermission(roleId, PermissionMap.buildEmployeePermissionCollection(), accountId)
        return roleId
    }

    private fun saveAdminRole(accountId: Int): Int {
        // Register default role for account
        val role = Role()
        role.rolename = SimpleRole.ADMIN
        role.description = ""
        role.saccountid = accountId
        role.issystemrole = true
        role.isdefault = java.lang.Boolean.FALSE
        val roleId = roleService.saveWithSession(role, "")
        roleService.savePermission(roleId, PermissionMap.buildAdminPermissionCollection(), accountId)
        return roleId
    }

    private fun saveGuestRole(accountId: Int?): Int {
        // Register default role for account
        val role = Role()
        role.rolename = SimpleRole.GUEST
        role.description = ""
        role.saccountid = accountId
        role.issystemrole = true
        role.isdefault = java.lang.Boolean.TRUE
        val roleId = roleService.saveWithSession(role, "")
        roleService.savePermission(roleId, PermissionMap.buildGuestPermissionCollection(), accountId!!)
        return roleId
    }
}
