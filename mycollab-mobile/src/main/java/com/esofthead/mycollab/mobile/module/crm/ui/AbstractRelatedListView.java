/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.ui;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.vaadin.mvp.AbstractMobilePageView;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.ui.RelatedListHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class AbstractRelatedListView<T, S extends SearchCriteria>
		extends AbstractMobilePageView implements IRelatedListHandlers<T> {

	private static final long serialVersionUID = 1L;

	protected Set<RelatedListHandler<T>> handlers;
	protected AbstractPagedBeanList<S, T> itemList;

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
		itemList.setSearchCriteria(criteria);
	}

	public void setSelectedItems(final Set<T> selectedItems) {
		throw new MyCollabException("Must be override by support class");
	}
}
