package com.esofthead.mycollab.vaadin.web.ui;

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

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 * @param <S>
 * @param <B>
 */
public interface ListView<S extends SearchCriteria, B> extends PageView {
	void enableActionControls(int numOfSelectedItem);

	void disableActionControls();

	HasSearchHandlers<S> getSearchHandlers();

	HasSelectionOptionHandlers getOptionSelectionHandlers();

	HasMassItemActionHandler getPopupActionHandlers();

	HasSelectableItemHandlers<B> getSelectableItemHandlers();

	AbstractPagedBeanTable<S, B> getPagedBeanTable();
}
