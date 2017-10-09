package com.mycollab.vaadin.ui;

import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public interface IBeanList<T> {

    interface RowDisplayHandler<T> {
        Component generateRow(IBeanList<T> host, T item, int rowIndex);
    }
}
