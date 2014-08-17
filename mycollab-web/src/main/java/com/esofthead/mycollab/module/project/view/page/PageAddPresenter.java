package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class PageAddPresenter extends AbstractPresenter<PageAddView> {
	private static final long serialVersionUID = 1L;

	public PageAddPresenter() {
		super(PageAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(new EditFormHandler<Page>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSave(final Page page) {
				savePage(page);
				ViewState viewState = HistoryViewManager.back();
				if (viewState instanceof NullViewState) {
					EventBusFactory.getInstance().post(
							new PageEvent.GotoList(this, null));
				}
			}

			@Override
			public void onCancel() {
				ViewState viewState = HistoryViewManager.back();
				if (viewState instanceof NullViewState) {
					EventBusFactory.getInstance().post(
							new PageEvent.GotoList(this, null));
				}
			}

			@Override
			public void onSaveAndNew(final Page page) {
				savePage(page);
				EventBusFactory.getInstance().post(
						new PageEvent.GotoAdd(this, null));
			}
		});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.PAGES)) {
			PageContainer pageContainer = (PageContainer) container;
			pageContainer.removeAllComponents();
			pageContainer.addComponent(view.getWidget());

			Page page = (Page) data.getParams();
			view.editItem(page);

			ProjectBreadcrumb breadcrumb = ViewManager
					.getView(ProjectBreadcrumb.class);
			if (page.getPath().equals("")) {
				breadcrumb.gotoPageAdd();
			} else {
				breadcrumb.gotoPageEdit(page);
			}

		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	private void savePage(Page page) {
		WikiService wikiService = ApplicationContextUtil
				.getSpringBean(WikiService.class);

		wikiService.savePage(page, AppContext.getUsername());
		EventBusFactory.getInstance().post(
				new PageEvent.GotoAdd(PageAddPresenter.this, null));
	}
}
