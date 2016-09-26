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
import com.mycollab.module.project.ui.components.IGroupComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
class DefaultTicketGroupComponent extends MVerticalLayout implements IGroupComponent {
    private Label headerLbl;
    private CssLayout wrapBody;

    private String titleValue;
    private int numElements = 0;

    DefaultTicketGroupComponent(String titleValue) {
        this.titleValue = titleValue;
        initComponent();
    }

    private void initComponent() {
        this.setMargin(new MarginInfo(true, false, true, false));
        wrapBody = new CssLayout();
        wrapBody.setWidth("100%");
        wrapBody.addStyleName(WebUIConstants.BORDER_LIST);
        headerLbl = ELabel.h3("");
        this.addComponent(headerLbl);
        this.addComponent(wrapBody);
        updateHeader();
    }

    private void updateHeader() {
        headerLbl.setValue(String.format("%s (%d)", titleValue, numElements));
    }

    void insertTicket(ProjectTicket ticket) {
        wrapBody.addComponent(new TicketRowRenderer(ticket));
        numElements++;
        updateHeader();
    }
}
