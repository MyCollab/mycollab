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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectLinkParams;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskUrlResolver extends ProjectUrlResolver {
	private static final Logger LOG = LoggerFactory.getLogger(TaskUrlResolver.class);

	public TaskUrlResolver() {
		this.addSubResolver("preview", new ReadUrlResolver());
		this.addSubResolver("add", new AddUrlResolver());
		this.addSubResolver("edit", new EditUrlResolver());
	}

	private static class ReadUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int projectId, taskId;

			if (ProjectLinkParams.isValidParam(params[0])) {
				String prjShortName = ProjectLinkParams.getProjectShortName(params[0]);
				int itemKey = ProjectLinkParams.getItemKey(params[0]);
				ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
				SimpleTask task = taskService.findByProjectAndTaskKey(itemKey,
						prjShortName, AppContext.getAccountId());

				if (task != null) {
					projectId = task.getProjectid();
					taskId = task.getId();
				} else {
					throw new ResourceNotFoundException(
							String.format("Can not find task with itemKey %d and project %s", itemKey, prjShortName));
				}
			} else {
				LOG.error(String.format("Should not call this. Fall back function: %s", params[0]));
				UrlTokenizer tokenizer = new UrlTokenizer(params[0]);
				projectId = tokenizer.getInt();
				taskId = tokenizer.getInt();
			}

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new TaskScreenData.Read(taskId));
			EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class EditUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			SimpleTask task;
			ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);

			if (ProjectLinkParams.isValidParam(params[0])) {
				String prjShortName = ProjectLinkParams.getProjectShortName(params[0]);
				int itemKey = ProjectLinkParams.getItemKey(params[0]);

				task = taskService.findByProjectAndTaskKey(itemKey, prjShortName, AppContext.getAccountId());
			} else {
				throw new MyCollabException("Can not find task link "
						+ params[0]);
			}

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(task.getProjectid()),
					new TaskScreenData.Edit(task));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class AddUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int projectId = new UrlTokenizer(params[0]).getInt();

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new TaskScreenData.Add(new SimpleTask()));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}
}
