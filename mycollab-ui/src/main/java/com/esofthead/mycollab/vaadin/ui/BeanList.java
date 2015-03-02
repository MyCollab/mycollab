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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

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

	private static final Logger LOG = LoggerFactory.getLogger(BeanList.class);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private RowDisplayHandler<T> constructRowDisplayHandler() {
		RowDisplayHandler<T> rowHandler;
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
		SearchRequest<S> searchRequest = new SearchRequest<>(searchCriteria,
				0, Integer.MAX_VALUE);
		return setSearchRequest(searchRequest);
	}

	@SuppressWarnings("unchecked")
	public int setSearchRequest(SearchRequest<S> searchRequest) {
		List<T> currentListData = searchService
				.findPagableListByCriteria(searchRequest);
		loadItems(currentListData);
		return currentListData.size();
	}

	public int getTotalCount(S searchCriteria) {
		return searchService.getTotalCount(searchCriteria);
	}

	public void loadItems(List<T> currentListData) {
		contentLayout.removeAllComponents();

		try {
			if (CollectionUtils.isEmpty(currentListData)
					&& isDisplayEmptyListText) {
				Label noItemLbl = new Label(
						AppContext.getMessage(GenericI18Enum.EXT_NO_ITEM));
				final MVerticalLayout widgetFooter = new MVerticalLayout().withWidth("100%");
				widgetFooter.addStyleName("widget-footer");
				widgetFooter.with(noItemLbl).withAlign(noItemLbl, Alignment.MIDDLE_CENTER);
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
			LOG.error("Error while generate column display", e);
		}
	}

	/**
	 * 
	 * @author MyCollab Ltd.
	 * @since 1.0
	 * 
	 * @param <T>
	 */
	@SuppressWarnings("rawtypes")
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
