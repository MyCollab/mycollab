package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.components.IBlockContainer;
import com.mycollab.module.project.ui.components.IGroupComponent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
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
    private MVerticalLayout headerGroup;
    private Label headerLbl;
    private DDVerticalLayout wrapBody;

    private SimpleMilestone milestone;

    MilestoneTicketGroupComponent(Integer milestoneId) {
        this.setMargin(new MarginInfo(true, false, true, false));
        wrapBody = new DDVerticalLayout();
        wrapBody.setWidth("100%");
        wrapBody.addStyleName(WebThemes.BORDER_LIST);
        headerLbl = ELabel.h3("");

        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        milestone = milestoneService.findById(milestoneId, AppUI.getAccountId());
        if (milestone != null) {
            ELabel milestoneDateLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_FROM_TO,
                    UserUIContext.formatDate(milestone.getStartdate()), UserUIContext.formatDate(milestone.getEnddate())))
                    .withStyleName(UIConstants.META_INFO);
            headerGroup = new MVerticalLayout(headerLbl, milestoneDateLbl).withMargin(false);
        } else {
            headerGroup = new MVerticalLayout(headerLbl).withMargin(false);
        }

        with(headerGroup);
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
        String titleValue;
        if (milestone == null) {
            titleValue = String.format("%s (%d)", UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED), wrapBody.getComponentCount());
        } else {
            titleValue = new DivLessFormatter().appendChild(new A(ProjectLinkBuilder.generateMilestonePreviewFullLink(milestone.getProjectid(), milestone.getId())).
                    appendText(String.format("%s (%d)", milestone.getName(), wrapBody.getComponentCount()))).write();
        }
        headerLbl.setValue(titleValue);
    }
}
