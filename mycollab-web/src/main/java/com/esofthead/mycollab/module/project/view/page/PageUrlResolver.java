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
package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.common.InvalidTokenException;
import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.page.service.PageService;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.PageScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class PageUrlResolver extends ProjectUrlResolver {
	public PageUrlResolver() {
		this.addSubResolver("list", new ListUrlResolver());
		this.addSubResolver("add", new AddUrlResolver());
		this.addSubResolver("edit", new EditUrlResolver());
		this.addSubResolver("preview", new PreviewUrlResolver());
	}

	private static class ListUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			try {
				UrlTokenizer tokenizer = new UrlTokenizer(params[0]);

				int projectId = tokenizer.getInt();
				String pagePath = tokenizer.getRemainValue();

				PageActionChain chain = new PageActionChain(
						new ProjectScreenData.Goto(projectId),
						new PageScreenData.Search(pagePath));
				EventBusFactory.getInstance().post(
						new ProjectEvent.GotoMyProject(this, chain));
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
		}
	}

	private static class PreviewUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			try {
				UrlTokenizer tokenizer = new UrlTokenizer(params[0]);

				int projectId = tokenizer.getInt();
				String pagePath = tokenizer.getRemainValue();

				PageService pageService = ApplicationContextUtil
						.getSpringBean(PageService.class);
				Page page = pageService.getPage(pagePath,
						AppContext.getUsername());

				if (page != null) {
					PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(projectId),
							new PageScreenData.Read(page));
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(this, chain));
				} else {
					PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(projectId));
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(this, chain));
				}
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
		}
	}

	private static class EditUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			try {
				UrlTokenizer tokenizer = new UrlTokenizer(params[0]);

				int projectId = tokenizer.getInt();
				String pagePath = tokenizer.getRemainValue();

				PageService pageService = ApplicationContextUtil
						.getSpringBean(PageService.class);
				Page page = pageService.getPage(pagePath,
						AppContext.getUsername());

				if (page != null) {
					PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(projectId),
							new PageScreenData.Edit(page));
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(this, chain));
				} else {
					PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(projectId));
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(this, chain));
				}

			} catch (InvalidTokenException e) {
				throw new MyCollabException(e);
			}
		}
	}

	private static class AddUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			try {
				UrlTokenizer tokenizer = new UrlTokenizer(params[0]);

				int projectId = tokenizer.getInt();
				String pagePath = tokenizer.getRemainValue();

				Page page = new Page();
				page.setPath(pagePath + "/"
						+ StringUtils.generateSoftUniqueId());

				PageActionChain chain = new PageActionChain(
						new ProjectScreenData.Goto(projectId),
						new PageScreenData.Add(page));
				EventBusFactory.getInstance().post(
						new ProjectEvent.GotoMyProject(this, chain));
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
		}
	}
}
