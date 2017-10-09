package com.mycollab.module.user.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.domain.User
import com.mycollab.module.user.domain.criteria.UserSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface UserService : IDefaultService<String, User, UserSearchCriteria> {

    fun authentication(username: String, password: String, subdomain: String, isPasswordEncrypt: Boolean): SimpleUser

    @CacheEvict
    fun saveUserAccount(user: User, roleId: Int?, subDomain: String, @CacheKey sAccountId: Int, inviteUser: String, isSendInvitationEmail: Boolean)

    @CacheEvict
    fun updateUserAccount(user: SimpleUser, @CacheKey sAccountId: Int)

    @CacheEvict
    fun updateUserAccountStatus(username: String, @CacheKey sAccountId: Int, registerStatus: String)

    @CacheEvict
    fun pendingUserAccount(username: String, @CacheKey accountId: Int)

    @CacheEvict
    fun pendingUserAccounts(usernames: List<String>, @CacheKey accountId: Int)

    @Cacheable
    fun findUserByUserNameInAccount(username: String, @CacheKey accountId: Int): SimpleUser?

    fun findUserInAccount(username: String, accountId: Int): SimpleUser?

    @Cacheable
    fun getTotalActiveUsersInAccount(@CacheKey accountId: Int): Int

    fun findUserByUserName(username: String): User?

    fun requestToResetPassword(username: String)
}
