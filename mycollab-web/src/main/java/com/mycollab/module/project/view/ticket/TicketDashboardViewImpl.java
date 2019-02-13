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

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.QueryAnalyzer;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.query.TicketQueryInfo;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.QueryParamHandler;
import com.mycollab.vaadin.web.ui.StringValueComboBox;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TicketDashboardViewImpl extends AbstractVerticalPageView implements TicketDashboardView {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(TicketDashboardViewImpl.class);

    private int currentPage = 0;

    private String groupByState;
    private String sortDirection;

    private ProjectTicketSearchCriteria baseCriteria;
    private ProjectTicketSearchCriteria statisticSearchCriteria;

    private TicketSearchPanel ticketSearchPanel;
    private MVerticalLayout wrapBody;
    private TicketGroupOrderComponent ticketGroupOrderComponent;

    private ApplicationEventListener<TicketEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<TicketEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(TicketEvent.SearchRequest event) {
                    ProjectTicketSearchCriteria criteria = event.getSearchCriteria();
                    queryTickets(criteria);
                }
            };

    private ApplicationEventListener<TicketEvent.NewTicketAdded> newTicketAddedHandler = new
            ApplicationEventListener<TicketEvent.NewTicketAdded>() {
                @Override
                @Subscribe
                public void handle(TicketEvent.NewTicketAdded event) {
                    final ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
                    ProjectTicket ticket = projectTicketService.findTicket(event.getTypeVal(), event.getTypeIdVal());
                    if (ticket != null && ticketGroupOrderComponent != null) {
                        ticketGroupOrderComponent.insertTickets(Collections.singletonList(ticket));
                    }
                    displayTicketsStatistic();

                    int totalTickets = projectTicketService.getTotalTicketsCount(baseCriteria);
                    ticketSearchPanel.setTotalCountNumber(totalTickets);
                }
            };

    private ApplicationEventListener<ShellEvent.AddQueryParam> addQueryHandler = QueryParamHandler.queryParamHandler();

    public TicketDashboardViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        ticketSearchPanel = new TicketSearchPanel();

        MHorizontalLayout extraCompsHeaderLayout = new MHorizontalLayout();
        extraCompsHeaderLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        extraCompsHeaderLayout.addComponent(new ELabel(UserUIContext.getMessage(GenericI18Enum.ACTION_SORT)));
        StringValueComboBox sortCombo = new StringValueComboBox(false, UserUIContext.getMessage(GenericI18Enum.OPT_SORT_DESCENDING),
                UserUIContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING));
        sortCombo.setWidth("130px");
        sortCombo.addValueChangeListener(valueChangeEvent -> {
            String sortValue = sortCombo.getValue();
            if (UserUIContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING).equals(sortValue)) {
                sortDirection = SearchCriteria.ASC;
            } else {
                sortDirection = SearchCriteria.DESC;
            }
            queryAndDisplayTickets();
        });
        sortDirection = SearchCriteria.DESC;
        extraCompsHeaderLayout.addComponent(sortCombo);

        extraCompsHeaderLayout.addComponent(new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_GROUP)));
        StringValueComboBox groupCombo = new StringValueComboBox(false, UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE),
                UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE), UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME),
                UserUIContext.getMessage(GenericI18Enum.OPT_PLAIN), UserUIContext.getMessage(GenericI18Enum.OPT_USER),
                UserUIContext.getMessage(MilestoneI18nEnum.SINGLE));
        groupByState = UserUIContext.getMessage(MilestoneI18nEnum.SINGLE);
        groupCombo.setValue(UserUIContext.getMessage(MilestoneI18nEnum.SINGLE));
        groupCombo.addValueChangeListener(valueChangeEvent -> {
            groupByState = groupCombo.getValue();
            queryAndDisplayTickets();
        });
        groupCombo.setWidth("130px");

        extraCompsHeaderLayout.addComponent(groupCombo);

        MButton printBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(
                new TicketCustomizeReportOutputWindow(new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return baseCriteria;
                    }
                }))).withIcon(VaadinIcons.PRINT).withStyleName(WebThemes.BUTTON_OPTION)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        extraCompsHeaderLayout.addComponent(printBtn);

        MButton newTicketBtn = new MButton(UserUIContext.getMessage(TicketI18nEnum.NEW), clickEvent -> {
            UI.getCurrent().addWindow(AppContextUtil.getSpringBean(TicketComponentFactory.class)
                    .createNewTicketWindow(null, CurrentProjectVariables.getProjectId(), null, false));
        }).withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWriteTicket());
        extraCompsHeaderLayout.addComponent(newTicketBtn);

        ticketSearchPanel.addHeaderRight(extraCompsHeaderLayout);

        wrapBody = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false));

        this.with(ticketSearchPanel, wrapBody).expand(wrapBody);
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        EventBusFactory.getInstance().register(newTicketAddedHandler);
        EventBusFactory.getInstance().register(addQueryHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(searchHandler);
        EventBusFactory.getInstance().unregister(newTicketAddedHandler);
        EventBusFactory.getInstance().unregister(addQueryHandler);
        super.detach();
    }

    public void displayView(String query) {
        baseCriteria = new ProjectTicketSearchCriteria();
        baseCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        baseCriteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());

        statisticSearchCriteria = BeanUtility.deepClone(baseCriteria);
        statisticSearchCriteria.setOpen(new SearchField());
        statisticSearchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                ProjectTypeConstants.RISK));

        if (StringUtils.isNotBlank(query)) {
            try {
                String jsonQuery = UrlEncodeDecoder.decode(query);
                List<SearchFieldInfo<ProjectTicketSearchCriteria>> searchFieldInfos = QueryAnalyzer.toSearchFieldInfos(jsonQuery, ProjectTypeConstants.TICKET);
                ticketSearchPanel.displaySearchFieldInfos(searchFieldInfos);
                ProjectTicketSearchCriteria searchCriteria = SearchFieldInfo.buildSearchCriteria(baseCriteria, searchFieldInfos);
                searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                queryTickets(searchCriteria);
            } catch (Exception e) {
                LOG.error("Error", e);
                ticketSearchPanel.selectQueryInfo(TicketQueryInfo.OPEN_TICKETS);
            }
        } else {
            ticketSearchPanel.selectQueryInfo(TicketQueryInfo.OPEN_TICKETS);
        }
    }

    @Override
    public void showNoItemView() {

    }

    @Override
    public ProjectTicketSearchCriteria getCriteria() {
        return baseCriteria;
    }

    private void displayTicketsStatistic() {
        ProjectView rightBar = UIUtils.getRoot(this, ProjectView.class);
        UnresolvedTicketsByAssigneeWidget unresolvedTicketsByAssigneeWidget = new UnresolvedTicketsByAssigneeWidget();
        unresolvedTicketsByAssigneeWidget.setSearchCriteria(statisticSearchCriteria);
        UIUtils.makeStackPanel(unresolvedTicketsByAssigneeWidget);

        UnresolvedTicketByPriorityWidget unresolvedTicketByPriorityWidget = new UnresolvedTicketByPriorityWidget();
        unresolvedTicketByPriorityWidget.setSearchCriteria(statisticSearchCriteria);
        UIUtils.makeStackPanel(unresolvedTicketByPriorityWidget);

        rightBar.addComponentToRightBar(new MVerticalLayout(unresolvedTicketsByAssigneeWidget, unresolvedTicketByPriorityWidget).withMargin(false));
    }

    @Override
    public void queryTickets(ProjectTicketSearchCriteria searchCriteria) {
        baseCriteria = searchCriteria;
        if (baseCriteria.getTypes() == null) {
            baseCriteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
        }

        queryAndDisplayTickets();
        displayTicketsStatistic();
    }

    private void queryAndDisplayTickets() {
        wrapBody.removeAllComponents();

        if (UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("dueDate", sortDirection)));
            ticketGroupOrderComponent = new DueDateOrderComponent();
        } else if (UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("startdate", sortDirection)));
            ticketGroupOrderComponent = new StartDateOrderComponent();
        } else if (UserUIContext.getMessage(GenericI18Enum.OPT_PLAIN).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("lastupdatedtime", sortDirection)));
            ticketGroupOrderComponent = new SimpleListOrderComponent();
        } else if (UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdtime", sortDirection)));
            ticketGroupOrderComponent = new CreatedDateOrderComponent();
        } else if (UserUIContext.getMessage(GenericI18Enum.OPT_USER).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("assignUser", sortDirection)));
            ticketGroupOrderComponent = new UserOrderComponent();
        } else if (UserUIContext.getMessage(MilestoneI18nEnum.SINGLE).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("milestoneId", sortDirection)));
            ticketGroupOrderComponent = new MilestoneOrderGroup();
        } else {
            throw new MyCollabException("Do not support group view by " + groupByState);
        }
        wrapBody.addComponent(ticketGroupOrderComponent);
        ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        int totalTasks = projectTicketService.getTotalTicketsCount(baseCriteria);
        ticketSearchPanel.setTotalCountNumber(totalTasks);
        currentPage = 0;
        int pages = totalTasks / 100;
        if (currentPage < pages) {
            MButton moreBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_MORE), clickEvent -> {
                int newTotalTickets = projectTicketService.getTotalTicketsCount(baseCriteria);
                int newNumPages = newTotalTickets / 100;
                currentPage++;
                List<ProjectTicket> otherTickets = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new
                        BasicSearchRequest<>(baseCriteria, currentPage + 1, 100));
                ticketGroupOrderComponent.insertTickets(otherTickets);
                if (currentPage >= newNumPages) {
                    wrapBody.removeComponent(wrapBody.getComponent(1));
                }
            }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.ANGLE_DOUBLE_DOWN);
            wrapBody.addComponent(moreBtn);
        }
        List<ProjectTicket> tickets = (List<ProjectTicket>) projectTicketService.findTicketsByCriteria(new BasicSearchRequest<>
                (baseCriteria, currentPage + 1, 100));
        ticketGroupOrderComponent.insertTickets(tickets);
    }

    @Override
    public HasSearchHandlers<ProjectTicketSearchCriteria> getSearchHandlers() {
        return ticketSearchPanel;
    }
}