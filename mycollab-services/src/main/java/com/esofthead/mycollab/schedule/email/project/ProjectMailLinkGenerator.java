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
package com.esofthead.mycollab.schedule.email.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Deprecated
public class ProjectMailLinkGenerator {
	private static Logger log = LoggerFactory
			.getLogger(ProjectMailLinkGenerator.class);

	private int projectId;
	private String siteUrl;

	public ProjectMailLinkGenerator(int projectId) {
		this.projectId = projectId;

		ProjectService projectService = ApplicationContextUtil
				.getSpringBean(ProjectService.class);
		String subdomain = projectService.getSubdomainOfProject(projectId);
		if (subdomain != null) {
			siteUrl = SiteConfiguration.getSiteUrl(subdomain);
		} else {
			log.error("Can not find subdomain for projectid {}", projectId);
			siteUrl = "";
		}
	}

	public String generateProjectFullLink() {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ "project/dashboard/" + UrlEncodeDecoder.encode(projectId);
	}

	public String generateMessagePreviewFullLink(Integer messageId) {
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateMessagePreviewLink(projectId,
						messageId);
	}

	public String generateBugPreviewFullLink(Integer bugId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateBugPreviewLink(projectId, bugId);
	}

	public String generateMilestonePreviewFullLink(Integer milestoneId) {
		if (milestoneId == null) {
			return "";
		}
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateMilestonePreviewLink(projectId,
						milestoneId);
	}

	public String generateTaskPreviewFullLink(Integer taskId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateTaskPreviewLink(projectId, taskId);
	}

	public String generateTaskGroupPreviewFullLink(Integer taskgroupId) {
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateTaskGroupPreviewLink(projectId,
						taskgroupId);
	}

	public String generateRiskPreviewFullLink(Integer riskId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateRiskPreviewLink(projectId, riskId);
	}

	public String generateProblemPreviewFullLink(Integer problemId) {
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateProblemPreviewLink(projectId,
						problemId);
	}

	public String generateBugComponentPreviewFullLink(Integer bugComponentId) {
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateBugComponentPreviewLink(projectId,
						bugComponentId);
	}

	public String generateBugVersionPreviewFullLink(Integer bugVersionId) {
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateBugVersionPreviewLink(projectId,
						bugVersionId);
	}
}
