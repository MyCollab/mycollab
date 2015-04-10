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

import com.esofthead.mycollab.html.FormatUtils;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.hp.gagawa.java.elements.Text;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.service.MilestoneService;
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
			return new Label(html, ContentMode.HTML);
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
				return ProjectLinkBuilder.generateProjectItemLinkWithTooltip(CurrentProjectVariables.getShortName(),
						milestone.getProjectid(), milestone.getName(), ProjectTypeConstants.MILESTONE, milestone.getId() + "", milestone.getId() + "");
			}
		} catch (Exception e) {
			LOG.error("Error", e);
		}

		return value;
	}

}
