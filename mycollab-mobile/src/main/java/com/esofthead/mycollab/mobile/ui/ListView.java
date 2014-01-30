package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.PageView;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <S>
 * @param <B>
 */
public interface ListView<S extends SearchCriteria, B> extends PageView {

	AbstractPagedBeanList<S, B> getPagedBeanTable();
}
