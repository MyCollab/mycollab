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
package com.mycollab.module.crm.view;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.event.CrmEvent;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.IListView;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.vaadin.ui.HasComponents;

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

    public void displayListView(HasComponents container, ScreenData<?> data) {
        this.candidateView = view;
        displayView(container);
    }

    public void displayNoExistItems(HasComponents container, ScreenData<?> data) {
        this.candidateView = ViewManager.getCacheComponent(noItemFallbackViewClass);
        displayView(container);
    }

    private void displayView(HasComponents container) {
        CrmModule crmModule = (CrmModule) container;
        crmModule.setContent(candidateView);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);
        EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
    }
}
