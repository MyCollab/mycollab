/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.ui.format;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class MilestoneHistoryFieldFormat implements HistoryFieldFormat {

	private static final Logger LOG = LoggerFactory
			.getLogger(MilestoneHistoryFieldFormat.class);

	@Override
	public Component toVaadinComponent(String value) {
		try {
			int milestoneId = Integer.parseInt(value);
			String html = ProjectLinkBuilder
					.generateMilestoneHtmlLink(milestoneId);
			return (value != null) ? new Label(html, ContentMode.HTML)
					: new Label("");
		} catch (NumberFormatException e) {
			return new Label("");
		}
	}

	@Override
	public String toString(String value) {
		if (StringUtils.isBlank(value)) {
			return new Span().write();
		}

		try {
			int milestoneId = Integer.parseInt(value);
			MilestoneService milestoneService = ApplicationContextUtil
					.getSpringBean(MilestoneService.class);
			SimpleMilestone milestone = milestoneService.findById(milestoneId,
					AppContext.getAccountId());

			if (milestone != null) {
				String milestoneIconLink = ProjectResources
						.getResourceLink(ProjectTypeConstants.MILESTONE);
				Img img = TagBuilder.newImg("icon", milestoneIconLink);

				String milestoneLink = ProjectLinkGenerator
						.generateMilestonePreviewFullLink(
								AppContext.getSiteUrl(),
								milestone.getProjectid(), milestone.getId());
				A link = TagBuilder.newA(milestoneLink, milestone.getName());
				return TagBuilder.newLink(img, link).write();
			}
		} catch (Exception e) {
			LOG.error("Error", e);
		}

		return value;
	}

}
