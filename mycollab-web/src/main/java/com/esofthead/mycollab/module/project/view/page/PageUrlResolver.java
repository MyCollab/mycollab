package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.common.InvalidTokenException;
import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.PageScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
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

				WikiService wikiService = ApplicationContextUtil
						.getSpringBean(WikiService.class);
				Page page = wikiService.getPage(pagePath);

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

				WikiService wikiService = ApplicationContextUtil
						.getSpringBean(WikiService.class);
				Page page = wikiService.getPage(pagePath);

				if (page != null) {
					PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(projectId),
							new PageScreenData.Add(page));
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
