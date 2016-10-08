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

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.IgnoreException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.module.project.events.BugEvent;
import com.mycollab.mobile.module.project.events.RiskEvent;
import com.mycollab.mobile.module.project.events.TaskEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
@ViewComponent
public class TicketListViewImpl extends AbstractListPageView<ProjectTicketSearchCriteria, ProjectTicket> implements TicketListView {

    public TicketListViewImpl() {
        super();
        setCaption(UserUIContext.getMessage(TicketI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<ProjectTicketSearchCriteria, ProjectTicket> createBeanList() {
        return new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(ProjectTicketService.class),
                new TicketRowDisplayHandler());
    }

    @Override
    protected SearchInputField<ProjectTicketSearchCriteria> createSearchField() {
        return new SearchInputField<ProjectTicketSearchCriteria>() {
            @Override
            protected ProjectTicketSearchCriteria fillUpSearchCriteria(String value) {
                ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
                searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                searchCriteria.setName(StringSearchField.and(value));
                searchCriteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
                return searchCriteria;
            }
        };
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(UserUIContext.getMessage(TaskI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(TicketListViewImpl.this, null))));
        content.with(new Button(UserUIContext.getMessage(BugI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoAdd(TicketListViewImpl.this, null))));
        if (!SiteConfiguration.isCommunityEdition()) {
            content.with(new Button(UserUIContext.getMessage(RiskI18nEnum.NEW),
                    clickEvent -> EventBusFactory.getInstance().post(new RiskEvent.GotoAdd(TicketListViewImpl.this, null))));
        }

        menu.setContent(content);
        return menu;
    }

    private static class TicketRowDisplayHandler implements AbstractPagedBeanList.RowDisplayHandler<ProjectTicket> {

        @Override
        public Component generateRow(final ProjectTicket ticket, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withSpacing(false).withFullWidth();

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

            CssLayout ticketLbl = new CssLayout(ELabel.html(ticketLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS));
            rowLayout.with(new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset(ticket.getType())), ticketLbl)
                    .expand(ticketLbl).withFullWidth());

            CssLayout metaInfoLayout = new CssLayout();
            rowLayout.with(metaInfoLayout);

            ELabel lastUpdatedTimeLbl = new ELabel(UserUIContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, UserUIContext
                    .formatPrettyTime((ticket.getLastUpdatedTime())))).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(lastUpdatedTimeLbl);

            A assigneeLink = new A(ProjectLinkGenerator.generateProjectMemberFullLink(MyCollabUI.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), ticket.getAssignUser()));
            assigneeLink.appendText(StringUtils.trim(ticket.getAssignUserFullName(), 30, true));
            Div assigneeDiv = new Div().appendText(UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                    .appendChild(DivLessFormatter.EMPTY_SPACE(), new Img("", StorageFactory
                                    .getAvatarPath(ticket.getAssignUserAvatarId(), 16)), DivLessFormatter.EMPTY_SPACE(),
                            assigneeLink);

            ELabel assigneeLbl = ELabel.html(assigneeDiv.write()).withStyleName(UIConstants.META_INFO).withWidthUndefined();
            metaInfoLayout.addComponent(assigneeLbl);

            ELabel statusLbl = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_STATUS) + ": " + UserUIContext
                    .getMessage(OptionI18nEnum.BugStatus.class, ticket.getStatus())).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(statusLbl);

            return rowLayout;
        }
    }
}
