package com.esofthead.mycollab.module.crm.ui.components;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanBlockList;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.ui.RelatedListHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <SearchService>
 * @param <S>
 * @param <T>
 */
public abstract class RelatedListComp2<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
		extends AbstractBeanBlockList<S, T> implements IRelatedListHandlers<T> {
	private static final long serialVersionUID = -5854451222908978059L;
	private final SearchService searchService;

	protected Set<RelatedListHandler<T>> handlers;

	public RelatedListComp2(SearchService searchService,
			int defaultNumberSearchItems) {
		super(defaultNumberSearchItems);
		this.searchService = searchService;
	}

	public RelatedListComp2(SearchService searchService,
			BlockDisplayHandler<T> blockDisplayHandler,
			int defaultNumberSearchItems) {
		super(blockDisplayHandler, defaultNumberSearchItems);
		this.searchService = searchService;
	}

	@Override
	protected int queryTotalCount() {
		return searchService.getTotalCount(searchRequest.getSearchCriteria());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<T> queryCurrentData() {
		return searchService.findPagableListByCriteria(searchRequest);
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

	public void setSelectedItems(final Set<T> selectedItems) {
		throw new MyCollabException("Must be override by support class");
	}

}
