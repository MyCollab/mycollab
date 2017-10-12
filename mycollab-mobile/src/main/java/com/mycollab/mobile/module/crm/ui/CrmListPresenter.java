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
package com.mycollab.mobile.module.crm.ui;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.mobile.ui.AbstractListPresenter;
import com.mycollab.mobile.ui.AbstractMobileTabPageView;
import com.mycollab.mobile.ui.IListView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * @param <V>
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class CrmListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean> extends AbstractListPresenter<V, S, B> {
    private static final long serialVersionUID = -8215491621059981765L;

    public CrmListPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        Component targetView;
        NavigationManager currentNav = (NavigationManager) container;

        if (getView().getParent() != null && getView().getParent() instanceof AbstractMobileTabPageView) {
            targetView = getView().getParent();
            ((AbstractMobileTabPageView) getView().getParent()).setSelectedTab(getView());
        } else {
            targetView = getView();
        }

        currentNav.navigateTo(targetView);
        doSearch((S) data.getParams());
    }
}
