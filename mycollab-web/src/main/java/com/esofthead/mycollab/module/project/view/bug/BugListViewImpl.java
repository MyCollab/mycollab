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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.db.query.VariableInjector;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.reporting.StreamResourceUtils;
import com.esofthead.mycollab.module.project.view.bug.components.*;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.AsyncInvoker;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.ValueComboBox;
import com.esofthead.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.peter.buttongroup.ButtonGroup;
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

    static final String DESCENDING = "Descending";
    static final String ASCENDING = "Ascending";

    private int currentPage = 0;

    private String groupByState;
    private String sortDirection;
    private BugSearchCriteria baseCriteria;
    private BugSearchCriteria statisticSearchCriteria;

    private BugSearchPanel searchPanel;
    private MVerticalLayout wrapBody;
    private VerticalLayout rightColumn;
    private BugGroupOrderComponent bugGroupOrderComponent;

    private ApplicationEventListener<BugEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<BugEvent.SearchRequest>() {
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

    private ApplicationEventListener<BugEvent.NewBugAdded> newBugHandler = new
            ApplicationEventListener<BugEvent.NewBugAdded>() {
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

    public BugListViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        searchPanel = new BugSearchPanel();
        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        groupWrapLayout.addComponent(new Label("Sort:"));
        final ComboBox sortCombo = new ValueComboBox(false, DESCENDING, ASCENDING);
        sortCombo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String sortValue = (String) sortCombo.getValue();
                if (ASCENDING.equals(sortValue)) {
                    sortDirection = SearchCriteria.ASC;
                } else {
                    sortDirection = SearchCriteria.DESC;
                }
                queryAndDisplayBugs();
            }
        });
        sortDirection = SearchCriteria.DESC;
        groupWrapLayout.addComponent(sortCombo);

        groupWrapLayout.addComponent(new Label("Group by:"));
        final ComboBox groupCombo = new ValueComboBox(false, GROUP_DUE_DATE, GROUP_START_DATE, GROUP_CREATED_DATE,
                PLAIN_LIST, GROUP_USER);
        groupCombo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                groupByState = (String) groupCombo.getValue();
                queryAndDisplayBugs();
            }
        });
        groupByState = GROUP_DUE_DATE;
        groupWrapLayout.addComponent(groupCombo);

        searchPanel.addHeaderRight(groupWrapLayout);

        MButton exportPdfBtn = new MButton("").withIcon(FontAwesome.FILE_PDF_O).withStyleName(UIConstants
                .BUTTON_OPTION).withDescription("Export to PDF");
        FileDownloader pdfFileDownloder = new FileDownloader(buildStreamSource(ReportExportType.PDF));
        pdfFileDownloder.extend(exportPdfBtn);

        MButton exportExcelBtn = new MButton("").withIcon(FontAwesome.FILE_EXCEL_O).withStyleName(UIConstants
                .BUTTON_OPTION).withDescription("Export to Excel");
        FileDownloader excelFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.EXCEL));
        excelFileDownloader.extend(exportExcelBtn);

        MButton exportCsvBtn = new MButton("").withIcon(FontAwesome.FILE_TEXT_O).withStyleName(UIConstants
                .BUTTON_OPTION).withDescription("Export to Csv");
        FileDownloader csvFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.CSV));
        csvFileDownloader.extend(exportCsvBtn);


        ButtonGroup exportGroup = new ButtonGroup();
        exportGroup.addButton(exportPdfBtn);
        exportGroup.addButton(exportExcelBtn);
        exportGroup.addButton(exportCsvBtn);

        groupWrapLayout.with(exportGroup);

        Button newBugBtn = new Button(AppContext.getMessage(BugI18nEnum.NEW), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                SimpleBug bug = new SimpleBug();
                bug.setProjectid(CurrentProjectVariables.getProjectId());
                bug.setSaccountid(AppContext.getAccountId());
                bug.setLogby(AppContext.getUsername());
                UI.getCurrent().addWindow(new BugAddWindow(bug));
            }
        });
        newBugBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        newBugBtn.setIcon(FontAwesome.PLUS);
        newBugBtn.setDescription(AppContext.getMessage(BugI18nEnum.NEW));
        newBugBtn.setStyleName(UIConstants.BUTTON_ACTION);
        groupWrapLayout.addComponent(newBugBtn);

        Button advanceDisplayBtn = new Button("List");
        advanceDisplayBtn.setWidth("100px");
        advanceDisplayBtn.setIcon(FontAwesome.SITEMAP);
        advanceDisplayBtn.setDescription("Detail");

        Button kanbanBtn = new Button("Kanban", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                displayKanbanView();
            }
        });
        kanbanBtn.setWidth("100px");
        kanbanBtn.setDescription("Kanban View");
        kanbanBtn.setIcon(FontAwesome.TH);

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

    private StreamResource buildStreamSource(ReportExportType exportType) {
        return StreamResourceUtils.buildBugStreamResource(exportType, new VariableInjector<BugSearchCriteria>() {
            @Override
            public BugSearchCriteria eval() {
                return baseCriteria;
            }
        });
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        EventBusFactory.getInstance().register(newBugHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(searchHandler);
        EventBusFactory.getInstance().unregister(newBugHandler);
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
        if (GROUP_DUE_DATE.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("duedate", sortDirection)));
            bugGroupOrderComponent = new DueDateOrderComponent();
        } else if (GROUP_START_DATE.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("m_tracker_bug.startdate", sortDirection)));
            bugGroupOrderComponent = new StartDateOrderComponent();
        } else if (PLAIN_LIST.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("lastUpdatedTime", sortDirection)));
            bugGroupOrderComponent = new SimpleListOrderComponent();
        } else if (GROUP_CREATED_DATE.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdTime", sortDirection)));
            bugGroupOrderComponent = new CreatedDateOrderComponent();
        } else if (GROUP_USER.equals(groupByState)) {
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
            Button moreBtn = new Button("More", new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    int totalTasks = bugService.getTotalCount(baseCriteria);
                    int pages = totalTasks / 20;
                    currentPage++;
                    List<SimpleBug> otherBugs = bugService.findPagableListByCriteria(new BasicSearchRequest<>
                            (baseCriteria, currentPage + 1, 20));
                    bugGroupOrderComponent.insertBugs(otherBugs);
                    if (currentPage == pages) {
                        wrapBody.removeComponent(wrapBody.getComponent(1));
                    }
                }
            });
            moreBtn.addStyleName(UIConstants.BUTTON_ACTION);
            wrapBody.addComponent(moreBtn);
        }
        List<SimpleBug> bugs = bugService.findPagableListByCriteria(new BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
        bugGroupOrderComponent.insertBugs(bugs);
        displayBugStatistic();
    }

    @Override
    public void displayView() {
        baseCriteria = new BugSearchCriteria();
        baseCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        baseCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.BugStatus.Open.name(), OptionI18nEnum.BugStatus.ReOpen.name()));
        statisticSearchCriteria = BeanUtility.deepClone(baseCriteria);
        searchPanel.selectQueryInfo(BugSavedFilterComboBox.OPEN_BUGS);
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
