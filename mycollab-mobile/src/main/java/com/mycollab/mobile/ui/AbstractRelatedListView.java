/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.ui;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.ui.RelatedListHandler;
import com.vaadin.ui.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class AbstractRelatedListView<T, S extends SearchCriteria> extends AbstractMobilePageView implements IRelatedListHandlers<T> {
    private static final long serialVersionUID = 1L;

    protected Set<RelatedListHandler<T>> handlers;
    protected AbstractPagedBeanList<S, T> itemList;

    public AbstractRelatedListView() {
        this.setWidth("100%");

        Component rightComponent = createRightComponent();
        if (rightComponent != null) {
            setRightComponent(rightComponent);
        }
    }

    abstract protected Component createRightComponent();

    @Override
    public void addRelatedListHandler(final RelatedListHandler<T> handler) {
        if (handlers == null) {
            handlers = new HashSet<>();
        }

        handlers.add(handler);
    }

    protected void fireNewRelatedItem(final String itemId) {
        if (handlers != null) {
            for (final RelatedListHandler handler : handlers) {
                handler.createNewRelatedItem(itemId);
            }
        }
    }

    public void fireSelectedRelatedItems(Set<T> selectedItems) {
        if (handlers != null) {
            for (RelatedListHandler<T> handler : handlers) {
                handler.selectAssociateItems(selectedItems);
            }
        }
    }

    public void setSearchCriteria(final S criteria) {
        itemList.search(criteria);
    }

    public void setSelectedItems(final Set<T> selectedItems) {
        throw new MyCollabException("Must be override by support class");
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        refresh();
    }

    public AbstractPagedBeanList<S, T> getItemList() {
        return this.itemList;
    }
}
