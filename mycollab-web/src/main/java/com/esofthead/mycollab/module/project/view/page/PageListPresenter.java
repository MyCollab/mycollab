package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.milestone.MilestoneContainer;
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

	public PageListPresenter() {
		super(PageListView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.PAGES)) {
			PageContainer pageContainer = (PageContainer) container;
			pageContainer.removeAllComponents();
			pageContainer.addComponent(view.getWidget());

			view.displayPages();

			ProjectBreadcrumb breadcrumb = ViewManager
					.getView(ProjectBreadcrumb.class);
			breadcrumb.gotoPageList();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

}
