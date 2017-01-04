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

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.BlockRowRender;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.PropertyChangedEvent;
import com.mycollab.vaadin.ui.PropertyChangedListener;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.LazyPopupView;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CssLayout;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class TicketRowRenderer extends BlockRowRender implements PropertyChangedListener {
    private static Logger LOG = LoggerFactory.getLogger(TicketRowRenderer.class);

    private ToggleTicketSummaryField toggleTicketField;
    private ProjectTicket ticket;

    TicketRowRenderer(final ProjectTicket ticket) {
        this.ticket = ticket;
        withMargin(false).withFullWidth().addStyleName(WebThemes.BORDER_LIST_ROW);

        if (ticket.isTask()) {
            addStyleName("task");
        } else if (ticket.isBug()) {
            addStyleName("bug");
        } else if (ticket.isRisk()) {
            addStyleName("risk");
        }

        toggleTicketField = new ToggleTicketSummaryField(ticket);
        MHorizontalLayout headerLayout = new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset(ticket.getType()))
                .withWidthUndefined(), toggleTicketField).expand(toggleTicketField).withFullWidth()
                .withMargin(new MarginInfo(false, true, false, false));

        TicketComponentFactory popupFieldFactory = AppContextUtil.getSpringBean(TicketComponentFactory.class);
        AbstractComponent assigneeField = wrapListenerComponent(popupFieldFactory.createAssigneePopupField(ticket));
        headerLayout.with(assigneeField, toggleTicketField).expand(toggleTicketField);

        CssLayout footer = new CssLayout();
        footer.addComponent(popupFieldFactory.createCommentsPopupField(ticket));
        footer.addComponent(wrapListenerComponent(popupFieldFactory.createPriorityPopupField(ticket)));
        footer.addComponent(popupFieldFactory.createFollowersPopupField(ticket));
        footer.addComponent(wrapListenerComponent(popupFieldFactory.createStatusPopupField(ticket)));
        footer.addComponent(wrapListenerComponent(popupFieldFactory.createStartDatePopupField(ticket)));
        footer.addComponent(wrapListenerComponent(popupFieldFactory.createEndDatePopupField(ticket)));
        footer.addComponent(wrapListenerComponent(popupFieldFactory.createDueDatePopupField(ticket)));
        if (!SiteConfiguration.isCommunityEdition()) {
            footer.addComponent(popupFieldFactory.createBillableHoursPopupField(ticket));
            footer.addComponent(popupFieldFactory.createNonBillableHoursPopupField(ticket));
        }
        this.with(headerLayout, footer);
    }

    private AbstractComponent wrapListenerComponent(AbstractComponent component) {
        if (component instanceof LazyPopupView) {
            ((LazyPopupView) component).addPropertyChangeListener(this);
        }
        return component;
    }

    public ProjectTicket getTicket() {
        return ticket;
    }

    @Override
    public void propertyChanged(PropertyChangedEvent event) {
        EventBusFactory.getInstance().post(new TicketEvent.HasTicketPropertyChanged(this, event.getBindProperty()));
        if ("status".equals(event.getBindProperty())) {
            Object bean = event.getSource();
            try {
                String statusValue = (String) PropertyUtils.getProperty(bean, "status");
                boolean isClosed = StatusI18nEnum.Closed.name().equals(statusValue) || BugStatus.Verified.name().equals(statusValue);
                if (isClosed) {
                    toggleTicketField.setClosedTicket();
                } else {
                    toggleTicketField.unsetClosedTicket();
                }
            } catch (Exception e) {
                LOG.error("Error", e);
            }
        }

        TicketDashboardView ticketDashboardView = UIUtils.getRoot(this, TicketDashboardView.class);
        if (ticketDashboardView != null) {
            ProjectTicketSearchCriteria criteria = ticketDashboardView.getCriteria();
            boolean isSatisfied = AppContextUtil.getSpringBean(ProjectTicketService.class).isTicketIdSatisfyCriteria(ticket.getType(),
                    ticket.getTypeId(), criteria);
            if (!isSatisfied) {
                this.selfRemoved();
            }
        }
    }
}
