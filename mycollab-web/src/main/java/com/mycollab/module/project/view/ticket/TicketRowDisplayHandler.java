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
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class TicketRowDisplayHandler implements IBeanList.RowDisplayHandler<ProjectTicket> {

    private boolean displayPrjShortname;

    public TicketRowDisplayHandler(boolean displayPrjShortname) {
        this.displayPrjShortname = displayPrjShortname;
    }

    @Override
    public Component generateRow(IBeanList<ProjectTicket> host, ProjectTicket ticket, int rowIndex) {
        MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName("list-row").withFullWidth();
        rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        Div issueDiv = new Div().appendText(ProjectAssetsManager.getAsset(ticket.getType()).getHtml());

        String status = "";
        if (ticket.isBug()) {
            status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
            rowComp.addStyleName("bug");
        } else if (ticket.isMilestone()) {
            status = UserUIContext.getMessage(MilestoneStatus.class, ticket.getStatus());
            rowComp.addStyleName("milestone");
        } else if (ticket.isRisk()) {
            status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
            rowComp.addStyleName("risk");
        } else if (ticket.isTask()) {
            status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
            rowComp.addStyleName("task");
        }
        issueDiv.appendChild(new Span().appendText(status).setCSSClass(WebThemes.BLOCK));

        String avatarLink = StorageUtils.getAvatarPath(ticket.getAssignUserAvatarId(), 16);
        Img img = new Img(ticket.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                .setTitle(ticket.getAssignUserFullName());
        issueDiv.appendChild(img, DivLessFormatter.EMPTY_SPACE);

        A ticketLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID);
        ticketLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ticket.getType(), ticket.getTypeId() + ""));
        ticketLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        if (displayPrjShortname) {
            ticketLink.appendText(String.format("[%s] - %s", ticket.getProjectShortName(), ticket.getName()));
        } else {
            ticketLink.appendText(ticket.getName());
        }
        ticketLink.setHref(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(),
                ticket.getProjectId(), ticket.getType(), ticket.getExtraTypeId() + ""));

        issueDiv.appendChild(ticketLink);
        if (ticket.isClosed()) {
            ticketLink.setCSSClass("completed");
        } else if (ticket.isOverdue()) {
            ticketLink.setCSSClass("overdue");
            issueDiv.appendChild(new Span().appendText(" - " + UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_DUE_IN,
                    UserUIContext.formatDuration(ticket.getDueDate()))).setCSSClass(WebThemes.META_INFO));
        }

        rowComp.with(ELabel.html(issueDiv.write()).withFullWidth());
        return rowComp;
    }
}
