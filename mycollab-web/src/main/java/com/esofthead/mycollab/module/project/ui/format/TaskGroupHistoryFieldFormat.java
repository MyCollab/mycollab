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

import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.html.FormatUtils;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.hp.gagawa.java.elements.Text;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
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
public class TaskGroupHistoryFieldFormat implements HistoryFieldFormat {

	private static final Logger LOG = LoggerFactory
			.getLogger(TaskGroupHistoryFieldFormat.class);

	@Override
	public Component toVaadinComponent(String value) {
		try {
			int taskgroupId = Integer.parseInt(value);
			String html = ProjectLinkBuilder
					.generateTaskGroupHtmlLink(taskgroupId);
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
			int taskgroupId = Integer.parseInt(value);
			ProjectTaskListService tasklistService = ApplicationContextUtil
					.getSpringBean(ProjectTaskListService.class);
			SimpleTaskList taskgroup = tasklistService.findById(taskgroupId,
					AppContext.getAccountId());
			if (taskgroup != null) {
				Text img = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK_LIST).getHtml());

				String taskgroupLink = ProjectLinkGenerator
						.generateTaskGroupPreviewFullLink(
								AppContext.getSiteUrl(),
								taskgroup.getProjectid(), taskgroup.getId());
				A link = FormatUtils.newA(taskgroupLink, taskgroup.getName());
				return FormatUtils.newLink(img, link).write();
			}
		} catch (Exception e) {
			LOG.error("Error", e);
		}

		return value;
	}

}
