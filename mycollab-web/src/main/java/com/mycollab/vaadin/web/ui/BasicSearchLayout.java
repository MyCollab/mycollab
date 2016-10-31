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
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
abstract public class BasicSearchLayout<S extends SearchCriteria> extends SearchLayout<S> {
    private static final long serialVersionUID = 1L;
    protected ComponentContainer header;
    protected ComponentContainer body;

    public BasicSearchLayout(DefaultGenericSearchPanel<S> parent) {
        super(parent, "basicSearch");
        this.initLayout();
    }

    private void initLayout() {
        header = this.constructHeader();
        body = this.constructBody();
        if (header != null) {
            this.addComponent(header, "basicSearchHeader");
        }

        this.addComponent(body, "basicSearchBody");
    }

    @Override
    protected void addHeaderRight(Component c) {
        if (header == null)
            return;

        header.addComponent(c);
    }

    private ComponentContainer constructHeader() {
        return ((DefaultGenericSearchPanel)searchPanel).constructHeader();
    }

    abstract public ComponentContainer constructBody();
}
