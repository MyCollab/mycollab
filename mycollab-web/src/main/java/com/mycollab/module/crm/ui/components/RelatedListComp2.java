package com.mycollab.module.crm.ui.components;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.ui.RelatedListHandler;
import com.mycollab.vaadin.web.ui.AbstractBeanBlockList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @param <SearchService>
 * @param <S>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class RelatedListComp2<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
        extends AbstractBeanBlockList<S, T> implements IRelatedListHandlers<T> {
    private static final long serialVersionUID = -5854451222908978059L;
    private final SearchService searchService;

    protected Set<RelatedListHandler<T>> handlers;

    public RelatedListComp2(SearchService searchService, int defaultNumberSearchItems) {
        super(defaultNumberSearchItems);
        this.searchService = searchService;
    }

    public RelatedListComp2(SearchService searchService, BlockDisplayHandler<T> blockDisplayHandler, int defaultNumberSearchItems) {
        super(blockDisplayHandler, defaultNumberSearchItems);
        this.searchService = searchService;
    }

    @Override
    protected int queryTotalCount() {
        return searchService.getTotalCount(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<T> queryCurrentData() {
        return (List<T>) searchService.findPageableListByCriteria(searchRequest);
    }

    @Override
    public void addRelatedListHandler(final RelatedListHandler<T> handler) {
        if (handlers == null) {
            handlers = new HashSet<>();
        }

        handlers.add(handler);
    }

    protected void fireNewRelatedItem(String itemId) {
        if (handlers != null) {
            handlers.forEach(handler -> handler.createNewRelatedItem(itemId));
        }
    }

    public void fireSelectedRelatedItems(Set selectedItems) {
        if (handlers != null) {
            handlers.forEach(handler -> handler.selectAssociateItems(selectedItems));
        }
    }
}