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
package com.mycollab.module.user.service.mybatis

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.configuration.EnDecryptHelper
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.UserInvalidInputException
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.billing.RegisterStatusConstants
import com.mycollab.module.billing.service.BillingPlanCheckerService
import com.mycollab.module.file.service.UserAvatarService
import com.mycollab.module.user.dao.RolePermissionMapper
import com.mycollab.module.user.dao.UserAccountMapper
import com.mycollab.module.user.dao.UserMapper
import com.mycollab.module.user.dao.UserMapperExt
import com.mycollab.module.user.domain.*
import com.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.mycollab.module.user.esb.DeleteUserEvent
import com.mycollab.module.user.esb.NewUserJoinEvent
import com.mycollab.module.user.esb.RequestToResetPasswordEvent
import com.mycollab.module.user.esb.SendUserInvitationEvent
import com.mycollab.module.user.service.UserService
import com.mycollab.security.PermissionMap
import org.apache.commons.collections.CollectionUtils
import org.apache.ibatis.session.RowBounds
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
class UserServiceDBImpl(private val userMapper: UserMapper,
                        private val userMapperExt: UserMapperExt,
                        private val userAccountMapper: UserAccountMapper,
                        private val rolePermissionMapper: RolePermissionMapper,
                        private val userAvatarService: UserAvatarService,
                        private val billingPlanCheckerService: BillingPlanCheckerService,
                        private val asyncEventBus: AsyncEventBus,
                        private val deploymentMode: IDeploymentMode) : DefaultService<String, User, UserSearchCriteria>(), UserService {

    override val crudMapper: ICrudGenericDAO<String, User>
        get() = userMapper as ICrudGenericDAO<String, User>

    override val searchMapper: ISearchableDAO<UserSearchCriteria>
        get() = userMapperExt

    override fun saveUserAccount(record: User, roleId: Int?, subDomain: String, sAccountId: Int, inviteUser: String, isSendInvitationEmail: Boolean) {
        billingPlanCheckerService.validateAccountCanCreateNewUser(sAccountId)

        // check if user email has already in this account yet
        var userAccountEx = UserAccountExample()

        if (deploymentMode.isDemandEdition) {
            userAccountEx.createCriteria().andUsernameEqualTo(record.email).andAccountidEqualTo(sAccountId)
                    .andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE)
        } else {
            userAccountEx.createCriteria().andUsernameEqualTo(record.email).andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE)
        }

        if (userAccountMapper.countByExample(userAccountEx) > 0) {
            throw UserInvalidInputException(String.format("There is already user has email %s in your account", record.email))
        }

        val password = record.password
        if (password != null) {
            record.password = EnDecryptHelper.encryptSaltPassword(password)
        }

        if (record.username == null) {
            record.username = record.email
        }

        if (record.lastname == null) {
            val userEmail = record.email
            val index = userEmail.lastIndexOf("@")
            if (index > 0) {
                record.lastname = userEmail.substring(0, index)
            } else {
                record.lastname = userEmail
            }
        }

        if (record.firstname == null) {
            record.firstname = ""
        }

        // Check if user has already account in system, if not we will create new user
        val userEx = UserExample()
        userEx.createCriteria().andUsernameEqualTo(record.username)
        if (userMapper.countByExample(userEx) == 0L) {
            userMapper.insert(record)
            userAvatarService.uploadDefaultAvatar(record.username)
        }

        // save record in s_user_account table
        val userAccount = UserAccount()
        userAccount.accountid = sAccountId

        if (roleId != null && roleId > 0) {
            userAccount.roleid = roleId
            userAccount.isaccountowner = false
        } else {
            userAccount.roleid = null
            userAccount.isaccountowner = true
        }

        userAccount.username = record.username
        userAccount.registeredtime = GregorianCalendar().time
        userAccount.lastaccessedtime = GregorianCalendar().time
        userAccount.registerstatus = RegisterStatusConstants.NOT_LOG_IN_YET
        userAccount.inviteuser = inviteUser

        userAccountEx = UserAccountExample()
        if (deploymentMode.isDemandEdition) {
            userAccountEx.createCriteria().andUsernameEqualTo(record.email).andAccountidEqualTo(sAccountId)
        } else {
            userAccountEx.createCriteria().andUsernameEqualTo(record.email)
        }

        when {
            userAccountMapper.countByExample(userAccountEx) > 0 -> userAccountMapper.updateByExampleSelective(userAccount, userAccountEx)
            else -> userAccountMapper.insert(userAccount)
        }

        if (isSendInvitationEmail) {
            val invitationEvent = SendUserInvitationEvent(record.username, password,
                    inviteUser, subDomain, sAccountId)
            asyncEventBus.post(invitationEvent)
        }
    }

    override fun updateWithSession(record: User, username: String?): Int {
        LOG.debug("Check whether there is exist email in system before")
        if (record.email != null && record.username != record.email) {
            val ex = UserExample()
            ex.createCriteria().andUsernameEqualTo(record.email)
            val numUsers = userMapper.countByExample(ex)
            if (numUsers > 0) {
                throw UserInvalidInputException("Email ${record.email} is already existed in system. Please choose another email.")
            }
        }

        // now we keep username similar than email
        val ex = UserExample()
        ex.createCriteria().andUsernameEqualTo(record.username)
        record.username = record.email
        return userMapper.updateByExampleSelective(record, ex)
    }

    override fun updateUserAccount(record: SimpleUser, sAccountId: Int) {
        val oldUser = findUserByUserNameInAccount(record.username, sAccountId)
        if (oldUser != null) {
            if (java.lang.Boolean.TRUE == oldUser.isAccountOwner && java.lang.Boolean.FALSE == record.isAccountOwner) {
                val userAccountEx = UserAccountExample()
                userAccountEx.createCriteria().andAccountidEqualTo(sAccountId).andIsaccountownerEqualTo(java.lang.Boolean.TRUE)
                        .andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE)
                if (userAccountMapper.countByExample(userAccountEx) <= 1) {
                    throw UserInvalidInputException("Can not change role of user ${record.username}. The reason is ${record.username} is the unique account owner of the current account.")
                }
            }
        }

        if (record.username != record.email) {
            val ex = UserExample()
            ex.createCriteria().andUsernameEqualTo(record.email)
            val numUsers = userMapper.countByExample(ex)
            if (numUsers > 0) {
                throw UserInvalidInputException("Email %s is already existed in system. Please choose another email ${record.email}")
            }
        }

        // now we keep username similar than email
        val ex = UserExample()
        ex.createCriteria().andUsernameEqualTo(record.username)
        record.username = record.email
        userMapper.updateByExampleSelective(record, ex)

        val userAccountEx = UserAccountExample()
        userAccountEx.createCriteria().andUsernameEqualTo(record.username).andAccountidEqualTo(sAccountId)
        val userAccounts = userAccountMapper.selectByExample(userAccountEx)
        if (userAccounts.size > 0) {
            val userAccount = userAccounts[0]
            if (record.roleid == -1) {
                userAccount.roleid = null
                userAccount.isaccountowner = true
            } else {
                userAccount.roleid = record.roleid
                userAccount.isaccountowner = false
            }

            userAccount.registerstatus = record.registerstatus
            userAccount.lastaccessedtime = GregorianCalendar().time
            userAccountMapper.updateByPrimaryKey(userAccount)
        }
    }

    override fun massRemoveWithSession(items: List<User>, username: String?, sAccountId: Int) {
        val keys = items.map { it.username }
        userMapperExt.removeKeysWithSession(keys)
    }

    override fun authentication(username: String, password: String, subDomain: String, isPasswordEncrypt: Boolean): SimpleUser {
        val criteria = UserSearchCriteria()
        criteria.username = StringSearchField.and(username)
        criteria.registerStatuses = SetSearchField(RegisterStatusConstants.ACTIVE, RegisterStatusConstants.NOT_LOG_IN_YET)
        criteria.saccountid = null

        if (deploymentMode.isDemandEdition) {
            criteria.subdomain = StringSearchField.and(subDomain)
        }

        val users = findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleUser>
        if (users.isEmpty()) {
            throw UserInvalidInputException("User $username is not existed in this domain $subDomain")
        } else {
            var user: SimpleUser? = null
            if (deploymentMode.isDemandEdition) {
                for (testUser in users) {
                    if (subDomain == testUser.subdomain) {
                        user = testUser
                        break
                    }
                }
                if (user == null) {
                    throw UserInvalidInputException("Invalid username or password")
                }
            } else {
                user = users[0]
            }

            if (user.password == null || !EnDecryptHelper.checkPassword(password, user.password, isPasswordEncrypt)) {
                throw UserInvalidInputException("Invalid username or password")
            }

            if (RegisterStatusConstants.NOT_LOG_IN_YET == user.registerstatus) {
                updateUserAccountStatus(user.username, user.accountId!!, RegisterStatusConstants.ACTIVE)
                asyncEventBus.post(NewUserJoinEvent(user.username, user.accountId!!))
            }
            LOG.debug("User $username login to system successfully!")

            if (user.isAccountOwner == null || (user.isAccountOwner != null && !user.isAccountOwner!!)) {
                if (user.roleid != null) {
                    val ex = RolePermissionExample()
                    ex.createCriteria().andRoleidEqualTo(user.roleid)
                    val roles = rolePermissionMapper.selectByExampleWithBLOBs(ex)
                    if (CollectionUtils.isNotEmpty(roles)) {
                        val rolePer = roles[0] as RolePermission
                        val permissionMap = PermissionMap.fromJsonString(rolePer.roleval)
                        user.permissionMaps = permissionMap
                        LOG.debug("Find role match to user $username")
                    } else {
                        LOG.debug("We can not find any role associate to user $username")
                    }
                } else {
                    LOG.debug("User %s has no any role $username")
                }
            }
            user.password = null
            return user
        }
    }

    override fun findUserByUserNameInAccount(username: String, accountId: Int): SimpleUser? =
            findUserInAccount(username, accountId)

    override fun findUserInAccount(username: String, accountId: Int): SimpleUser? {
        val criteria = UserSearchCriteria()
        criteria.username = StringSearchField.and(username)
        criteria.saccountid = NumberSearchField(accountId)

        val users = userMapperExt.findPageableListByCriteria(criteria, RowBounds(0, 1)) as List<SimpleUser>
        return if (CollectionUtils.isEmpty(users)) null else users[0]
    }

    override fun pendingUserAccount(username: String, accountId: Int) {
        pendingUserAccounts(listOf(username), accountId)
    }

    override fun pendingUserAccounts(usernames: List<String>, accountId: Int) {
        // check if current user is the unique account owner, then reject deletion
        var userAccountEx = UserAccountExample()
        userAccountEx.createCriteria().andUsernameNotIn(usernames).andAccountidEqualTo(accountId)
                .andIsaccountownerEqualTo(true).andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE)
        if (userAccountMapper.countByExample(userAccountEx) == 0L) {
            throw UserInvalidInputException("Can not delete users. The reason is there is no account owner in the rest users")
        }

        userAccountEx = UserAccountExample()
        userAccountEx.createCriteria().andUsernameIn(usernames).andAccountidEqualTo(accountId)
        val userAccount = UserAccount()
        userAccount.registerstatus = RegisterStatusConstants.DELETE
        userAccountMapper.updateByExampleSelective(userAccount, userAccountEx)

        // notify users are "deleted"
        for (username in usernames) {
            val event = DeleteUserEvent(username, accountId)
            asyncEventBus.post(event)
        }
    }

    override fun findUserByUserName(username: String): User? {
        val ex = UserExample()
        ex.createCriteria().andUsernameEqualTo(username)
        val users = userMapper.selectByExample(ex)
        return if (CollectionUtils.isEmpty(users)) null else users[0]
    }

    override fun updateUserAccountStatus(username: String, sAccountId: Int, registerStatus: String) {
        // Update status of user account
        val userAccount = UserAccount()
        userAccount.accountid = sAccountId
        userAccount.username = username
        userAccount.registerstatus = registerStatus

        val ex = UserAccountExample()
        ex.createCriteria().andAccountidEqualTo(sAccountId).andUsernameEqualTo(username)
        userAccountMapper.updateByExampleSelective(userAccount, ex)
    }

    override fun requestToResetPassword(username: String) {
        asyncEventBus.post(RequestToResetPasswordEvent(username))
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserServiceDBImpl::class.java)
    }
}
