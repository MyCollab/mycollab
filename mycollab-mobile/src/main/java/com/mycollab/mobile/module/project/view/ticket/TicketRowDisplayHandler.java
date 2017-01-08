/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.ticket;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.IgnoreException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketRowDisplayHandler implements IBeanList.RowDisplayHandler<ProjectTicket> {

    @Override
    public Component generateRow(IBeanList<ProjectTicket> host, final ProjectTicket ticket, int rowIndex) {
        MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
        if (ticket.isTask()) {
            rowLayout.addStyleName("task");
        } else if (ticket.isBug()) {
            rowLayout.addStyleName("bug");
        } else if (ticket.isRisk()) {
            rowLayout.addStyleName("risk");
        }

        A ticketLink = new A();
        if (ticket.isBug() || ticket.isTask()) {
            ticketLink.setHref(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(),
                    ticket.getProjectId(), ticket.getType(), ticket.getExtraTypeId() + ""));
        } else if (ticket.isRisk()) {
            ticketLink.setHref(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(),
                    ticket.getProjectId(), ticket.getType(), ticket.getTypeId() + ""));
        } else {
            throw new IgnoreException("Not support type: " + ticket.getType());
        }
        ticketLink.appendText(ticket.getName());

        ELabel ticketLbl = ELabel.html(ticketLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
        if (ticket.isClosed()) {
            ticketLbl.addStyleName(MobileUIConstants.LINK_COMPLETED);
        } else if (ticket.isOverdue()) {
            ticketLbl.addStyleName(MobileUIConstants.LINK_OVERDUE);
        }
        CssLayout ticketLayout = new CssLayout(ticketLbl);
        String priorityValue = ProjectAssetsManager.getPriority(ticket.getPriority()).getHtml();
        ELabel priorityLbl = ELabel.html(priorityValue).withWidthUndefined().withStyleName("priority-" + ticket.getPriority().toLowerCase());
        rowLayout.with(new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset(ticket.getType())), priorityLbl,
                ticketLayout).expand(ticketLayout).withFullWidth());

        MVerticalLayout metaInfoLayout = new MVerticalLayout().withMargin(false);
        rowLayout.with(metaInfoLayout);

        ELabel lastUpdatedTimeLbl = new ELabel(UserUIContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, UserUIContext
                .formatPrettyTime((ticket.getLastUpdatedTime())))).withStyleName(UIConstants.META_INFO);
        metaInfoLayout.addComponent(lastUpdatedTimeLbl);

        A assigneeLink = new A(ProjectLinkGenerator.generateProjectMemberLink(CurrentProjectVariables.getProjectId(),
                ticket.getAssignUser()));
        assigneeLink.appendText(StringUtils.trim(ticket.getAssignUserFullName(), 30, true));
        Div assigneeDiv = new Div().appendText(UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .appendChild(DivLessFormatter.EMPTY_SPACE(), new Img("", StorageFactory
                                .getAvatarPath(ticket.getAssignUserAvatarId(), 16)).setCSSClass(UIConstants.CIRCLE_BOX),
                        DivLessFormatter.EMPTY_SPACE(), assigneeLink);

        ELabel assigneeLbl = ELabel.html(assigneeDiv.write()).withStyleName(UIConstants.META_INFO).withWidthUndefined();
        metaInfoLayout.addComponent(assigneeLbl);

        String status;
        if (ticket.isBug()) {
            status = UserUIContext.getMessage(BugStatus.class, ticket.getStatus());
        } else {
            status = UserUIContext.getMessage(OptionI18nEnum.StatusI18nEnum.class, ticket.getStatus());
        }
        MHorizontalLayout statusLbl = new MHorizontalLayout(ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_STATUS)).withStyleName
                (UIConstants.META_INFO), new ELabel(status).withStyleName(UIConstants.BLOCK));
        metaInfoLayout.addComponent(statusLbl);

        return rowLayout;
    }
}
