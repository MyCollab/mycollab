package com.mycollab.vaadin.ui;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface IRelatedListHandlers<T> {

    void addRelatedListHandler(RelatedListHandler<T> handler);

    void refresh();
}
