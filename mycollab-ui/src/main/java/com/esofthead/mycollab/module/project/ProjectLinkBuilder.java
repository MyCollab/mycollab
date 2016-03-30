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
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
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

import static com.esofthead.mycollab.utils.TooltipHelper.TOOLTIP_ID;

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
        return ProjectLinkGenerator.generateProjectFullLink(AppContext.getSiteUrl(), projectId);
    }

    public static String generateComponentPreviewFullLink(Integer projectId, Integer componentId) {
        if (projectId == null || componentId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateBugComponentPreviewFullLink(AppContext.getSiteUrl(), projectId, componentId);
    }

    public static String generateBugVersionPreviewFullLink(Integer projectId, Integer versionId) {
        if (projectId == null || versionId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateBugVersionPreviewFullLink(AppContext.getSiteUrl(), projectId, versionId);
    }

    public static String generateRolePreviewFullLink(Integer projectId, Integer roleId) {
        if (projectId == null || roleId == null) {
            return "";
        }
        return ProjectLinkGenerator.generateRolePreviewFullLink(AppContext.getSiteUrl(), projectId, roleId);
    }

    public static String generateProjectMemberFullLink(Integer projectId, String memberName) {
        return ProjectLinkGenerator.generateProjectMemberFullLink(AppContext.getSiteUrl(), projectId, memberName);
    }

    public static String generateProjectMemberHtmlLink(Integer projectId, String username, String displayName, String avarId,
                                                       Boolean isDisplayTooltip) {
        Img userAvatar = new Img("", StorageFactory.getInstance().getAvatarPath(avarId, 16));
        A link = new A().setId("tag" + TOOLTIP_ID).setHref(generateProjectMemberFullLink(projectId,
                username)).appendText(StringUtils.trim(displayName, 30, true));
        if (isDisplayTooltip) {
            link.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username));
            link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            return new DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), link).write();
        } else {
            return new DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), link).write();
        }
    }

    public static String generateProjectMemberHtmlLink(Integer projectId, String username, Boolean isDisplayTooltip) {
        ProjectMemberService projectMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
        SimpleProjectMember member = projectMemberService.findMemberByUsername(username, projectId, AppContext.getAccountId());
        if (member != null) {
            return generateProjectMemberHtmlLink(projectId, member.getUsername(), member.getDisplayName(), member
                    .getMemberAvatarId(), isDisplayTooltip);
        } else {
            return null;
        }
    }

    public static String generateBugPreviewFullLink(Integer bugKey, String prjShortName) {
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateBugPreviewLink(bugKey, prjShortName);
    }

    public static String generateMessagePreviewFullLink(Integer projectId, Integer messageId) {
        if (projectId == null || messageId == null) {
            return "";
        }
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateMessagePreviewLink(projectId, messageId);
    }

    public static String generateRiskPreviewFullLink(Integer projectId, Integer riskId) {
        if (projectId == null || riskId == null) {
            return "";
        }
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + "project/risk/preview/" + UrlEncodeDecoder.encode(projectId + "/" + riskId);
    }

    public static String generateTaskPreviewFullLink(Integer taskKey, String prjShortName) {
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateTaskPreviewLink(taskKey, prjShortName);
    }

    public static String generateMilestonePreviewFullLink(Integer projectId, Integer milestoneId) {
        if (projectId == null || milestoneId == null) {
            return "";
        }
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generateMilestonePreviewLink(projectId, milestoneId);
    }

    public static String generateClientPreviewFullLink(Integer clientId) {
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator
                .generateClientPreviewLink(clientId);
    }

    public static final String generatePageFolderFullLink(Integer projectId, String folderPath) {
        if (projectId == null || folderPath == null) {
            return "";
        }
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generatePagesLink(projectId, folderPath);
    }

    public static final String generatePageFullLink(Integer projectId, String pagePath) {
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
                + ProjectLinkGenerator.generatePageRead(projectId, pagePath);
    }

    public static String generateStandupDashboardLink(Integer projectId) {
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator
                .generateStandupDashboardLink(projectId);
    }

    public static String generateProjectItemHtmlLinkAndTooltip(String prjShortName, Integer projectId, String summary, String type, String typeId) {
        Text image = new Text(ProjectAssetsManager.getAsset(type).getHtml());
        A link = new A().setId("tag" + TOOLTIP_ID);
        link.setHref(AppContext.getSiteUrl() + generateProjectItemLink(prjShortName, projectId, type, typeId)).appendChild(new Text(summary));
        link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(type, typeId));
        link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        Div div = new DivLessFormatter().appendChild(image, DivLessFormatter.EMPTY_SPACE(), link);
        return div.write();
    }

    public static String generateProjectItemLink(String prjShortName, Integer projectId, String type, String typeId) {
        String result = "";

        if (typeId == null || StringUtils.isBlank(typeId) || "null".equals(typeId)) {
            return "";
        }

        try {
            if (ProjectTypeConstants.PROJECT.equals(type)) {
            } else if (ProjectTypeConstants.MESSAGE.equals(type)) {
                result = ProjectLinkGenerator.generateMessagePreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.MILESTONE.equals(type)) {
                result = ProjectLinkGenerator.generateMilestonePreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.RISK.equals(type)) {
                result = ProjectLinkGenerator.generateRiskPreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.TASK.equals(type)) {
                result = ProjectLinkGenerator.generateTaskPreviewLink(Integer.parseInt(typeId), prjShortName);
            } else if (ProjectTypeConstants.BUG.equals(type)) {
                result = ProjectLinkGenerator.generateBugPreviewLink(Integer.parseInt(typeId), prjShortName);
            } else if (ProjectTypeConstants.BUG_COMPONENT.equals(type)) {
                result = ProjectLinkGenerator.generateBugComponentPreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
                result = ProjectLinkGenerator.generateBugVersionPreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.STANDUP.equals(type)) {
                result = ProjectLinkGenerator.generateStandUpPreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.PAGE.equals(type)) {
                result = ProjectLinkGenerator.generatePageRead(projectId, typeId);
            }
        } catch (Exception e) {
            LOG.error(String.format("Error generate tooltip%d---%s---%s", projectId, type, typeId), e);
        }

        return "#" + result;
    }
}
