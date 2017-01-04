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
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.components.IBlockContainer;
import com.mycollab.module.project.ui.components.IGroupComponent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.5
 */
class MilestoneTicketGroupComponent extends MVerticalLayout implements IGroupComponent, IBlockContainer {
    private Label headerLbl;
    private DDVerticalLayout wrapBody;

    private String titleValue;
    private Integer milestoneId;

    MilestoneTicketGroupComponent(String titleValue, Integer milestoneId) {
        this.titleValue = titleValue;
        this.milestoneId = milestoneId;
        this.setMargin(new MarginInfo(true, false, true, false));
        wrapBody = new DDVerticalLayout();
        wrapBody.setWidth("100%");
        wrapBody.addStyleName(WebThemes.BORDER_LIST);
        headerLbl = ELabel.h3("");
        with(headerLbl);
        addComponent(wrapBody);

        wrapBody.setComponentVerticalDropRatio(0.3f);
        wrapBody.setDragMode(LayoutDragMode.CLONE);
        wrapBody.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();

                DDVerticalLayout.VerticalLayoutTargetDetails details = (DDVerticalLayout.VerticalLayoutTargetDetails) event
                        .getTargetDetails();

                Component dragComponent = transferable.getComponent();
                if (dragComponent instanceof TicketRowRenderer) {
                    TicketRowRenderer ticketRowRenderer = (TicketRowRenderer) dragComponent;
                    MilestoneTicketGroupComponent originalMilestoneContainer = UIUtils.getRoot(ticketRowRenderer,
                            MilestoneTicketGroupComponent.class);
                    ProjectTicket ticket = ticketRowRenderer.getTicket();
                    ticket.setMilestoneId(milestoneId);
                    AppContextUtil.getSpringBean(ProjectTicketService.class).updateTicket(ticket, UserUIContext.getUsername());
                    wrapBody.addComponent(ticketRowRenderer);
                    updateTitle();
                    if (originalMilestoneContainer != null) {
                        originalMilestoneContainer.updateTitle();
                    }
                }
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new Not(VerticalLocationIs.MIDDLE);
            }
        });
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
        TicketRowRenderer ticketRowRenderer = new TicketRowRenderer(ticket);
        ticketRowRenderer.addStyleName("cursor_move");
        wrapBody.addComponent(ticketRowRenderer);
        updateTitle();
    }

    private void updateTitle() {
        headerLbl.setValue(String.format("%s (%d)", titleValue, wrapBody.getComponentCount()));
    }
}
