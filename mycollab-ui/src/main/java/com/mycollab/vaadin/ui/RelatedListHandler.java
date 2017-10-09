package com.mycollab.vaadin.ui;

import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface RelatedListHandler<T> {

    void createNewRelatedItem(String itemId);

    void selectAssociateItems(Set<T> items);
}
