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
package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.IBlockContainer;
import com.mycollab.module.project.ui.components.IGroupComponent;
import com.mycollab.module.project.ui.components.TicketRowRender;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.dnd.DropTargetExtension;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Optional;

/**
 * @author MyCollab Ltd
 * @since 5.4.5
 */
class MilestoneTicketGroupComponent extends MVerticalLayout implements IGroupComponent, IBlockContainer {
    private Label headerLbl;
    private MVerticalLayout wrapBody;

    private SimpleMilestone milestone;

    MilestoneTicketGroupComponent(Integer milestoneId) {
        this.withMargin(new MarginInfo(true, false, true, false)).withSpacing(false);
        wrapBody = new MVerticalLayout().withFullWidth().withSpacing(false).withMargin(false).withStyleName(WebThemes.BORDER_LIST);
        headerLbl = ELabel.h3("").withFullWidth();

        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        MVerticalLayout headerGroup;
        if (milestoneId == null) {
            headerGroup = new MVerticalLayout(headerLbl).withMargin(false).withSpacing(false);
        } else {
            milestone = milestoneService.findById(milestoneId, AppUI.getAccountId());
            if (milestone != null) {
                ELabel milestoneDateLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_FROM_TO,
                        UserUIContext.formatDate(milestone.getStartdate()), UserUIContext.formatDate(milestone.getEnddate())))
                        .withStyleName(WebThemes.META_INFO);
                headerGroup = new MVerticalLayout(headerLbl, milestoneDateLbl).withMargin(false).withSpacing(false);
            } else {
                headerGroup = new MVerticalLayout(headerLbl).withMargin(false).withSpacing(false);
            }
        }

        with(headerGroup, wrapBody);

        // make the layout accept drops
        DropTargetExtension<MVerticalLayout> dropTarget = new DropTargetExtension<>(wrapBody);
        // catch the drops
        dropTarget.addDropListener(event -> {
            Optional<AbstractComponent> dragSource = event.getDragSourceComponent();
            if (dragSource.isPresent() && dragSource.get() instanceof EditableTicketRowRenderer) {
                TicketRowRender ticketRowRenderer = (TicketRowRender) dragSource.get();
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

    void insertTicketComp(TicketRowRender ticketRowRenderer) {
        ticketRowRenderer.addStyleName("cursor-move");
        wrapBody.addComponent(ticketRowRenderer);
        updateTitle();
    }

    private void updateTitle() {
        String titleValue;
        if (milestone == null) {
            titleValue = String.format("%s (%d)", UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED), wrapBody.getComponentCount());
        } else {
            titleValue = new DivLessFormatter().appendChild(new A(ProjectLinkGenerator.generateMilestonePreviewLink(milestone.getProjectid(), milestone.getId())).
                    appendText(String.format("%s (%d)", milestone.getName(), wrapBody.getComponentCount()))).write();
        }
        headerLbl.setValue(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml() + " " + titleValue);
    }
}
