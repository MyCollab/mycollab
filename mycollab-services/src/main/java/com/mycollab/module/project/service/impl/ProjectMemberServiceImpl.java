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

package com.mycollab.module.project.service.impl;

import com.mycollab.core.UserInvalidInputException;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.core.utils.ArrayUtils;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.dao.ProjectMemberMapper;
import com.mycollab.module.project.dao.ProjectMemberMapperExt;
import com.mycollab.module.project.domain.ProjectMember;
import com.mycollab.module.project.domain.ProjectMemberExample;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.esb.DeleteProjectMemberEvent;
import com.mycollab.module.project.esb.InviteProjectMembersEvent;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.user.dao.UserAccountMapper;
import com.mycollab.module.user.dao.UserMapper;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.RoleService;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ProjectMemberServiceImpl extends DefaultService<Integer, ProjectMember, ProjectMemberSearchCriteria> implements ProjectMemberService {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectMemberServiceImpl.class);

    @Autowired
    private ProjectService projectService;

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
        SimpleProjectMember oldMember = findById(member.getId(), member.getSaccountid());
        if (oldMember != null) {
            if (Boolean.FALSE.equals(member.getIsadmin()) && Boolean.TRUE.equals(oldMember.getIsadmin())) {
                ProjectMemberExample userAccountEx = new ProjectMemberExample();
                userAccountEx.createCriteria().andUsernameNotIn(Collections.singletonList(member.getUsername())).andProjectidEqualTo(member.getProjectid())
                        .andIsadminEqualTo(Boolean.TRUE).andStatusEqualTo(ProjectMemberStatusConstants.ACTIVE);
                if (projectMemberMapper.countByExample(userAccountEx) == 0) {
                    throw new UserInvalidInputException(String.format("Can not change role of user %s. The reason is " +
                            "%s is the unique account owner of the current project.", member.getUsername(), member.getUsername()));
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
                throw new UserInvalidInputException("Can not delete users. The reason is there is no project owner in the rest users");
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
    public boolean isUserBelongToProject(String username, Integer projectId, Integer sAccountId) {
        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectId(new NumberSearchField(projectId));
        criteria.setSaccountid(new NumberSearchField(sAccountId));
        criteria.setInvolvedMember(StringSearchField.and(username));
        criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE,
                ProjectMemberStatusConstants.NOT_ACCESS_YET));
        return (getTotalCount(criteria) > 0);
    }

    @Override
    public List<SimpleUser> getActiveUsersInProjects(List<Integer> projectIds, Integer sAccountId) {
        return projectMemberMapperExt.getActiveUsersInProjects(projectIds, sAccountId);
    }

    @Override
    public List findPageableListByCriteria(BasicSearchRequest<ProjectMemberSearchCriteria> searchRequest) {
        return super.findPageableListByCriteria(searchRequest);
    }

    @Override
    public SimpleUser getActiveUserOfProject(String username, Integer projectId, @CacheKey Integer sAccountId) {
        return projectMemberMapperExt.getActiveUserOfProject(username, projectId, sAccountId);
    }

    @Override
    public List<SimpleProjectMember> findMembersHourlyInProject(Integer projectId, Integer sAccountId, Date start, Date end) {
        return projectMemberMapperExt.findMembersHourlyInProject(projectId, sAccountId, start, end);
    }
}
