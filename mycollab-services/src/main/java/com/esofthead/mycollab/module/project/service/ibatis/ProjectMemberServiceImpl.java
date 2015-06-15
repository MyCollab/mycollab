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

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.esb.CamelProxyBuilderUtil;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.dao.ProjectMapper;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapperExt;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.esb.DeleteProjectMemberCommand;
import com.esofthead.mycollab.module.project.esb.InviteProjectMembersCommand;
import com.esofthead.mycollab.module.project.esb.ProjectEndPoints;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.user.UserExistedException;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.dao.UserMapper;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ProjectMemberServiceImpl extends DefaultService<Integer, ProjectMember, ProjectMemberSearchCriteria>
        implements ProjectMemberService {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectMemberServiceImpl.class);

    @Autowired
    protected ProjectMemberMapper projectMemberMapper;

    @Autowired
    protected ProjectMemberMapperExt projectMemberMapperExt;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private RoleService roleService;

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
    public Integer removeWithSession(Integer primaryKey, String username, Integer accountId) {
        SimpleProjectMember projectMember = projectMemberMapperExt
                .findMemberById(primaryKey);
        ProjectMapper projectMapper = ApplicationContextUtil.getSpringBean(ProjectMapper.class);

        if (projectMember != null) {
            try {
                Project project = projectMapper.selectByPrimaryKey(projectMember.getProjectid());
                DeleteProjectMemberCommand projectMemberDeleteListener = CamelProxyBuilderUtil
                        .build(ProjectEndPoints.PROJECT_MEMBER_DELETE_ENDPOINT,
                                DeleteProjectMemberCommand.class);
                projectMemberDeleteListener.projectMemberRemoved(username,
                        primaryKey, projectMember.getProjectid(), project.getSaccountid());
            } catch (Exception e) {
                LOG.error("Error while notify project member delete", e);
            }

            projectMember.setStatus(RegisterStatusConstants.DELETE);
            projectMemberMapper.updateByPrimaryKeySelective(projectMember);
        }

        return 1;
    }

    @Override
    public List<SimpleUser> getActiveUsersInProject(Integer projectId, Integer sAccountId) {
        return projectMemberMapperExt.getActiveUsersInProject(projectId, sAccountId);
    }

    @Override
    public void inviteProjectMembers(String[] email, Integer projectId,
                                     Integer projectRoleId, String inviteUser, String inviteMessage,
                                     Integer sAccountId) {
        InviteProjectMembersCommand listener = CamelProxyBuilderUtil.build(
                ProjectEndPoints.PROJECT_SEND_INVITATION_USER,
                InviteProjectMembersCommand.class);
        listener.inviteUsers(email, projectId, projectRoleId, inviteUser,
                inviteMessage, sAccountId);
    }

    @Override
    public void acceptProjectInvitationByNewUser(String email, String password,
                                                 Integer projectId, Integer projectRoleId, Integer sAccountId) {

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
        CacheUtils.cleanCache(sAccountId, ProjectMemberService.class.getName());
    }

    @Override
    public boolean isUserBelongToProject(String username, Integer projectId,
                                         Integer sAccountId) {
        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectId(new NumberSearchField(projectId));
        criteria.setSaccountid(new NumberSearchField(sAccountId));
        criteria.setInvolvedMember(new StringSearchField(username));
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
