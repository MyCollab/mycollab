package com.mycollab.module.crm.ui.components;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.ui.RelatedListHandler;
import com.mycollab.vaadin.web.ui.table.IPagedBeanTable;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class RelatedListComp<T, S extends SearchCriteria> extends MVerticalLayout implements IRelatedListHandlers<T> {
    private static final long serialVersionUID = 1L;

    protected Set<RelatedListHandler<T>> handlers;
    protected IPagedBeanTable<S, T> tableItem;

    public RelatedListComp() {
        this.setWidth("100%");
    }

    @Override
    public void addRelatedListHandler(final RelatedListHandler<T> handler) {
        if (handlers == null) {
            handlers = new HashSet<>();
        }

        handlers.add(handler);
    }

    protected void fireNewRelatedItem(final String itemId) {
        if (handlers != null) {
            handlers.forEach(handler -> handler.createNewRelatedItem(itemId));
        }
    }

    public void setSearchCriteria(final S criteria) {
        tableItem.setSearchCriteria(criteria);
    }
}
