package com.mycollab.common.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.mycollab.module.user.service.UserService
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat

@RunWith(SpringJUnit4ClassRunner::class)
class UserServiceTest : IntegrationServiceTest() {
    @Autowired
    private lateinit var userService: UserService

    @DataSet
    @Test
    fun testGetListUser() {
        val criteria = UserSearchCriteria()
        criteria.saccountid = NumberSearchField(1)
        val users = userService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleUser>
        assertThat(users.size).isEqualTo(4)
        assertThat<SimpleUser>(users).extracting("username").contains(
                "hainguyen@esofthead.com", "linhduong@esofthead.com",
                "huynguyen@esofthead.com", "test@esofthead.com")
    }

    @DataSet
    @Test
    fun updateUserEmail() {
        val user = userService.findUserByUserNameInAccount("hainguyen@esofthead.com", 1)
        assertThat(user!!.email).isEqualTo("hainguyen@esofthead.com")

        user.email = "hannguyen@esofthead.com"
        userService.updateUserAccount(user, 1)

        val anotherUser = userService.findUserByUserNameInAccount("hannguyen@esofthead.com", 1)
        assertThat(anotherUser!!.email).isEqualTo("hannguyen@esofthead.com")
        assertThat(anotherUser.lastname).isEqualTo("Hai")
    }

    @DataSet
    @Test
    fun testGetTotalActiveUsersInAccount() {
        val totalActiveUsersInAccount = userService.getTotalActiveUsersInAccount(1)
        assertThat(totalActiveUsersInAccount).isEqualTo(3)
    }

    @DataSet
    @Test
    fun testFindUserByUsernameInAccount() {
        val user = userService.findUserByUserNameInAccount("hainguyen@esofthead.com", 1)
        assertThat(user!!.username).isEqualTo("hainguyen@esofthead.com")
        assertThat(user.accountId).isEqualTo(1)
        assertThat(user.firstname).isEqualTo("Nguyen")
        assertThat(user.lastname).isEqualTo("Hai")
    }
}
