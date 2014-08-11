package com.esofthead.mycollab.module.project.view.page;

import java.util.List;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.wiki.domain.WikiResource;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class PageListPresenter extends AbstractPresenter<PageListView> {
	private static final long serialVersionUID = 1L;

	private WikiService wikiService;

	public PageListPresenter() {
		super(PageListView.class);

		wikiService = ApplicationContextUtil.getSpringBean(WikiService.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.PAGES)) {
			PageContainer pageContainer = (PageContainer) container;
			pageContainer.removeAllComponents();
			pageContainer.addComponent(view.getWidget());

			List<WikiResource> resources = wikiService
					.getResources(CurrentProjectVariables.getCurrentPagePath());
			view.displayPages(resources);

			ProjectBreadcrumb breadcrumb = ViewManager
					.getView(ProjectBreadcrumb.class);
			breadcrumb.gotoPageList();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

}
