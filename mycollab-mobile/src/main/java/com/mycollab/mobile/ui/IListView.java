package com.mycollab.mobile.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public interface IListView<S extends SearchCriteria, B> extends PageView {

    AbstractPagedBeanList<S, B> getPagedBeanTable();
}
