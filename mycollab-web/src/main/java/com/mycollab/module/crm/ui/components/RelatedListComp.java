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
package com.mycollab.module.crm.ui.components;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.ui.RelatedListHandler;
import com.mycollab.vaadin.web.ui.table.IPagedGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class RelatedListComp<T, S extends SearchCriteria> extends MVerticalLayout implements IRelatedListHandlers<T> {
    private static final long serialVersionUID = 1L;

    protected Set<RelatedListHandler<T>> handlers;
    protected IPagedGrid<S, T> tableItem;

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
            handlers.forEach(handler -> handler.createNewRelatedItem(itemId));
        }
    }

    public void setSearchCriteria(final S criteria) {
        tableItem.setSearchCriteria(criteria);
    }
}
