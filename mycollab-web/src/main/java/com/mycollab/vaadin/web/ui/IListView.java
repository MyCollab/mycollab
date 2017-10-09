package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.event.HasMassItemActionHandler;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.mycollab.vaadin.event.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.web.ui.table.IPagedBeanTable;

/**
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface IListView<S extends SearchCriteria, B> extends PageView {
    void enableActionControls(int numOfSelectedItem);

    void disableActionControls();

    HasSearchHandlers<S> getSearchHandlers();

    HasSelectionOptionHandlers getOptionSelectionHandlers();

    HasMassItemActionHandler getPopupActionHandlers();

    HasSelectableItemHandlers<B> getSelectableItemHandlers();

    IPagedBeanTable<S, B> getPagedBeanTable();

    void showNoItemView();
}
