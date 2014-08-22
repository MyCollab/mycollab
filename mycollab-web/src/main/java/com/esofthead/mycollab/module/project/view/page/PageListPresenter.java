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

import java.util.List;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.wiki.domain.WikiResource;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
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

			String path = (String) data.getParams();
			if (path == null) {
				path = CurrentProjectVariables.getCurrentPagePath();
			} else {
				CurrentProjectVariables.setCurrentPagePath(path);
			}
			List<WikiResource> resources = wikiService.getResources(path,
					AppContext.getUsername());
			view.displayDefaultPages(resources);

			ProjectBreadcrumb breadcrumb = ViewManager
					.getCacheComponent(ProjectBreadcrumb.class);
			breadcrumb.gotoPageList();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

}
