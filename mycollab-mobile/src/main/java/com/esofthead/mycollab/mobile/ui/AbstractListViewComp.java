package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.AbstractMobilePageView;
import com.vaadin.ui.CssLayout;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractListViewComp<S extends SearchCriteria, B> extends
		AbstractMobilePageView implements ListView<S, B> {
	private static final long serialVersionUID = 3603608419228750094L;

	protected CssLayout contentLayout;

	protected AbstractPagedBeanList<S, B> itemList;

	public AbstractListViewComp() {

		this.contentLayout = new CssLayout();
		this.contentLayout.setSizeFull();

		this.itemList = createBeanTable();
		this.contentLayout.addComponent(itemList);

		setContent(contentLayout);
	}

	@Override
	public AbstractPagedBeanList<S, B> getPagedBeanTable() {
		return this.itemList;
	}

	abstract protected AbstractPagedBeanList<S, B> createBeanTable();

}
