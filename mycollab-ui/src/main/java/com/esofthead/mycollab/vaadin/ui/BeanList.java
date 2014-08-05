/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * Generic list item
 * 
 * @param <SearchService>
 *            search service generic interface
 * @param <S>
 *            search criteria
 * @param <T>
 *            bean item
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class BeanList<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
		extends CustomComponent {

	private static Logger log = LoggerFactory.getLogger(BeanList.class);
	private static final long serialVersionUID = 1L;

	protected SearchService searchService;

	private Object parentComponent;
	private Class<? extends RowDisplayHandler<T>> rowDisplayHandler;
	private Layout contentLayout;
	private boolean isDisplayEmptyListText = true;

	public BeanList(Object parentComponent, SearchService searchService,
			Class<? extends RowDisplayHandler<T>> rowDisplayHandler) {
		this(parentComponent, searchService, rowDisplayHandler, null);
	}

	public BeanList(Object parentComponent, SearchService searchService,
			Class<? extends RowDisplayHandler<T>> rowDisplayHandler,
			Layout contentLayout) {
		this.parentComponent = parentComponent;
		this.searchService = searchService;
		this.rowDisplayHandler = rowDisplayHandler;

		if (contentLayout != null) {
			this.contentLayout = contentLayout;

		} else {
			this.contentLayout = new CssLayout();
			this.contentLayout.setWidth("100%");
		}

		this.setCompositionRoot(this.contentLayout);

		this.setStyleName("bean-list");
	}

	public Layout getContentLayout() {
		return this.contentLayout;
	}

	public BeanList(SearchService searchService,
			Class<? extends RowDisplayHandler<T>> rowDisplayHandler) {
		this(null, searchService, rowDisplayHandler);
	}

	public void setDisplayEmptyListText(boolean isDisplayEmptyListText) {
		this.isDisplayEmptyListText = isDisplayEmptyListText;
	}

	public void removeRow(Component row) {
		contentLayout.removeComponent(row);
	}

	public void insetItemOnBottom(T item) {
		RowDisplayHandler<T> rowHandler = constructRowDisplayHandler();
		Component row = rowHandler.generateRow(item,
				contentLayout.getComponentCount());
		if (row != null && contentLayout != null) {
			contentLayout.addComponent(row);
		}
	}

	private RowDisplayHandler<T> constructRowDisplayHandler() {
		RowDisplayHandler<T> rowHandler = null;
		try {

			if (rowDisplayHandler.getEnclosingClass() != null
					&& !Modifier.isStatic(rowDisplayHandler.getModifiers())) {

				Constructor constructor = rowDisplayHandler
						.getDeclaredConstructor(rowDisplayHandler
								.getEnclosingClass());
				rowHandler = (RowDisplayHandler<T>) constructor
						.newInstance(parentComponent);
			} else {
				rowHandler = rowDisplayHandler.newInstance();
			}
			rowHandler.setOwner(this);
			return rowHandler;
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	public int setSearchCriteria(S searchCriteria) {
		SearchRequest<S> searchRequest = new SearchRequest<S>(searchCriteria,
				0, Integer.MAX_VALUE);
		return setSearchRequest(searchRequest);
	}

	public int setSearchRequest(SearchRequest<S> searchRequest) {
		List<T> currentListData = searchService
				.findPagableListByCriteria(searchRequest);
		loadItems(currentListData);
		return currentListData.size();
	}

	public void loadItems(List<T> currentListData) {
		contentLayout.removeAllComponents();

		try {
			if ((currentListData == null || currentListData.size() == 0)
					&& isDisplayEmptyListText) {
				Label noItemLbl = new Label(
						AppContext.getMessage(GenericI18Enum.EXT_NO_ITEM));
				final VerticalLayout widgetFooter = new VerticalLayout();
				widgetFooter.addStyleName("widget-footer");
				widgetFooter.setWidth("100%");
				widgetFooter.addComponent(noItemLbl);
				widgetFooter.setComponentAlignment(noItemLbl,
						Alignment.MIDDLE_CENTER);
				contentLayout.addComponent(widgetFooter);
			} else {
				int i = 0;
				for (T item : currentListData) {
					RowDisplayHandler<T> rowHandler = constructRowDisplayHandler();

					Component row = rowHandler.generateRow(item, i);
					if (row != null) {
						row.setWidth("100%");
						contentLayout.addComponent(row);
					}

					i++;
				}
			}

		} catch (Exception e) {
			log.error("Error while generate column display", e);
		}
	}

	/**
	 * 
	 * @author MyCollab Ltd.
	 * @since 1.0
	 * 
	 * @param <T>
	 */
	public static abstract class RowDisplayHandler<T> implements Serializable {
		private static final long serialVersionUID = 1L;

		protected BeanList owner;

		public BeanList getOwner() {
			return owner;
		}

		private void setOwner(BeanList owner) {
			this.owner = owner;
		}

		public abstract Component generateRow(T obj, int rowIndex);
	}
}
