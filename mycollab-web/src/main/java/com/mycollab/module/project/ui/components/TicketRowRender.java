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
package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.vaadin.ui.UIUtils;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketRowRender extends MVerticalLayout {
    protected ProjectTicket ticket;

    public void selfRemoved() {
        IBlockContainer blockContainer = UIUtils.getRoot(this, IBlockContainer.class);
        ComponentContainer container = (ComponentContainer) getParent();
        if (container != null) {
            container.removeComponent(this);
        }
        if (blockContainer != null) {
            blockContainer.refresh();
        }
    }

    final public ProjectTicket getTicket() {
        return ticket;
    }
}
