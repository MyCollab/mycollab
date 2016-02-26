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

import com.esofthead.mycollab.common.dao.OptionValMapper;
import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.domain.OptionValExample;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.domain.criteria.*;
import com.esofthead.mycollab.module.project.service.*;
import com.esofthead.mycollab.module.tracker.dao.BugRelatedItemMapper;
import com.esofthead.mycollab.module.tracker.domain.*;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author myCollab Ltd
 * @since 5.2.8
 */
@Service
public class ProjectTemplateServiceImpl implements ProjectTemplateService {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectTemplateService.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRoleService projectRoleService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private BugService bugService;

    @Autowired
    private BugRelatedItemMapper bugRelatedItemMapper;

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private VersionService versionService;

    @Autowired
    private RiskService riskService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OptionValMapper optionValMapper;

    @Override
    public Integer cloneProject(Integer projectId, String newPrjName, String newPrjKey, Integer sAccountId, String username) {
        SimpleProject project = projectService.findById(projectId, sAccountId);
        if (project != null) {
            LOG.info("Clone project info");
            project.setId(null);
            project.setName(newPrjName);
            project.setShortname(newPrjKey);
            Integer newProjectId = projectService.savePlainProject(project, username);
            cloneProjectOptions(projectId, newProjectId, sAccountId);
            Map<Integer, Integer> mapRoleIds = cloneProjectRoles(projectId, newProjectId, username, sAccountId);
            cloneProjectMembers(projectId, newProjectId, mapRoleIds, username);
            cloneProjectMessages(projectId, newProjectId, username);
            cloneProjectRisks(projectId, newProjectId, username);
            Map<Integer, Integer> milestoneMapIds = cloneProjectMilestone(projectId, newProjectId, username);
            cloneProjectTasks(projectId, newProjectId, milestoneMapIds, username);
            Map<Integer, Integer> componentMapIds = cloneProjectComponents(projectId, newProjectId, username, sAccountId);
            Map<Integer, Integer> versionMapIds = cloneProjectVersions(projectId, newProjectId, username, sAccountId);
            cloneProjectBugs(projectId, newProjectId, milestoneMapIds, componentMapIds, versionMapIds, username, sAccountId);

            return newProjectId;
        } else {
            throw new MyCollabException("Can not find project with id " + projectId);
        }
    }

    private void cloneProjectOptions(Integer projectId, Integer newProjectId, Integer sAccountId) {
        OptionValExample ex = new OptionValExample();
        ex.createCriteria().andIsdefaultEqualTo(false).andSaccountidEqualTo(sAccountId).andExtraidEqualTo(projectId);
        List<OptionVal> optionVals = optionValMapper.selectByExample(ex);
        for (OptionVal optionVal : optionVals) {
            optionVal.setId(null);
            optionVal.setExtraid(newProjectId);
            optionValMapper.insert(optionVal);
        }
    }

    private Map<Integer, Integer> cloneProjectRoles(Integer projectId, Integer newProjectId, String username, Integer sAccountId) {
        LOG.info("Clone project roles");
        Map<Integer, Integer> mapRoleIds = new HashMap<>();
        ProjectRoleSearchCriteria searchCriteria = new ProjectRoleSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(projectId));
        List<SimpleProjectRole> roles = projectRoleService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        for (SimpleProjectRole role : roles) {
            role.setId(null);
            role.setProjectid(newProjectId);
            Integer newRoleId = projectRoleService.saveWithSession(role, username);
            projectRoleService.savePermission(projectId, newRoleId, role.getPermissionMap(), sAccountId);
            mapRoleIds.put(role.getId(), newRoleId);
        }
        return mapRoleIds;
    }

    private void cloneProjectTasks(Integer projectId, Integer newProjectId, Map<Integer, Integer> milestoneMapIds, String username) {
        LOG.info("Clone project tasks");
        Map<Integer, Integer> taskMapIds = new HashMap<>();
        TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
        searchCriteria.setProjectid(NumberSearchField.and(projectId));
        List<SimpleTask> tasks = projectTaskService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        cloneProjectTasks(newProjectId, milestoneMapIds, taskMapIds, tasks, username);
    }

    private void cloneProjectTasks(Integer newProjectId, Map<Integer, Integer> milestoneMapIds,
                                   Map<Integer, Integer> taskMapIds, List<SimpleTask> tasks, String username) {
        List<SimpleTask> pendingTasks = new ArrayList<>();
        for (SimpleTask task : tasks) {
            Integer taskId = task.getId();
            Integer parentTaskId = task.getParenttaskid();
            if (parentTaskId == null) {
                task.setId(null);
                task.setMilestoneid(milestoneMapIds.get(task.getMilestoneid()));
                task.setProjectid(newProjectId);
                Integer newTaskId = projectTaskService.saveWithSession(task, username);
                taskMapIds.put(taskId, newTaskId);
            } else {
                Integer candidateParentTaskId = taskMapIds.get(parentTaskId);
                if (candidateParentTaskId != null) {
                    task.setId(null);
                    task.setProjectid(newProjectId);
                    task.setMilestoneid(milestoneMapIds.get(task.getMilestoneid()));
                    task.setParenttaskid(candidateParentTaskId);
                    Integer newTaskId = projectTaskService.saveWithSession(task, username);
                    taskMapIds.put(taskId, newTaskId);
                } else {
                    pendingTasks.add(task);
                }
            }
        }
        if (pendingTasks.size() > 0) {
            cloneProjectTasks(newProjectId, milestoneMapIds, taskMapIds, pendingTasks, username);
        }
    }

    private Map<Integer, Integer> cloneProjectVersions(Integer projectId, Integer newProjectId, String username, Integer sAccountId) {
        LOG.info("Clone project versions");
        Map<Integer, Integer> versionMapIds = new HashMap<>();
        VersionSearchCriteria searchCriteria = new VersionSearchCriteria();
        searchCriteria.setProjectId(NumberSearchField.and(projectId));
        List<SimpleVersion> versions = versionService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        for (SimpleVersion version : versions) {
            Integer versionId = version.getId();
            version.setId(null);
            version.setProjectid(newProjectId);
            Integer newVersionId = versionService.saveWithSession(version, username);
            versionMapIds.put(versionId, newVersionId);
        }
        return versionMapIds;
    }

    private Map<Integer, Integer> cloneProjectComponents(Integer projectId, Integer newProjectId, String username, Integer sAccountId) {
        LOG.info("Clone project components");
        Map<Integer, Integer> componentMapIds = new HashMap<>();
        ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
        searchCriteria.setProjectid(NumberSearchField.and(projectId));
        List<SimpleComponent> components = componentService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        for (SimpleComponent component : components) {
            Integer componentId = component.getId();
            component.setId(null);
            component.setProjectid(newProjectId);
            Integer newComponentId = componentService.saveWithSession(component, username);
            componentMapIds.put(componentId, newComponentId);
        }
        return componentMapIds;
    }

    private void cloneProjectBugs(Integer projectId, Integer newProjectId, Map<Integer, Integer> milestoneMapIds,
                                  Map<Integer, Integer> componentMapIds, Map<Integer, Integer> versionMapIds,
                                  String username, Integer sAccountId) {
        LOG.info("Clone project bugs");
        BugSearchCriteria searchCriteria = new BugSearchCriteria();
        searchCriteria.setProjectId(NumberSearchField.and(projectId));
        List<SimpleBug> bugs = bugService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        for (SimpleBug bug : bugs) {
            Integer bugId = bug.getId();
            bug.setId(null);
            bug.setProjectid(newProjectId);
            bug.setMilestoneid(milestoneMapIds.get(bug.getMilestoneid()));
            Integer newBugId = bugService.saveWithSession(bug, username);

            List<Version> affectedVersions = bug.getAffectedVersions();
            for (Version version : affectedVersions) {
                BugRelatedItem bugRelatedItem = new BugRelatedItem();
                bugRelatedItem.setBugid(newBugId);
                bugRelatedItem.setType(SimpleRelatedBug.AFFVERSION);
                bugRelatedItem.setTypeid(versionMapIds.get(version.getId()));
                bugRelatedItemMapper.insert(bugRelatedItem);
            }

            List<Version> fixedVersions = bug.getFixedVersions();
            for (Version version : fixedVersions) {
                BugRelatedItem bugRelatedItem = new BugRelatedItem();
                bugRelatedItem.setBugid(newBugId);
                bugRelatedItem.setType(SimpleRelatedBug.FIXVERSION);
                bugRelatedItem.setTypeid(versionMapIds.get(version.getId()));
                bugRelatedItemMapper.insert(bugRelatedItem);
            }

            List<Component> components = bug.getComponents();
            for (Component component : components) {
                BugRelatedItem bugRelatedItem = new BugRelatedItem();
                bugRelatedItem.setBugid(newBugId);
                bugRelatedItem.setType(SimpleRelatedBug.COMPONENT);
                bugRelatedItem.setTypeid(componentMapIds.get(component.getId()));
                bugRelatedItemMapper.insert(bugRelatedItem);
            }
        }
    }

    private void cloneProjectMembers(Integer projectId, Integer newProjectId, Map<Integer, Integer> mapRoleIds, String username) {
        LOG.info("Clone project members");
        ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(projectId));
        searchCriteria.setStatus(StringSearchField.and(ProjectMemberStatusConstants.ACTIVE));
        List<SimpleProjectMember> members = projectMemberService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        for (SimpleProjectMember member : members) {
            member.setId(null);
            member.setProjectid(newProjectId);
            if (Boolean.FALSE.equals(member.getIsadmin())) {
                Integer newRoleId = mapRoleIds.get(member.getProjectroleid());
                member.setProjectroleid(newRoleId);
            }
            projectMemberService.saveWithSession(member, username);
        }
    }

    private void cloneProjectMessages(Integer projectId, Integer newProjectId, String username) {
        LOG.info("Clone project messages");
        MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
        searchCriteria.setProjectids(new SetSearchField<>(projectId));
        List<SimpleMessage> messages = messageService.findPagableListByCriteria(new SearchRequest<>
                (searchCriteria, 0, Integer.MAX_VALUE));
        for (SimpleMessage message : messages) {
            message.setId(null);
            message.setProjectid(newProjectId);
            messageService.saveWithSession(message, username);
        }
    }

    private void cloneProjectRisks(Integer projectId, Integer newProjectId, String username) {
        LOG.info("Clone project risks");
        RiskSearchCriteria searchCriteria = new RiskSearchCriteria();
        searchCriteria.setProjectId(NumberSearchField.and(projectId));
        List<SimpleRisk> risks = riskService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        for (SimpleRisk risk : risks) {
            risk.setId(null);
            risk.setProjectid(newProjectId);
            riskService.saveWithSession(risk, username);
        }
    }

    private Map<Integer, Integer> cloneProjectMilestone(Integer projectId, Integer newProjectId, String username) {
        LOG.info("Clone project milestones");
        Map<Integer, Integer> milestoneMapIds = new HashMap<>();
        MilestoneSearchCriteria searchCriteria = new MilestoneSearchCriteria();
        searchCriteria.setProjectIds(new SetSearchField<>(projectId));
        List<SimpleMilestone> milestones = milestoneService.findPagableListByCriteria(new SearchRequest<>(searchCriteria));
        for (SimpleMilestone milestone : milestones) {
            Integer milestoneId = milestone.getId();
            milestone.setId(null);
            milestone.setProjectid(newProjectId);
            Integer newMilestoneId = milestoneService.saveWithSession(milestone, username);
            milestoneMapIds.put(milestoneId, newMilestoneId);
        }
        return milestoneMapIds;
    }
}
