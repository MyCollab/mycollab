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

    fun authentication(username: String, password: String, subDomain: String, isPasswordEncrypt: Boolean): SimpleUser

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

    fun findUserByUserName(username: String): User?

    fun requestToResetPassword(username: String)
}
