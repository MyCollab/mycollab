/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectLinkBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectLinkBuilder.class);

    public static String generateProjectFullLink(Integer projectId) {
        if (projectId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateProjectFullLink(
                AppContext.getSiteUrl(), projectId);
    }

    public static String generateComponentPreviewFullLink(Integer projectId,
                                                          Integer componentId) {
        if (projectId == null || componentId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateBugComponentPreviewFullLink(
                AppContext.getSiteUrl(), projectId, componentId);
    }

    public static String generateBugVersionPreviewFullLink(Integer projectId,
                                                           Integer versionId) {
        if (projectId == null || versionId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateBugVersionPreviewFullLink(
                AppContext.getSiteUrl(), projectId, versionId);
    }

    public static String generateRolePreviewFullLink(Integer projectId,
                                                     Integer roleId) {
        if (projectId == null || roleId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateRolePreviewFullLink(
                AppContext.getSiteUrl(), projectId, roleId);
    }

    public static String generateProblemPreviewFullLink(Integer projectId,
                                                        Integer problemId) {
        if (projectId == null || problemId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateProblemPreviewFullLink(
                AppContext.getSiteUrl(), projectId, problemId);
    }

    public static String generateProjectMemberFullLink(int projectId, String memberName) {
        return ProjectLinkGenerator.generateProjectMemberFullLink(
                AppContext.getSiteUrl(), projectId, memberName);
    }

    public static String generateProjectMemberHtmlLink(int projectId, String username) {
        ProjectMemberService projectMemberService = ApplicationContextUtil
                .getSpringBean(ProjectMemberService.class);
        SimpleProjectMember member = projectMemberService.findMemberByUsername(
                username, projectId, AppContext.getAccountId());
        if (member != null) {
            String uid = UUID.randomUUID().toString();
            Img userAvatar = new Img("", Storage.getAvatarPath(
                    member.getMemberAvatarId(), 16));
            A link = new A().setId("tag" + uid).setHref(generateProjectMemberFullLink(projectId,
                    member.getUsername())).appendText(StringUtils.trim(member.getDisplayName(), 30, true));
            link.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, username));
            link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            return new DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), link,
                    DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid)).write();
        } else {
            return null;
        }
    }

    public static String generateBugPreviewFullLink(Integer bugKey,
                                                    String prjShortName) {
        return AppContext.getSiteUrl()
                + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateBugPreviewLink(bugKey,
                prjShortName);
    }

    public static String generateMessagePreviewFullLink(Integer projectId, Integer messageId) {
        if (projectId == null || messageId == null) {
            return "";
        }
        return AppContext.getSiteUrl()
                + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateMessagePreviewLink(projectId,
                messageId);
    }

    public static String generateRiskPreviewFullLink(Integer projectId,
                                                     Integer riskId) {
        if (projectId == null || riskId == null) {
            return "";
        }
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + "project/risk/preview/"
                + UrlEncodeDecoder.encode(projectId + "/" + riskId);
    }

    public static String generateTaskPreviewFullLink(Integer taskKey,
                                                     String prjShortName) {
        return AppContext.getSiteUrl()
                + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateTaskPreviewLink(taskKey,
                prjShortName);
    }

    public static String generateTaskGroupPreviewFullLink(Integer projectId,
                                                          Integer taskgroupId) {
        if (projectId == null || taskgroupId == null) {
            return "";
        }
        return AppContext.getSiteUrl()
                + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateTaskGroupPreviewLink(projectId,
                taskgroupId);
    }

    public static String generateTaskGroupHtmlLink(int taskgroupId) {
        ProjectTaskListService taskListService = ApplicationContextUtil
                .getSpringBean(ProjectTaskListService.class);
        SimpleTaskList taskList = taskListService.findById(taskgroupId,
                AppContext.getAccountId());
        if (taskList != null) {
            DivLessFormatter div = new DivLessFormatter();
            Text img = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK_LIST).getHtml());
            A link = new A();
            link.setHref(generateTaskGroupPreviewFullLink(
                    taskList.getProjectid(), taskList.getId()));
            Text text = new Text(taskList.getName());
            link.appendChild(text);
            return div.appendChild(img, DivLessFormatter.EMPTY_SPACE(), link).write();
        } else {
            return null;
        }
    }

    public static String generateMilestonePreviewFullLink(Integer projectId,
                                                          Integer milestoneId) {
        if (projectId == null || milestoneId == null) {
            return "";
        }
        return AppContext.getSiteUrl()
                + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateMilestonePreviewLink(projectId,
                milestoneId);
    }

    public static String generateMilestoneHtmlLink(int milestoneId) {
        MilestoneService milestoneService = ApplicationContextUtil
                .getSpringBean(MilestoneService.class);
        SimpleMilestone milestone = milestoneService.findById(milestoneId,
                AppContext.getAccountId());
        if (milestone != null) {
            DivLessFormatter div = new DivLessFormatter();
            A link = new A();
            link.setHref(generateMilestonePreviewFullLink(
                    milestone.getProjectid(), milestone.getId()));
            Text text = new Text(milestone.getName());
            link.appendChild(text);
            return div.appendChild(new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml()),
                    DivLessFormatter.EMPTY_SPACE(), link).write();
        } else {
            return null;
        }
    }

    public static String generateProjectItemHtmlLink(String prjShortName,
                                                     Integer projectId, String summary, String type, String typeId) {
        String uid = UUID.randomUUID().toString();
        Text image = new Text(ProjectAssetsManager.getAsset(type).getHtml());
        A link = new A().setId("tag" + uid);
        link.setHref(AppContext.getSiteUrl() + generateProjectItemLink(prjShortName, projectId, type, typeId)).appendChild(new Text(summary));
        link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, type, typeId));
        link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        Div div = new DivLessFormatter().appendChild(image, DivLessFormatter.EMPTY_SPACE(), link, DivLessFormatter
                .EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
        return div.write();
    }

    public static String generateProjectItemLinkWithTooltip(String prjShortName,
                                                            Integer projectId, String itemName, String type, String typeId, String extraTypeId) {
        String uid = UUID.randomUUID().toString();
        DivLessFormatter div = new DivLessFormatter();
        Text img = new Text(ProjectAssetsManager.getAsset(type).getHtml());
        A itemLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectItemLink(
                prjShortName, projectId, type, extraTypeId));
        itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, type, typeId));
        itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        itemLink.appendText(itemName);

        div.appendChild(img, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
        return div.write();
    }

    public static String generateProjectItemLink(String prjShortName,
                                                 Integer projectId, String type, String typeId) {
        String result = "";

        try {
            if (ProjectTypeConstants.PROJECT.equals(type)) {
            } else if (ProjectTypeConstants.MESSAGE.equals(type)) {
                result = ProjectLinkGenerator.generateMessagePreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.MILESTONE.equals(type)) {
                result = ProjectLinkGenerator.generateMilestonePreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.PROBLEM.equals(type)) {
                result = ProjectLinkGenerator.generateProblemPreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.RISK.equals(type)) {
                result = ProjectLinkGenerator.generateRiskPreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.TASK.equals(type)) {
                result = ProjectLinkGenerator.generateTaskPreviewLink(
                        Integer.parseInt(typeId), prjShortName);
            } else if (ProjectTypeConstants.TASK_LIST.equals(type)) {
                result = ProjectLinkGenerator.generateTaskGroupPreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.BUG.equals(type)) {
                result = ProjectLinkGenerator.generateBugPreviewLink(
                        Integer.parseInt(typeId), prjShortName);
            } else if (ProjectTypeConstants.BUG_COMPONENT.equals(type)) {
                result = ProjectLinkGenerator.generateBugComponentPreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
                result = ProjectLinkGenerator.generateBugVersionPreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.STANDUP.equals(type)) {
                result = ProjectLinkGenerator.generateStandUpPreviewLink(
                        projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.PAGE.equals(type)) {
                result = ProjectLinkGenerator.generatePageRead(projectId,
                        typeId);
            }
        } catch (Exception e) {
            LOG.error("Error while generate link {} {} {} {}", prjShortName, projectId, type, typeId);
        }

        return "#" + result;
    }
}
