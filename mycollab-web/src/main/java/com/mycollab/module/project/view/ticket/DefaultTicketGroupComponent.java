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
package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.ui.components.IBlockContainer;
import com.mycollab.module.project.ui.components.IGroupComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
class DefaultTicketGroupComponent extends MVerticalLayout implements IGroupComponent, IBlockContainer {
    private Label headerLbl, estimatedHours, remainHours;
    private DDVerticalLayout wrapBody;

    private String titleValue;

    DefaultTicketGroupComponent(String titleValue) {
        this.titleValue = titleValue;
        this.setMargin(new MarginInfo(true, false, true, false));
        wrapBody = new DDVerticalLayout();
        wrapBody.setWidth("100%");
        wrapBody.addStyleName(WebThemes.BORDER_LIST);
        headerLbl = ELabel.h3("");
        this.with(headerLbl, wrapBody);
        refresh();
    }

    @Override
    public void refresh() {
        if (wrapBody.getComponentCount() > 0) {
            updateTitle();
        } else {
            ComponentContainer parent = (ComponentContainer) getParent();
            if (parent != null) {
                parent.removeComponent(this);
            }
        }
    }

    void insertTicket(ProjectTicket ticket) {
        wrapBody.addComponent(new TicketRowRenderer(ticket));
        updateTitle();
    }

    private void updateTitle() {
        headerLbl.setValue(String.format("%s (%d)", titleValue, wrapBody.getComponentCount()));
    }

    private void updateHours(ProjectTicket ticket) {

    }
}
