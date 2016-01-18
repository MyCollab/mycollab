/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.mobile.ui.IListView;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.ComponentContainer;

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
    protected void postInitView() {
        super.postInitView();
        HasSearchHandlers<S> searchHandlers = view.getSearchHandlers();
        if (searchHandlers != null) {
            searchHandlers.addSearchHandler(new SearchHandler<S>() {
                @Override
                public void onSearch(S criteria) {
                    searchCriteria = criteria;
                    view.getPagedBeanTable().search(criteria);
                }
            });
        }
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        NavigationManager currentNav = (NavigationManager) container;
        this.searchCriteria = (S) data.getParams();
        view.getPagedBeanTable().setSearchCriteria(searchCriteria);
        currentNav.navigateTo(view);
    }
}
