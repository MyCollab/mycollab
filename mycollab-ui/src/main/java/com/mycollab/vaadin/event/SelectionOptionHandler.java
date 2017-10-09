package com.mycollab.vaadin.event;

import java.io.Serializable;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface SelectionOptionHandler extends Serializable {
    void onSelectCurrentPage();

    void onSelectAll();

    void onDeSelect();
}
