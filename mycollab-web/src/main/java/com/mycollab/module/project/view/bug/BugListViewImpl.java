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
package com.mycollab.module.project.view.bug;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.QueryAnalyzer;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.events.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.view.bug.components.*;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.events.HasMassItemActionHandler;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.QueryParamHandler;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.ValueComboBox;
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
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class BugListViewImpl extends AbstractPageView implements BugListView {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(BugListViewImpl.class);

    private int currentPage = 0;

    private String groupByState;
    private String sortDirection;
    private BugSearchCriteria baseCriteria;
    private BugSearchCriteria statisticSearchCriteria;

    private BugSearchPanel searchPanel;
    private MVerticalLayout wrapBody;
    private VerticalLayout rightColumn;
    private BugGroupOrderComponent bugGroupOrderComponent;

    private ApplicationEventListener<BugEvent.SearchRequest> searchHandler = new ApplicationEventListener<BugEvent.SearchRequest>() {
        @Override
        @Subscribe
        public void handle(BugEvent.SearchRequest event) {
            BugSearchCriteria criteria = (BugSearchCriteria) event.getData();
            if (criteria != null) {
                baseCriteria = criteria;
                queryAndDisplayBugs();
            }
        }
    };

    private ApplicationEventListener<BugEvent.NewBugAdded> newBugHandler = new ApplicationEventListener<BugEvent.NewBugAdded>() {
        @Override
        @Subscribe
        public void handle(BugEvent.NewBugAdded event) {
            final BugService bugService = AppContextUtil.getSpringBean(BugService.class);
            SimpleBug bug = bugService.findById((Integer) event.getData(), AppContext.getAccountId());
            if (bug != null && bugGroupOrderComponent != null) {
                bugGroupOrderComponent.insertBugs(Collections.singletonList(bug));
            }
            displayBugStatistic();

            int totalTasks = bugService.getTotalCount(baseCriteria);
            searchPanel.setTotalCountNumber(totalTasks);
        }
    };

    private ApplicationEventListener<ShellEvent.AddQueryParam> addQueryHandler = QueryParamHandler.queryParamHandler();

    public BugListViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        searchPanel = new BugSearchPanel();
        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        groupWrapLayout.addComponent(new Label(AppContext.getMessage(GenericI18Enum.ACTION_SORT)));
        final ComboBox sortCombo = new ValueComboBox(false, AppContext.getMessage(GenericI18Enum.OPT_SORT_DESCENDING),
                AppContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING));
        sortCombo.addValueChangeListener(valueChangeEvent -> {
            String sortValue = (String) sortCombo.getValue();
            if (AppContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING).equals(sortValue)) {
                sortDirection = SearchCriteria.ASC;
            } else {
                sortDirection = SearchCriteria.DESC;
            }
            queryAndDisplayBugs();
        });
        sortDirection = SearchCriteria.DESC;
        groupWrapLayout.addComponent(sortCombo);

        groupWrapLayout.addComponent(new Label(AppContext.getMessage(GenericI18Enum.OPT_GROUP)));
        final ComboBox groupCombo = new ValueComboBox(false, AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE),
                AppContext.getMessage(GenericI18Enum.FORM_START_DATE), AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME),
                AppContext.getMessage(GenericI18Enum.OPT_PLAIN), AppContext.getMessage(GenericI18Enum.OPT_USER));
        groupCombo.addValueChangeListener(valueChangeEvent -> {
            groupByState = (String) groupCombo.getValue();
            queryAndDisplayBugs();
        });
        groupByState = AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE);
        groupWrapLayout.addComponent(groupCombo);

        searchPanel.addHeaderRight(groupWrapLayout);

        MButton printBtn = new MButton("", clickEvent -> {
            UI.getCurrent().addWindow(new BugCustomizeReportOutputWindow(new LazyValueInjector() {
                @Override
                protected Object doEval() {
                    return baseCriteria;
                }
            }));
        }).withIcon(FontAwesome.PRINT).withStyleName(WebUIConstants.BUTTON_OPTION);
        printBtn.setDescription(AppContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        groupWrapLayout.addComponent(printBtn);

        MButton newBugBtn = new MButton(AppContext.getMessage(BugI18nEnum.NEW), clickEvent -> {
            SimpleBug bug = new SimpleBug();
            bug.setProjectid(CurrentProjectVariables.getProjectId());
            bug.setSaccountid(AppContext.getAccountId());
            bug.setLogby(AppContext.getUsername());
            UI.getCurrent().addWindow(new BugAddWindow(bug));
        }).withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION).withDescription(AppContext.getMessage(BugI18nEnum.NEW))
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
        groupWrapLayout.addComponent(newBugBtn);

        MButton advanceDisplayBtn = new MButton("List").withWidth("100px").withIcon(FontAwesome.SITEMAP);
        advanceDisplayBtn.setDescription("Detail");

        MButton kanbanBtn = new MButton("Kanban", clickEvent -> displayKanbanView()).withIcon(FontAwesome.TH).withWidth("100px");
        kanbanBtn.setDescription("Kanban View");

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(advanceDisplayBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.withDefaultButton(advanceDisplayBtn);
        groupWrapLayout.addComponent(viewButtons);

        MHorizontalLayout mainLayout = new MHorizontalLayout().withFullHeight().withFullWidth();
        wrapBody = new MVerticalLayout().withMargin(new MarginInfo(false, true, true, false));

        rightColumn = new MVerticalLayout().withWidth("370px").withMargin(new MarginInfo(true, false, true, false));

        mainLayout.with(wrapBody, rightColumn).expand(wrapBody);
        this.with(searchPanel, mainLayout);
    }

    @Override
    public void showNoItemView() {

    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        EventBusFactory.getInstance().register(newBugHandler);
        EventBusFactory.getInstance().register(addQueryHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(searchHandler);
        EventBusFactory.getInstance().unregister(newBugHandler);
        EventBusFactory.getInstance().unregister(addQueryHandler);
        super.detach();
    }

    private void displayBugStatistic() {
        rightColumn.removeAllComponents();
        final BugStatusTrendChartWidget bugStatusTrendChartWidget = new BugStatusTrendChartWidget();
        rightColumn.addComponent(bugStatusTrendChartWidget);
        // Unresolved by assignee
        UnresolvedBugsByAssigneeWidget unresolvedByAssigneeWidget = new UnresolvedBugsByAssigneeWidget();
        BugSearchCriteria unresolvedByAssigneeSearchCriteria = BeanUtility.deepClone(statisticSearchCriteria);
        unresolvedByAssigneeWidget.setSearchCriteria(unresolvedByAssigneeSearchCriteria);
        rightColumn.addComponent(unresolvedByAssigneeWidget);

        // Unresolve by priority widget
        UnresolvedBugsByPriorityWidget unresolvedByPriorityWidget = new UnresolvedBugsByPriorityWidget();
        BugSearchCriteria unresolvedByPrioritySearchCriteria = BeanUtility.deepClone(statisticSearchCriteria);
        unresolvedByPriorityWidget.setSearchCriteria(unresolvedByPrioritySearchCriteria);
        rightColumn.addComponent(unresolvedByPriorityWidget);

        //Unresolved by status
        UnresolvedBugsByStatusWidget unresolvedBugsByStatusWidget = new UnresolvedBugsByStatusWidget();
        BugSearchCriteria unresolvedByStatusSearchCriteria = BeanUtility.deepClone(statisticSearchCriteria);
        unresolvedBugsByStatusWidget.setSearchCriteria(unresolvedByStatusSearchCriteria);
        rightColumn.addComponent(unresolvedBugsByStatusWidget);

        AsyncInvoker.access(new AsyncInvoker.PageCommand() {
            @Override
            public void run() {
                TimelineTrackingSearchCriteria timelineTrackingSearchCriteria = new TimelineTrackingSearchCriteria();
                timelineTrackingSearchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                bugStatusTrendChartWidget.display(timelineTrackingSearchCriteria);
            }
        });
    }

    private void queryAndDisplayBugs() {
        wrapBody.removeAllComponents();
        if (AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("duedate", sortDirection)));
            bugGroupOrderComponent = new DueDateOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.FORM_START_DATE).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("m_tracker_bug.startdate", sortDirection)));
            bugGroupOrderComponent = new StartDateOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.OPT_PLAIN).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("lastUpdatedTime", sortDirection)));
            bugGroupOrderComponent = new SimpleListOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdTime", sortDirection)));
            bugGroupOrderComponent = new CreatedDateOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.OPT_USER).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdTime", sortDirection)));
            bugGroupOrderComponent = new UserOrderComponent();
        } else {
            throw new MyCollabException("Do not support group view by " + groupByState);
        }
        wrapBody.addComponent(bugGroupOrderComponent);
        final BugService bugService = AppContextUtil.getSpringBean(BugService.class);
        int totalBugs = bugService.getTotalCount(baseCriteria);
        searchPanel.setTotalCountNumber(totalBugs);
        currentPage = 0;
        int pages = totalBugs / 20;
        if (currentPage < pages) {
            MButton moreBtn = new MButton(AppContext.getMessage(GenericI18Enum.ACTION_MORE), clickEvent -> {
                int innerTotalBugs = bugService.getTotalCount(baseCriteria);
                int innerPages = innerTotalBugs / 20;
                currentPage++;
                List<SimpleBug> otherBugs = bugService.findPageableListByCriteria(new BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
                bugGroupOrderComponent.insertBugs(otherBugs);
                if (currentPage == innerPages) {
                    wrapBody.removeComponent(wrapBody.getComponent(1));
                }
            }).withStyleName(WebUIConstants.BUTTON_ACTION);
            wrapBody.addComponent(moreBtn);
        }
        List<SimpleBug> bugs = bugService.findPageableListByCriteria(new BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
        bugGroupOrderComponent.insertBugs(bugs);
        displayBugStatistic();
    }

    @Override
    public void displayView(String query) {
        baseCriteria = new BugSearchCriteria();
        baseCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        statisticSearchCriteria = BeanUtility.deepClone(baseCriteria);
        statisticSearchCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.BugStatus.Open.name(), OptionI18nEnum.BugStatus.ReOpen.name()));
        if (StringUtils.isNotBlank(query)) {
            try {
                String jsonQuery = UrlEncodeDecoder.decode(query);
                List<SearchFieldInfo> searchFieldInfos = QueryAnalyzer.toSearchFieldInfos(jsonQuery, ProjectTypeConstants.BUG);
                searchPanel.displaySearchFieldInfos(searchFieldInfos);
                BugSearchCriteria searchCriteria = SearchFieldInfo.buildSearchCriteria(baseCriteria, searchFieldInfos);
                searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                queryBug(searchCriteria);
            } catch (Exception e) {
                LOG.error("Error", e);
                searchPanel.selectQueryInfo(BugSavedFilterComboBox.OPEN_BUGS);
            }
        } else {
            searchPanel.selectQueryInfo(BugSavedFilterComboBox.OPEN_BUGS);
        }
    }

    @Override
    public void queryBug(final BugSearchCriteria searchCriteria) {
        baseCriteria = searchCriteria;
        queryAndDisplayBugs();
    }

    @Override
    public HasSearchHandlers<BugSearchCriteria> getSearchHandlers() {
        return searchPanel;
    }

    private void displayKanbanView() {
        EventBusFactory.getInstance().post(new BugEvent.GotoKanbanView(this, null));
    }


    @Override
    public HasSelectableItemHandlers<SimpleBug> getSelectableItemHandlers() {
        return null;
    }

    @Override
    public void enableActionControls(int numOfSelectedItem) {

    }

    @Override
    public void disableActionControls() {

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
    public AbstractPagedBeanTable<BugSearchCriteria, SimpleBug> getPagedBeanTable() {
        return null;
    }
}
