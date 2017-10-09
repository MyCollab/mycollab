package com.mycollab.mobile.module.project.view;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.mobile.ui.IListView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public abstract class ProjectListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean> extends AbstractPresenter<V> {
    private static final long serialVersionUID = -2202567598255893303L;

    protected S searchCriteria;

    public ProjectListPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        NavigationManager currentNav = (NavigationManager) container;
        this.searchCriteria = (S) data.getParams();
        getView().getPagedBeanTable().setSearchCriteria(searchCriteria);
        currentNav.navigateTo(getView());
    }
}
