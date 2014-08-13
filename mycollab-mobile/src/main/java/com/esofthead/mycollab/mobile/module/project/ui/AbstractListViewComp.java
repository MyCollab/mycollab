package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.mobile.ui.AbstractMobileSwipeView;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.IListView;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public abstract class AbstractListViewComp<S extends SearchCriteria, B> extends
		AbstractMobileSwipeView implements IListView<S, B> {
	private static final long serialVersionUID = 3603608419228750094L;

	protected AbstractPagedBeanList<S, B> itemList;

	public AbstractListViewComp() {

		this.itemList = createBeanTable();

		setContent(itemList);

		Component rightComponent = createRightComponent();
		if (rightComponent != null) {
			setRightComponent(rightComponent);
		}
	}

	@Override
	public AbstractPagedBeanList<S, B> getPagedBeanTable() {
		return this.itemList;
	}

	@Override
	public void onBecomingVisible() {
		super.onBecomingVisible();

		if (getPagedBeanTable().getSearchRequest() != null)
			getPagedBeanTable().refresh();
	}

	abstract protected AbstractPagedBeanList<S, B> createBeanTable();

	abstract protected Component createRightComponent();
}
