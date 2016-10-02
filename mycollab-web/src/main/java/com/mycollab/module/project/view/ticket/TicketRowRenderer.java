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

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.milestone.ToggleTicketSummaryField;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class TicketRowRenderer extends MVerticalLayout {
    private ProjectTicket assignment;

    private ToggleTicketSummaryField toggleTaskField;

    public TicketRowRenderer(final ProjectTicket ticket) {
        this.assignment = ticket;
        withMargin(false).withFullWidth().addStyleName(WebUIConstants.BORDER_LIST_ROW);

        MHorizontalLayout headerLayout = new MHorizontalLayout();
        toggleTaskField = new ToggleTicketSummaryField(ticket);
        headerLayout.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(ticket.getType())).withWidthUndefined(),
                toggleTaskField).expand(toggleTaskField).withFullWidth().withMargin(new MarginInfo(false, true, false, false));

        TicketComponentFactory popupFieldFactory = AppContextUtil.getSpringBean(TicketComponentFactory.class);
        AbstractComponent assigneeField = popupFieldFactory.createAssigneePopupField(ticket);
        headerLayout.with(assigneeField, toggleTaskField).expand(toggleTaskField);

        CssLayout footer = new CssLayout();
        footer.addComponent(popupFieldFactory.createCommentsPopupField(ticket));
        footer.addComponent(popupFieldFactory.createPriorityPopupField(ticket));
        footer.addComponent(popupFieldFactory.createFollowersPopupField(ticket));
        footer.addComponent(popupFieldFactory.createStatusPopupField(ticket));
        footer.addComponent(popupFieldFactory.createStartDatePopupField(ticket));
        footer.addComponent(popupFieldFactory.createEndDatePopupField(ticket));
        footer.addComponent(popupFieldFactory.createDueDatePopupField(ticket));
        if (!SiteConfiguration.isCommunityEdition()) {
            footer.addComponent(popupFieldFactory.createBillableHoursPopupField(ticket));
            footer.addComponent(popupFieldFactory.createNonBillableHoursPopupField(ticket));
        }

        this.with(headerLayout, footer);
    }
}
