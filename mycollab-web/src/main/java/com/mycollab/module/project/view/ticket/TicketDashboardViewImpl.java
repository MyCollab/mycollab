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

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.QueryAnalyzer;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.AssignmentEvent;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.module.project.view.task.components.*;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasMassItemActionHandler;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.QueryParamHandler;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.ValueComboBox;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TicketDashboardViewImpl extends AbstractPageView implements TicketDashboardView {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TicketDashboardViewImpl.class);

    private int currentPage = 0;

    private String groupByState;
    private String sortDirection;

    private ProjectTicketSearchCriteria baseCriteria;
    private ProjectTicketSearchCriteria statisticSearchCriteria;

    private TicketSearchPanel ticketSearchPanel;
    private MVerticalLayout wrapBody;
    private VerticalLayout rightColumn;
    private TicketGroupOrderComponent ticketGroupOrderComponent;

    private ApplicationEventListener<AssignmentEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<AssignmentEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(AssignmentEvent.SearchRequest event) {
                    ProjectTicketSearchCriteria criteria = (ProjectTicketSearchCriteria) event.getData();
                    if (criteria != null) {
                        criteria.setTypes(new SetSearchField(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                                ProjectTypeConstants.RISK));
                        queryAssignments(criteria);
                    }
                }
            };

    private ApplicationEventListener<TaskEvent.NewTaskAdded> newTaskAddedHandler = new
            ApplicationEventListener<TaskEvent.NewTaskAdded>() {
                @Override
                @Subscribe
                public void handle(TaskEvent.NewTaskAdded event) {
                    final ProjectTicketService projectTaskService = AppContextUtil.getSpringBean(ProjectTicketService.class);
//                    SimpleTask task = projectTaskService.findById((Integer) event.getData(), MyCollabUI.getAccountId());
//                    if (task != null && ticketGroupOrderComponent != null) {
//                        ticketGroupOrderComponent.insertTasks(Collections.singletonList(task));
//                    }
                    displayTaskStatistic();

                    int totalTasks = projectTaskService.getTotalAssignmentsCount(baseCriteria);
                    ticketSearchPanel.setTotalCountNumber(totalTasks);
                }
            };

    private ApplicationEventListener<ShellEvent.AddQueryParam> addQueryHandler = QueryParamHandler.queryParamHandler();

    public TicketDashboardViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        ticketSearchPanel = new TicketSearchPanel();

        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        groupWrapLayout.addComponent(new ELabel(UserUIContext.getMessage(GenericI18Enum.ACTION_SORT)));
        final ComboBox sortCombo = new ValueComboBox(false, UserUIContext.getMessage(GenericI18Enum.OPT_SORT_DESCENDING),
                UserUIContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING));
        sortCombo.addValueChangeListener(valueChangeEvent -> {
            String sortValue = (String) sortCombo.getValue();
            if (UserUIContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING).equals(sortValue)) {
                sortDirection = SearchCriteria.ASC;
            } else {
                sortDirection = SearchCriteria.DESC;
            }
            queryAndDisplayTasks();
        });
        sortDirection = SearchCriteria.DESC;
        groupWrapLayout.addComponent(sortCombo);

        groupWrapLayout.addComponent(new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_GROUP)));
        final ComboBox groupCombo = new ValueComboBox(false, UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE),
                UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE), UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME),
                UserUIContext.getMessage(GenericI18Enum.OPT_PLAIN), UserUIContext.getMessage(GenericI18Enum.OPT_USER));
        groupCombo.addValueChangeListener(valueChangeEvent -> {
            groupByState = (String) groupCombo.getValue();
            queryAndDisplayTasks();
        });
        groupByState = UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE);
        groupWrapLayout.addComponent(groupCombo);

        ticketSearchPanel.addHeaderRight(groupWrapLayout);

        MButton printBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(
                new TicketCustomizeReportOutputWindow(new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return baseCriteria;
                    }
                }))).withIcon(FontAwesome.PRINT).withStyleName(WebUIConstants.BUTTON_OPTION)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        groupWrapLayout.addComponent(printBtn);

        MButton newTicketBtn = new MButton(UserUIContext.getMessage(TicketI18nEnum.NEW), clickEvent -> {
            UI.getCurrent().addWindow(AppContextUtil.getSpringBean(TicketComponentFactory.class).createNewTicketWindow(new
                    Date(), CurrentProjectVariables.getProjectId(), null, false));
        }).withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        groupWrapLayout.addComponent(newTicketBtn);

        MButton advanceDisplayBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_LIST))
                .withIcon(FontAwesome.NAVICON).withWidth("100px");

        MButton kanbanBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN), clickEvent ->
                displayKanbanView()).withWidth("100px").withIcon(FontAwesome.TH);

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(advanceDisplayBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.withDefaultButton(advanceDisplayBtn);
        groupWrapLayout.addComponent(viewButtons);

        MHorizontalLayout mainLayout = new MHorizontalLayout().withFullHeight().withFullWidth();
        wrapBody = new MVerticalLayout().withMargin(new MarginInfo(false, true, true, false));
        rightColumn = new MVerticalLayout().withWidth("370px").withMargin(new MarginInfo(true, false, false, false));
        mainLayout.with(wrapBody, rightColumn).expand(wrapBody);
        this.with(ticketSearchPanel, mainLayout);
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        EventBusFactory.getInstance().register(newTaskAddedHandler);
        EventBusFactory.getInstance().register(addQueryHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(searchHandler);
        EventBusFactory.getInstance().unregister(newTaskAddedHandler);
        EventBusFactory.getInstance().unregister(addQueryHandler);
        super.detach();
    }

    public void displayView(String query) {
        baseCriteria = new ProjectTicketSearchCriteria();
        baseCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        baseCriteria.setTypes(new SetSearchField(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                ProjectTypeConstants.RISK));

        statisticSearchCriteria = BeanUtility.deepClone(baseCriteria);

        if (StringUtils.isNotBlank(query)) {
            try {
                String jsonQuery = UrlEncodeDecoder.decode(query);
                List<SearchFieldInfo> searchFieldInfos = QueryAnalyzer.toSearchFieldInfos(jsonQuery, ProjectTypeConstants.TICKET);
                ticketSearchPanel.displaySearchFieldInfos(searchFieldInfos);
                ProjectTicketSearchCriteria searchCriteria = SearchFieldInfo.buildSearchCriteria(baseCriteria, searchFieldInfos);
                searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                searchCriteria.setTypes(new SetSearchField(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                        ProjectTypeConstants.RISK));
                queryAssignments(searchCriteria);
            } catch (Exception e) {
                LOG.error("Error", e);
                ticketSearchPanel.selectQueryInfo(TaskSavedFilterComboBox.OPEN_TASKS);
            }
        } else {
            ticketSearchPanel.selectQueryInfo(TaskSavedFilterComboBox.OPEN_TASKS);
        }
    }

    @Override
    public void showNoItemView() {

    }

    private void displayTaskStatistic() {
        rightColumn.removeAllComponents();
//        final TaskStatusTrendChartWidget taskStatusTrendChartWidget = new TaskStatusTrendChartWidget();
//        rightColumn.addComponent(taskStatusTrendChartWidget);
        UnresolvedTicketsByAssigneeWidget unresolvedTicketsByAssigneeWidget = new UnresolvedTicketsByAssigneeWidget();
        unresolvedTicketsByAssigneeWidget.setSearchCriteria(statisticSearchCriteria);
        rightColumn.addComponent(unresolvedTicketsByAssigneeWidget);
//
//        UnresolvedTaskByPriorityWidget unresolvedTaskByPriorityWidget = new UnresolvedTaskByPriorityWidget();
//        unresolvedTaskByPriorityWidget.setSearchCriteria(statisticSearchCriteria);
//        rightColumn.addComponent(unresolvedTaskByPriorityWidget);
//
//        UnresolvedTaskByStatusWidget unresolvedTaskByStatusWidget = new UnresolvedTaskByStatusWidget();
//        unresolvedTaskByStatusWidget.setSearchCriteria(statisticSearchCriteria);
//        rightColumn.addComponent(unresolvedTaskByStatusWidget);
//
//        AsyncInvoker.access(getUI(), new AsyncInvoker.PageCommand() {
//            @Override
//            public void run() {
//                TimelineTrackingSearchCriteria timelineTrackingSearchCriteria = new TimelineTrackingSearchCriteria();
//                timelineTrackingSearchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
//                taskStatusTrendChartWidget.display(timelineTrackingSearchCriteria);
//            }
//        });
    }

    @Override
    public void queryAssignments(final ProjectTicketSearchCriteria searchCriteria) {
        baseCriteria = searchCriteria;
        queryAndDisplayTasks();
        displayTaskStatistic();
    }

    private void queryAndDisplayTasks() {
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
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdtime", sortDirection)));
            ticketGroupOrderComponent = new UserOrderComponent();
        } else {
            throw new MyCollabException("Do not support group view by " + groupByState);
        }
        wrapBody.addComponent(ticketGroupOrderComponent);
        final ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        int totalTasks = projectTicketService.getTotalAssignmentsCount(baseCriteria);
        ticketSearchPanel.setTotalCountNumber(totalTasks);
        currentPage = 0;
        int pages = totalTasks / 20;
        if (currentPage < pages) {
            Button moreBtn = new Button(UserUIContext.getMessage(GenericI18Enum.ACTION_MORE), clickEvent -> {
                int newTotalTasks = projectTicketService.getTotalAssignmentsCount(baseCriteria);
                int newNumPages = newTotalTasks / 20;
                currentPage++;
                List<ProjectTicket> otherTickets = projectTicketService.findAssignmentsByCriteria(new
                        BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
                ticketGroupOrderComponent.insertTickets(otherTickets);
                if (currentPage >= newNumPages) {
                    wrapBody.removeComponent(wrapBody.getComponent(1));
                }
            });
            moreBtn.addStyleName(WebUIConstants.BUTTON_ACTION);
            wrapBody.addComponent(moreBtn);
        }
        List<ProjectTicket> tickets = projectTicketService.findAssignmentsByCriteria(new BasicSearchRequest<>
                (baseCriteria, currentPage + 1, 20));
        ticketGroupOrderComponent.insertTickets(tickets);
    }

    private void displayKanbanView() {
        EventBusFactory.getInstance().post(new TaskEvent.GotoKanbanView(this, null));
    }

    @Override
    public void enableActionControls(int numOfSelectedItem) {

    }

    @Override
    public void disableActionControls() {

    }

    @Override
    public HasSearchHandlers<ProjectTicketSearchCriteria> getSearchHandlers() {
        return ticketSearchPanel;
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        return null;
    }

    @Override
    public HasMassItemActionHandler getPopupActionHandlers() {
        return null;
    }

    @Override
    public HasSelectableItemHandlers<ProjectTicket> getSelectableItemHandlers() {
        return null;
    }

    @Override
    public AbstractPagedBeanTable<ProjectTicketSearchCriteria, ProjectTicket> getPagedBeanTable() {
        return null;
    }
}