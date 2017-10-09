package com.mycollab.mobile.ui;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.ui.RelatedListHandler;
import com.vaadin.ui.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class AbstractRelatedListView<T, S extends SearchCriteria> extends AbstractMobilePageView implements IRelatedListHandlers<T> {
    private static final long serialVersionUID = 1L;

    protected Set<RelatedListHandler<T>> handlers;
    protected AbstractPagedBeanList<S, T> itemList;

    public AbstractRelatedListView() {
        this.setWidth("100%");

        Component rightComponent = createRightComponent();
        if (rightComponent != null) {
            setRightComponent(rightComponent);
        }
    }

    abstract protected Component createRightComponent();

    @Override
    public void addRelatedListHandler(final RelatedListHandler<T> handler) {
        if (handlers == null) {
            handlers = new HashSet<>();
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

    public void fireSelectedRelatedItems(Set<T> selectedItems) {
        if (handlers != null) {
            for (RelatedListHandler<T> handler : handlers) {
                handler.selectAssociateItems(selectedItems);
            }
        }
    }

    public void setSearchCriteria(final S criteria) {
        itemList.search(criteria);
    }

    public void setSelectedItems(final Set<T> selectedItems) {
        throw new MyCollabException("Must be override by support class");
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        refresh();
    }

    public AbstractPagedBeanList<S, T> getItemList() {
        return this.itemList;
    }
}
