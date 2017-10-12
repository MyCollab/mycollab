/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
