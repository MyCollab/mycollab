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

package com.esofthead.mycollab.module.crm.ui.components;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.ui.RelatedListHandler;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class RelatedListComp<T, S extends SearchCriteria> extends
        MVerticalLayout implements IRelatedListHandlers<T> {

    private static final long serialVersionUID = 1L;

    protected Set<RelatedListHandler<T>> handlers;
    protected IPagedBeanTable<S, T> tableItem;

    public RelatedListComp() {
        this.setWidth("100%");
    }

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

    public void setSearchCriteria(final S criteria) {
        tableItem.setSearchCriteria(criteria);
    }
}
