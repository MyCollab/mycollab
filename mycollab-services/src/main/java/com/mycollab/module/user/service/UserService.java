/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.service;

import com.mycollab.cache.IgnoreCacheClass;
import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
public interface UserService extends IDefaultService<String, User, UserSearchCriteria> {

    SimpleUser authentication(String username, String password, String subdomain, boolean isPasswordEncrypt);

    @CacheEvict
    void saveUserAccount(User user, Integer roleId, String subDomain, @CacheKey Integer sAccountId, String inviteUser, boolean isSendInvitationEmail);

    @CacheEvict
    void updateUserAccount(SimpleUser user, @CacheKey Integer sAccountId);

    @CacheEvict
    void updateUserAccountStatus(String username, @CacheKey Integer sAccountId, String registerStatus);

    @CacheEvict
    void pendingUserAccount(String username, @CacheKey Integer accountId);

    @CacheEvict
    void pendingUserAccounts(List<String> usernames, @CacheKey Integer accountId);

    @Cacheable
    SimpleUser findUserByUserNameInAccount(String username, @CacheKey Integer accountId);

    @Cacheable
    int getTotalActiveUsersInAccount(@CacheKey Integer accountId);

    User findUserByUserName(String username);

    void requestToResetPassword(String username);
}
