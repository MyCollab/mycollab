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
package com.mycollab.module.project;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.UIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM;
import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectLinkBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectLinkBuilder.class);

    public static String generateProjectFullLink(Integer projectId) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectLink(projectId);
    }

    public static String generateComponentPreviewFullLink(Integer projectId, Integer componentId) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateBugComponentPreviewLink(projectId, componentId);
    }

    public static String generateBugVersionPreviewFullLink(Integer projectId, Integer versionId) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateBugVersionPreviewLink(projectId, versionId);
    }

    public static String generateRolePreviewFullLink(Integer projectId, Integer roleId) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateRolePreviewLink(projectId, roleId);
    }

    public static String generateProjectMemberFullLink(Integer projectId, String memberName) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectMemberLink(projectId, memberName);
    }

    public static String generateProjectMemberHtmlLink(Integer projectId, String username, String displayName, String avatarId,
                                                       Boolean isDisplayTooltip) {
        Img userAvatar = new Img("", StorageFactory.getAvatarPath(avatarId, 16)).setCSSClass(UIConstants.CIRCLE_BOX);
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
        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        SimpleProjectMember member = projectMemberService.findMemberByUsername(username, projectId, MyCollabUI.getAccountId());
        if (member != null) {
            return generateProjectMemberHtmlLink(projectId, member.getUsername(), member.getDisplayName(), member
                    .getMemberAvatarId(), isDisplayTooltip);
        } else {
            return null;
        }
    }

    public static String generateBugPreviewFullLink(Integer bugKey, String prjShortName) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateBugPreviewLink(bugKey, prjShortName);
    }

    public static String generateMessagePreviewFullLink(Integer projectId, Integer messageId) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateMessagePreviewLink(projectId, messageId);
    }

    public static String generateRiskPreviewFullLink(Integer projectId, Integer riskId) {
        return URL_PREFIX_PARAM + "project/risk/preview/" + UrlEncodeDecoder.encode(projectId + "/" + riskId);
    }

    public static String generateTaskPreviewFullLink(Integer taskKey, String prjShortName) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateTaskPreviewLink(taskKey, prjShortName);
    }

    public static String generateMilestonePreviewFullLink(Integer projectId, Integer milestoneId) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateMilestonePreviewLink(projectId, milestoneId);
    }

    public static String generateClientPreviewFullLink(Integer clientId) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateClientPreviewLink(clientId);
    }

    public static String generatePageFolderFullLink(Integer projectId, String folderPath) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generatePagesLink(projectId, folderPath);
    }

    public static String generatePageFullLink(Integer projectId, String pagePath) {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generatePageRead(projectId, pagePath);
    }

    public static String generateStandupDashboardLink() {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateStandupDashboardLink();
    }

    public static String generateHoursWeeklyReportLink() {
        return MyCollabUI.getSiteUrl() + URL_PREFIX_PARAM + ProjectLinkGenerator
                .generateHoursWeeklyReportLink();
    }

    public static String generateTimesheetReportLink() {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateTimesheetReportLink();
    }

    public static String generateUsersWorkloadReportLink() {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateUsersWorkloadReportLink();
    }

    public static String generateProjectItemHtmlLinkAndTooltip(String prjShortName, Integer projectId, String summary, String type, String typeId) {
        Text image = new Text(ProjectAssetsManager.getAsset(type).getHtml());
        A link = new A().setId("tag" + TOOLTIP_ID);
        link.setHref(URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectItemLink(prjShortName, projectId, type, typeId))
                .appendChild(new Text(summary));
        link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(type, typeId));
        link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        Div div = new DivLessFormatter().appendChild(image, DivLessFormatter.EMPTY_SPACE(), link);
        return div.write();
    }
}
