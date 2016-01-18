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
package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.IListView;
import com.esofthead.mycollab.mobile.ui.SearchInputField;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public abstract class AbstractListPageView<S extends SearchCriteria, B> extends ProjectMobileMenuPageView implements IListView<S, B> {
    private static final long serialVersionUID = 3603608419228750094L;

    protected AbstractPagedBeanList<S, B> itemList;
    protected SearchInputField searchInputField;

    public AbstractListPageView() {
        itemList = createBeanList();
        searchInputField = createSearchField();

        if (searchInputField != null) {
            VerticalLayout content = new VerticalLayout();
            content.addComponent(searchInputField);
            content.addComponent(itemList);
            content.setExpandRatio(itemList, 1.0f);
            setContent(content);
        } else {
            setContent(itemList);
        }
    }

    @Override
    public AbstractPagedBeanList<S, B> getPagedBeanTable() {
        return itemList;
    }

    @Override
    public HasSearchHandlers<S> getSearchHandlers() {
        return searchInputField;
    }

    protected void doSearch() {
        if (getPagedBeanTable().getSearchRequest() != null) {
            getPagedBeanTable().refresh();
        }
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        doSearch();

        Component rightComponent = buildRightComponent();
        if (rightComponent != null) {
            setRightComponent(rightComponent);
        }

        Component toolbar = buildToolbar();
        if (toolbar != null) {
            setToolbar(toolbar);
        }
    }

    abstract protected AbstractPagedBeanList<S, B> createBeanList();

    abstract protected SearchInputField<S> createSearchField();

    protected Component buildRightComponent() {
        return null;
    }

    protected Component buildToolbar() {
        return null;
    }
}
