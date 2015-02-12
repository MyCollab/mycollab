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

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.vaadin.events.HasPagableHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.util.ReflectTools;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventListener;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public interface IPagedBeanTable<S extends SearchCriteria, T> extends
		HasSelectableItemHandlers<T>, HasPagableHandlers, Component, IBeanTable {

	void setSearchCriteria(S searchCriteria);

	Collection<T> getCurrentDataList();

	void addTableListener(TableClickListener listener);

	void addGeneratedColumn(Object id, Table.ColumnGenerator generatedColumn);

	T getBeanByIndex(Object itemId);

	void refresh();

	public static interface TableClickListener extends EventListener,
			Serializable {
		public static final Method itemClickMethod = ReflectTools.findMethod(
				TableClickListener.class, "itemClick", TableClickEvent.class);

		void itemClick(TableClickEvent event);
	}

	public static class TableClickEvent extends ApplicationEvent {
		public static final String TABLE_CLICK_IDENTIFIER = "tableClickEvent";

		private static final long serialVersionUID = 1L;
		private String fieldName;

		public TableClickEvent(IBeanTable source, Object data, String fieldName) {
			super(source, data);
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return fieldName;
		}
	}
}
