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
package com.mycollab.common.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.mycollab.module.user.service.UserService
import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, DbUnitInitializerRule::class)
class UserServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var userService: UserService

    @DataSet
    @Test
    fun testGetListUser() {
        val criteria = UserSearchCriteria()
        criteria.saccountid = NumberSearchField(1)
        criteria.subDomain = StringSearchField.and("a")
        val users = userService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleUser>
        assertThat(users.size).isEqualTo(4)
        assertThat<SimpleUser>(users).extracting("username").contains(
                "hainguyen@esofthead.com", "linhduong@esofthead.com",
                "huynguyen@esofthead.com", "test@esofthead.com")

        assertThat(userService.getTotalCount(criteria)).isEqualTo(4)
    }

    @DataSet
    @Test
    fun updateUserEmail() {
        val user = userService.findUserByUserNameInAccount("hainguyen@esofthead.com", 1)
        assertThat(user!!.email).isEqualTo("hainguyen@esofthead.com")

        user.email = "hannguyen@esofthead.com"
        userService.updateUserAccount(user, 1)

        val anotherUser = userService.findUserByUserNameInAccount("hannguyen@esofthead.com", 1)
        assertThat(anotherUser).extracting("email", "lastname").contains("hannguyen@esofthead.com", "Hai")
    }

    @DataSet
    @Test
    fun testFindUserByUsernameInAccount() {
        val user = userService.findUserByUserNameInAccount("hainguyen@esofthead.com", 1)
        assertThat(user).extracting("username", "accountId", "firstname", "lastname").contains("hainguyen@esofthead.com", 1, "Nguyen", "Hai")
    }
}
