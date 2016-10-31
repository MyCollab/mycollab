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
package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
abstract public class SearchLayout<S extends SearchCriteria> extends CustomLayoutExt {
    protected GenericSearchPanel<S> searchPanel;

    public SearchLayout(GenericSearchPanel<S> parent, String layoutName) {
        super(layoutName);
        this.searchPanel = parent;
    }

    public void callSearchAction() {
        final S searchCriteria = this.fillUpSearchCriteria();
        searchPanel.notifySearchHandler(searchCriteria);
    }

    abstract protected S fillUpSearchCriteria();

    abstract protected void addHeaderRight(Component c);

}
