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

package com.esofthead.mycollab.module.project.service.ibatis;

import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.core.utils.ArrayUtils;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapperExt;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.ProjectMemberExample;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.esb.DeleteProjectMemberEvent;
import com.esofthead.mycollab.module.project.esb.InviteProjectMembersEvent;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.user.UserExistedException;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.dao.UserMapper;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ProjectMemberServiceImpl extends DefaultService<Integer, ProjectMember, ProjectMemberSearchCriteria> implements ProjectMemberService {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectMemberServiceImpl.class);

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private ProjectMemberMapperExt projectMemberMapperExt;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO getCrudMapper() {
        return projectMemberMapper;
    }

    @Override
    public ISearchableDAO<ProjectMemberSearchCriteria> getSearchMapper() {
        return projectMemberMapperExt;
    }

    @Override
    public SimpleProjectMember findById(Integer memberId, Integer sAccountId) {
        return projectMemberMapperExt.findMemberById(memberId);
    }

    @Override
    public List<SimpleUser> getUsersNotInProject(Integer projectId, Integer sAccountId) {
        return projectMemberMapperExt.getUsersNotInProject(projectId, sAccountId);
    }

    @Override
    public SimpleProjectMember findMemberByUsername(String username, Integer projectId, Integer sAccountId) {
        return projectMemberMapperExt.findMemberByUsername(username, projectId);
    }

    @Override
    public Integer updateWithSession(ProjectMember member, String username) {
        if (Boolean.TRUE.equals(member.getIsadmin())) {
            if (member.getProjectroleid() != null && member.getProjectroleid() != -1) {
                ProjectMemberExample userAccountEx = new ProjectMemberExample();
                userAccountEx.createCriteria().andUsernameNotIn(Arrays.asList(member.getUsername())).andProjectidEqualTo(member.getProjectid())
                        .andIsadminEqualTo(Boolean.TRUE).andStatusEqualTo(ProjectMemberStatusConstants.ACTIVE);
                if (projectMemberMapper.countByExample(userAccountEx) == 0) {
                    throw new UserInvalidInputException(String.format("Can not change role of user %s. The reason is " +
                            "%s is the unique account owner of the current project.", member.getUsername(), member.getUsername()));
                } else {
                    member.setIsadmin(false);
                }
            }
        }
        return super.updateWithSession(member, username);
    }

    @Override
    public void massRemoveWithSession(List<ProjectMember> members, String username, Integer accountId) {
        if (CollectionUtils.isNotEmpty(members)) {
            List<String> usernames = new ArrayList<>();
            for (ProjectMember member : members) {
                usernames.add(member.getUsername());
            }
            ProjectMemberExample ex = new ProjectMemberExample();
            ex.createCriteria().andUsernameNotIn(usernames).andProjectidEqualTo(members.get(0).getProjectid())
                    .andIsadminEqualTo(true).andStatusEqualTo(ProjectMemberStatusConstants.ACTIVE);
            if (projectMemberMapper.countByExample(ex) == 0) {
                throw new UserInvalidInputException("Can not delete users. The reason is there is no " +
                        "project owner in the rest users");
            }

            ProjectMember updateMember = new ProjectMember();
            updateMember.setStatus(ProjectMemberStatusConstants.INACTIVE);
            ex = new ProjectMemberExample();
            ex.createCriteria().andSaccountidEqualTo(accountId).andIdIn(ArrayUtils.extractIds(members));
            projectMemberMapper.updateByExampleSelective(updateMember, ex);

            DeleteProjectMemberEvent event = new DeleteProjectMemberEvent(members.toArray(new ProjectMember[members.size()]),
                    username, accountId);
            asyncEventBus.post(event);
        }
    }

    @Override
    public List<SimpleUser> getActiveUsersInProject(Integer projectId, Integer sAccountId) {
        return projectMemberMapperExt.getActiveUsersInProject(projectId, sAccountId);
    }

    @Override
    public void inviteProjectMembers(String[] email, Integer projectId, Integer projectRoleId, String inviteUser,
                                     String inviteMessage, Integer sAccountId) {
        InviteProjectMembersEvent event = new InviteProjectMembersEvent(email, projectId, projectRoleId, inviteUser,
                inviteMessage, sAccountId);
        asyncEventBus.post(event);
    }

    @Override
    public void acceptProjectInvitationByNewUser(String email, String password, Integer projectId,
                                                 Integer projectRoleId, Integer sAccountId) {
        Date now = new GregorianCalendar().getTime();
        try {
            SimpleUser simpleUser = new SimpleUser();
            simpleUser.setAccountId(sAccountId);
            simpleUser.setFirstname("");
            simpleUser.setLastname(StringUtils.extractNameFromEmail(email));
            simpleUser.setRegisteredtime(now);
            simpleUser.setRegisterstatus(RegisterStatusConstants.ACTIVE);
            simpleUser.setPassword(PasswordEncryptHelper.encryptSaltPassword(password));
            simpleUser.setUsername(email);
            simpleUser.setEmail(email);
            LOG.debug("Save user {}", BeanUtility.printBeanObj(simpleUser));
            userMapper.insert(simpleUser);
        } catch (DuplicateKeyException e) {
            throw new UserExistedException("User existed " + email);
        }

        LOG.debug("Assign guest role for this user {}", email);
        Integer systemGuestRoleId = roleService.getSystemRoleId(
                SimpleRole.GUEST, sAccountId);
        if (systemGuestRoleId == null) {
            LOG.error("Can not find guess role for account {}", sAccountId);
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(email);
        userAccount.setAccountid(sAccountId);
        userAccount.setRegisterstatus(RegisterStatusConstants.ACTIVE);
        userAccount.setIsaccountowner(false);
        userAccount.setRegisteredtime(now);
        userAccount.setRoleid(systemGuestRoleId);

        LOG.debug("Start save user account {}", BeanUtility.printBeanObj(userAccount));
        userAccountMapper.insert(userAccount);

        ProjectMember member = new ProjectMember();
        member.setProjectid(projectId);
        member.setUsername(email);
        member.setJoindate(now);
        member.setSaccountid(sAccountId);
        member.setIsadmin(false);
        member.setStatus(RegisterStatusConstants.ACTIVE);
        member.setProjectroleid(projectRoleId);
        LOG.debug("Start save project member {}", BeanUtility.printBeanObj(member));

        saveWithSession(member, "");
    }

    @Override
    public boolean isUserBelongToProject(String username, Integer projectId, Integer sAccountId) {
        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectId(new NumberSearchField(projectId));
        criteria.setSaccountid(new NumberSearchField(sAccountId));
        criteria.setInvolvedMember(StringSearchField.and(username));
        return (getTotalCount(criteria) > 0);
    }

    @Override
    public List<SimpleUser> getActiveUsersInProjects(List<Integer> projectIds, Integer sAccountId) {
        return projectMemberMapperExt.getActiveUsersInProjects(projectIds, sAccountId);
    }

    @Override
    public SimpleUser getActiveUserOfProject(String username, Integer projectId, @CacheKey Integer sAccountId) {
        return projectMemberMapperExt.getActiveUserOfProject(username, projectId);
    }
}
