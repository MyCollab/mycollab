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
package com.esofthead.mycollab.vaadin.ui.table;

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.vaadin.events.HasPagableHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public interface IPagedBeanTable<S extends SearchCriteria, T> extends
		HasSelectableItemHandlers<T>, HasPagableHandlers, Component, IBeanTable {

	void setSearchCriteria(S searchCriteria);

	List<T> getCurrentDataList();

	void addTableListener(
			ApplicationEventListener<? extends ApplicationEvent> listener);

	void addGeneratedColumn(Object id, Table.ColumnGenerator generatedColumn);

	T getBeanByIndex(Object itemId);

	void refresh();
}
