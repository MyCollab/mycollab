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

import java.util.Arrays
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.esofthead.mycollab.cache.CacheUtils
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper
import com.esofthead.mycollab.module.project.domain.ProjectMember
import com.esofthead.mycollab.module.project.domain.ProjectMemberExample
import com.esofthead.mycollab.module.project.service.ProjectMemberService
import com.esofthead.mycollab.module.user.esb.UserRemovedCommand
import com.esofthead.mycollab.module.user.service.UserService

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@Component object UserRemovedCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[UserRemovedCommandImpl])
}

@Component class UserRemovedCommandImpl extends UserRemovedCommand {
    @Autowired private val userService: UserService = null
    @Autowired private val projectMemberMapper: ProjectMemberMapper = null
    @Autowired private val projectMemberService: ProjectMemberService = null

    def userRemoved(username: String, accountid: Integer) {
        UserRemovedCommandImpl.LOG.debug("Remove user {} with account id {}", Array(username, accountid))
        val ex: ProjectMemberExample = new ProjectMemberExample
        ex.createCriteria.andStatusIn(Arrays.asList(RegisterStatusConstants.ACTIVE, RegisterStatusConstants.SENT_VERIFICATION_EMAIL,
            RegisterStatusConstants.VERIFICATING)).andSaccountidEqualTo(accountid).andUsernameEqualTo(username)
        val projectMember: ProjectMember = new ProjectMember
        projectMember.setStatus(RegisterStatusConstants.DELETE)
        projectMemberMapper.updateByExampleSelective(projectMember, ex)
        CacheUtils.cleanCaches(accountid, classOf[ProjectMemberService])
    }
}