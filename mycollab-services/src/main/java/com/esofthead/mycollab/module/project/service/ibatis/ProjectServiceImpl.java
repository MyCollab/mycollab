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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.billing.service.BillingPlanCheckerService;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.ProjectMapper;
import com.esofthead.mycollab.module.project.dao.ProjectMapperExt;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.esb.AddProjectEvent;
import com.esofthead.mycollab.module.project.esb.DeleteProjectEvent;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.schedule.email.project.ProjectRelayEmailNotificationAction;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.PermissionMap;
import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "id", notifyAgent = ProjectRelayEmailNotificationAction.class)
public class ProjectServiceImpl extends DefaultService<Integer, Project, ProjectSearchCriteria> implements ProjectService {

    static {
        ClassInfoMap.put(ProjectServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.PROJECT));
    }

    private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMapperExt projectMapperExt;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private ProjectRoleService projectRoleService;

    @Autowired
    private BillingPlanCheckerService billingPlanCheckerService;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @SuppressWarnings("unchecked")
    @Override
    public ICrudGenericDAO<Integer, Project> getCrudMapper() {
        return projectMapper;
    }

    @Override
    public ISearchableDAO<ProjectSearchCriteria> getSearchMapper() {
        return projectMapperExt;
    }

    @Override
    public Integer updateWithSession(Project record, String username) {
        assertExistProjectShortnameInAccount(record.getId(), record.getShortname(), record.getSaccountid());
        return super.updateWithSession(record, username);
    }

    @Override
    public Integer saveWithSession(Project record, String username) {
        billingPlanCheckerService.validateAccountCanCreateMoreProject(record.getSaccountid());

        assertExistProjectShortnameInAccount(null, record.getShortname(), record.getSaccountid());

        Integer projectId = super.saveWithSession(record, username);

        // Add the first user to project
        ProjectMember projectMember = new ProjectMember();
        projectMember.setIsadmin(Boolean.TRUE);
        projectMember.setStatus(ProjectMemberStatusConstants.ACTIVE);
        projectMember.setJoindate(new GregorianCalendar().getTime());
        projectMember.setProjectid(projectId);
        projectMember.setUsername(username);
        projectMember.setSaccountid(record.getSaccountid());
        projectMemberMapper.insert(projectMember);

        // add client role to project
        ProjectRole clientRole = createProjectRole(projectId, record.getSaccountid(), "Client", "Default role for client");

        int clientRoleId = projectRoleService.saveWithSession(clientRole, username);

        PermissionMap permissionMapClient = new PermissionMap();
        for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
            String permissionName = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];

            if (permissionName.equals(ProjectRolePermissionCollections.USERS)
                    || permissionName.equals(ProjectRolePermissionCollections.ROLES)) {
                permissionMapClient.addPath(
                        ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i],
                        AccessPermissionFlag.READ_ONLY);
            } else {
                permissionMapClient.addPath(
                        ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i],
                        AccessPermissionFlag.READ_ONLY);
            }
        }
        projectRoleService.savePermission(projectId, clientRoleId, permissionMapClient, record.getSaccountid());

        // add consultant role to project
        LOG.debug("Add consultant role to project {}", record.getName());
        ProjectRole consultantRole = createProjectRole(projectId,
                record.getSaccountid(), "Consultant",
                "Default role for consultant");
        int consultantRoleId = projectRoleService.saveWithSession(consultantRole, username);

        PermissionMap permissionMapConsultant = new PermissionMap();
        for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
            String permissionName = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];

            if (permissionName.equals(ProjectRolePermissionCollections.USERS)
                    || permissionName.equals(ProjectRolePermissionCollections.ROLES)) {
                permissionMapConsultant.addPath(
                        ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i],
                        AccessPermissionFlag.READ_ONLY);
            } else {
                permissionMapConsultant.addPath(
                        ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i],
                        AccessPermissionFlag.ACCESS);
            }
        }
        projectRoleService.savePermission(projectId, consultantRoleId,
                permissionMapConsultant, record.getSaccountid());

        // add admin role to project
        LOG.debug("Add admin role to project {}", record.getName());
        ProjectRole adminRole = createProjectRole(projectId,
                record.getSaccountid(), "Admin", "Default role for admin");
        int adminRoleId = projectRoleService.saveWithSession(adminRole,
                username);

        PermissionMap permissionMapAdmin = new PermissionMap();
        for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
            permissionMapAdmin.addPath(
                    ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i],
                    AccessPermissionFlag.ACCESS);
        }
        projectRoleService.savePermission(projectId, adminRoleId,
                permissionMapAdmin, record.getSaccountid());


        //Do async task to create some post data after project is created
        AddProjectEvent event = new AddProjectEvent(projectId, record.getSaccountid());
        asyncEventBus.post(event);

        return projectId;
    }

    private void assertExistProjectShortnameInAccount(Integer projectId, String shortname, Integer sAccountId) {
        ProjectExample ex = new ProjectExample();
        ProjectExample.Criteria criteria = ex.createCriteria();
        criteria.andShortnameEqualTo(shortname).andSaccountidEqualTo(sAccountId);
        if (projectId != null) {
            criteria.andIdNotEqualTo(projectId);
        }
        if (projectMapper.countByExample(ex) > 0) {
            throw new UserInvalidInputException(String.format("There is already project in the account has short name %s", shortname));
        }
    }

    private ProjectRole createProjectRole(Integer projectId, Integer sAccountId, String roleName, String description) {
        ProjectRole projectRole = new ProjectRole();
        projectRole.setProjectid(projectId);
        projectRole.setSaccountid(sAccountId);
        projectRole.setRolename(roleName);
        projectRole.setDescription(description);
        return projectRole;
    }

    @Override
    public SimpleProject findById(Integer projectId, Integer sAccountId) {
        return projectMapperExt.findProjectById(projectId);
    }

    @Override
    public List<Integer> getProjectKeysUserInvolved(String username, Integer sAccountId) {
        ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        searchCriteria.setInvolvedMember(new StringSearchField(username));
        searchCriteria.setProjectStatuses(new SetSearchField<>(StatusI18nEnum.Open.name()));
        return projectMapperExt.getUserProjectKeys(searchCriteria);
    }

    @Override
    public String getSubdomainOfProject(Integer projectId) {
        if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
            return projectMapperExt.getSubdomainOfProject(projectId);
        } else {
            return SiteConfiguration.getSiteUrl("");
        }
    }

    @Override
    public void massRemoveWithSession(List<Project> projects, String username, Integer accountId) {
        super.massRemoveWithSession(projects, username, accountId);
        DeleteProjectEvent event = new DeleteProjectEvent(projects.toArray(new Project[projects.size()]), accountId);
        asyncEventBus.post(event);
    }

    @Override
    public Integer getTotalActiveProjectsInAccount(@CacheKey Integer sAccountId) {
        ProjectSearchCriteria criteria = new ProjectSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(sAccountId));
        criteria.setProjectStatuses(new SetSearchField<>(StatusI18nEnum.Open.name()));
        return projectMapperExt.getTotalCount(criteria);
    }

    @Override
    public List<ProjectRelayEmailNotification> findProjectRelayEmailNotifications() {
        return projectMapperExt.findProjectRelayEmailNotifications();
    }

    @Override
    public List<SimpleProject> getProjectsUserInvolved(String username, Integer sAccountId) {
        return projectMapperExt.getProjectsUserInvolved(username, sAccountId);
    }

    @Override
    public Integer getTotalActiveProjectsOfInvolvedUsers(String username, @CacheKey Integer sAccountId) {
        ProjectSearchCriteria criteria = new ProjectSearchCriteria();
        criteria.setInvolvedMember(new StringSearchField(username));
        criteria.setProjectStatuses(new SetSearchField<>(StatusI18nEnum.Open.name()));
        return projectMapperExt.getTotalCount(criteria);
    }
}
