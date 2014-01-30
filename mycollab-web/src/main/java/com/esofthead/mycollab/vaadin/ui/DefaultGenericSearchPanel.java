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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 * @param <S>
 */
public abstract class DefaultGenericSearchPanel<S extends SearchCriteria>
		extends GenericSearchPanel<S> {
	private static final long serialVersionUID = 1L;

	public DefaultGenericSearchPanel() {
		moveToBasicSearchLayout();
	}

	abstract protected SearchLayout<S> createBasicSearchLayout();

	abstract protected SearchLayout<S> createAdvancedSearchLayout();

	protected void moveToBasicSearchLayout() {
		SearchLayout<S> layout = createBasicSearchLayout();
		setCompositionRoot(layout);
	}

	protected void moveToAdvancedSearchLayout() {
		SearchLayout<S> layout = createAdvancedSearchLayout();
		setCompositionRoot(layout);
	}
}
