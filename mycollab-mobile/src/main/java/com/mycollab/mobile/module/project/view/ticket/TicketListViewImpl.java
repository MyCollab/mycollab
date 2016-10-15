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

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.BugEvent;
import com.mycollab.mobile.module.project.events.RiskEvent;
import com.mycollab.mobile.module.project.events.TaskEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.module.project.ui.SearchInputView;
import com.mycollab.mobile.module.project.ui.SearchNavigationButton;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
@ViewComponent
public class TicketListViewImpl extends AbstractListPageView<ProjectTicketSearchCriteria, ProjectTicket> implements TicketListView {

    public TicketListViewImpl() {
        setCaption(UserUIContext.getMessage(TicketI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<ProjectTicketSearchCriteria, ProjectTicket> createBeanList() {
        return new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(ProjectTicketService.class), new TicketRowDisplayHandler());
    }

    @Override
    protected SearchInputField<ProjectTicketSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu actionMenu = new NavigationBarQuickMenu();
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(UserUIContext.getMessage(TaskI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(TicketListViewImpl.this, null))));
        content.with(new Button(UserUIContext.getMessage(BugI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoAdd(TicketListViewImpl.this, null))));
        if (!SiteConfiguration.isCommunityEdition()) {
            content.with(new Button(UserUIContext.getMessage(RiskI18nEnum.NEW),
                    clickEvent -> EventBusFactory.getInstance().post(new RiskEvent.GotoAdd(TicketListViewImpl.this, null))));
        }

        actionMenu.setContent(content);

        SearchNavigationButton searchBtn = new SearchNavigationButton() {
            @Override
            protected SearchInputView getSearchInputView() {
                return new TicketSearchInputView();
            }
        };
        return new MHorizontalLayout(searchBtn, actionMenu).alignAll(Alignment.TOP_RIGHT);
    }
}
