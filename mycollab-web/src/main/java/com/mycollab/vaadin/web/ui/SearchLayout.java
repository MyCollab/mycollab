/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
abstract public class SearchLayout<S extends SearchCriteria> extends CustomComponent {
    protected DefaultGenericSearchPanel<S> searchPanel;

    public SearchLayout(DefaultGenericSearchPanel<S> parent) {
        this.searchPanel = parent;
    }

    public void callSearchAction() {
        final S searchCriteria = this.fillUpSearchCriteria();
        searchPanel.notifySearchHandler(searchCriteria);
    }

    abstract protected S fillUpSearchCriteria();

//    abstract protected void addHeaderRight(Component c);

}
