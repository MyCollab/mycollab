/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.events.CrmEvent;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.IListView;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @param <V>
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class CrmGenericListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean>
        extends ListSelectionPresenter<V, S, B> {
    private static final long serialVersionUID = 1L;

    private PageView candidateView;

    private Class<? extends PageView> noItemFallbackViewClass;

    public CrmGenericListPresenter(Class<V> viewClass) {
        this(viewClass, null);
    }

    public CrmGenericListPresenter(Class<V> viewClass, Class<? extends PageView> noItemFallbackViewClass) {
        super(viewClass);
        this.noItemFallbackViewClass = noItemFallbackViewClass;
    }

    @Override
    public V getView() {
        super.getView();
        this.candidateView = view;
        return view;
    }

    public void displayListView(ComponentContainer container, ScreenData<?> data) {
        this.candidateView = view;
        displayView(container);
    }

    public void displayNoExistItems(ComponentContainer container, ScreenData<?> data) {
        this.candidateView = ViewManager.getCacheComponent(noItemFallbackViewClass);
        displayView(container);
    }

    private void displayView(ComponentContainer container) {
        CrmModule crmModule = (CrmModule) container;
        crmModule.addView(candidateView);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);
        EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
    }
}
