package com.esofthead.mycollab.mobile.module.crm.ui;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.AbstractMobilePageView;
import com.esofthead.mycollab.vaadin.ui.IPagedBeanList;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.ui.RelatedListHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class AbstractRelatedListView<T, S extends SearchCriteria> extends AbstractMobilePageView implements IRelatedListHandlers<T> {

	private static final long serialVersionUID = 1L;

	protected Set<RelatedListHandler<T>> handlers;
	protected IPagedBeanList<S, T> tableItem;

	public AbstractRelatedListView() {
		this.setWidth("100%");
	}

	@Override
	public void addRelatedListHandler(final RelatedListHandler<T> handler) {
		if (handlers == null) {
			handlers = new HashSet<RelatedListHandler<T>>();
		}

		handlers.add(handler);
	}

	protected void fireNewRelatedItem(final String itemId) {
		if (handlers != null) {
			for (final RelatedListHandler handler : handlers) {
				handler.createNewRelatedItem(itemId);
			}
		}
	}

	protected void fireSelectedRelatedItems(final Set selectedItems) {
		if (handlers != null) {
			for (final RelatedListHandler handler : handlers) {
				handler.selectAssociateItems(selectedItems);
			}
		}
	}

	public void setSearchCriteria(final S criteria) {
		tableItem.setSearchCriteria(criteria);
	}

	public void setSelectedItems(final Set<T> selectedItems) {
		throw new MyCollabException("Must be override by support class");
	}
}
