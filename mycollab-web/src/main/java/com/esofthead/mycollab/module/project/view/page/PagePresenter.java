package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.parameters.PageScreenData;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class PagePresenter extends AbstractPresenter<PageContainer> {
	private static final long serialVersionUID = 1L;

	public PagePresenter() {
		super(PageContainer.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		ProjectView projectViewContainer = (ProjectView) container;
		projectViewContainer.gotoSubView("page");

		AbstractPresenter presenter = null;
		if (data instanceof PageScreenData.Search) {
			presenter = PresenterResolver.getPresenter(PageListPresenter.class);
		} else if (data instanceof PageScreenData.Add
				| data instanceof PageScreenData.Edit) {
			presenter = PresenterResolver.getPresenter(PageAddPresenter.class);
		} else if (data instanceof PageScreenData.Read) {
			presenter = PresenterResolver.getPresenter(PageReadPresenter.class);
		} else {
			throw new MyCollabException("Do not support screen data " + data);
		}

		presenter.go(view, data);
	}

}
