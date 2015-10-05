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

import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;
import com.esofthead.mycollab.core.utils.XStreamJsonDeSerializer;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.view.bug.components.*;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.ReportStreamSource;
import com.esofthead.mycollab.reporting.RpFieldsBuilder;
import com.esofthead.mycollab.reporting.SimpleReportTemplateExecutor;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private BugSearchPanel searchPanel;
    private MVerticalLayout wrapBody;
    private VerticalLayout rightColumn;
    private MHorizontalLayout mainLayout;
    private BugGroupOrderComponent bugGroupOrderComponent;

    private ApplicationEventListener<BugEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<BugEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(BugEvent.SearchRequest event) {
                    BugSearchCriteria criteria = (BugSearchCriteria) event.getData();
                    if (criteria != null) {
                        queryBug(criteria);
                    }
                }
            };

    public BugListViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        searchPanel = new BugSearchPanel();
        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        groupWrapLayout.addComponent(new Label("Filter:"));
        final SavedFilterComboBox savedFilterComboBox = new SavedFilterComboBox(ProjectTypeConstants.BUG);
        savedFilterComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                SaveSearchResultWithBLOBs item = (SaveSearchResultWithBLOBs) savedFilterComboBox.getValue();
                if (item != null) {
                    List<SearchFieldInfo> fieldInfos = (List<SearchFieldInfo>) XStreamJsonDeSerializer.fromJson(item.getQuerytext());
                    // @HACK: === the library serialize with extra list
                    // wrapper
                    if (CollectionUtils.isEmpty(fieldInfos)) {
                        throw new UserInvalidInputException("There is no field in search criterion");
                    }
                    fieldInfos = (List<SearchFieldInfo>) fieldInfos.get(0);
                    BugSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(BugSearchCriteria.class, fieldInfos);
                    criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new BugEvent.SearchRequest(BugListViewImpl.this, criteria));
                }
            }
        });
        groupWrapLayout.addComponent(savedFilterComboBox);

        groupWrapLayout.addComponent(new Label("Sort:"));
        final ComboBox sortCombo = new ValueComboBox(false, DESCENDING, ASCENDING);
        sortCombo.setWidth("100px");
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
        final ComboBox groupCombo = new ValueComboBox(false, GROUP_DUE_DATE, GROUP_START_DATE, PLAIN_LIST);
        groupCombo.setWidth("100px");
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

        Button exportBtn = new Button("Export");
        final SplitButton exportSplitBtn = new SplitButton(exportBtn);
        exportBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                exportSplitBtn.setPopupVisible(true);
            }
        });
        exportSplitBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        OptionPopupContent popupButtonsControl = new OptionPopupContent();

        Button exportPdfBtn = new Button("PDF");
        exportPdfBtn.setIcon(FontAwesome.FILE_PDF_O);
        FileDownloader pdfFileDownloder = new FileDownloader(buildStreamSource(ReportExportType.PDF));
        pdfFileDownloder.extend(exportPdfBtn);
        popupButtonsControl.addOption(exportPdfBtn);

        Button exportExcelBtn = new Button("Excel");
        exportExcelBtn.setIcon(FontAwesome.FILE_EXCEL_O);
        FileDownloader excelFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.EXCEL));
        excelFileDownloader.extend(exportExcelBtn);
        popupButtonsControl.addOption(exportExcelBtn);

        exportSplitBtn.setContent(popupButtonsControl);
        groupWrapLayout.with(exportSplitBtn);

        Button newBugBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(BugListViewImpl.this, null));
            }
        });
        newBugBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        newBugBtn.setIcon(FontAwesome.PLUS);
        newBugBtn.setDescription(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG));
        newBugBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        groupWrapLayout.addComponent(newBugBtn);

        Button advanceDisplayBtn = new Button();
        advanceDisplayBtn.setIcon(FontAwesome.SITEMAP);
        advanceDisplayBtn.setDescription("Detail");

        Button kanbanBtn = new Button(null, new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                displayKanbanView();
            }
        });
        kanbanBtn.setDescription("Kanban View");
        kanbanBtn.setIcon(FontAwesome.TH);

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(advanceDisplayBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.setDefaultButton(advanceDisplayBtn);
        groupWrapLayout.addComponent(viewButtons);

        mainLayout = new MHorizontalLayout().withFullHeight().withFullWidth();
        wrapBody = new MVerticalLayout().withMargin(new MarginInfo(false, true, true, false));

        this.rightColumn = new MVerticalLayout().withWidth("300px").withMargin(new MarginInfo(true, false, true, false));

        mainLayout.with(wrapBody, rightColumn).expand(wrapBody);
        this.with(searchPanel, mainLayout);

        FloatingComponent floatSidebar = FloatingComponent.floatThis(this.rightColumn);
        floatSidebar.setContainerId("main-body");
    }

    private StreamResource buildStreamSource(ReportExportType exportType) {
        List fields = Arrays.asList(BugTableFieldDef.summary(), BugTableFieldDef.environment(), BugTableFieldDef.priority(),
                BugTableFieldDef.severity(), BugTableFieldDef.status(), BugTableFieldDef.resolution(),
                BugTableFieldDef.logBy(), BugTableFieldDef.duedate(), BugTableFieldDef.assignUser(),
                BugTableFieldDef.billableHours(), BugTableFieldDef.nonBillableHours());
        SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>("Bugs", new
                RpFieldsBuilder(fields), exportType, SimpleBug.class, ApplicationContextUtil.getSpringBean(BugService.class));
        ReportStreamSource streamSource = new ReportStreamSource(reportTemplateExecutor) {
            @Override
            protected Map<String, Object> initReportParameters() {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("siteUrl", AppContext.getSiteUrl());
                parameters.put(SimpleReportTemplateExecutor.CRITERIA, baseCriteria);
                return parameters;
            }
        };
        return new StreamResource(streamSource, exportType.getDefaultFileName());
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(searchHandler);
        super.detach();
    }

    private void displayBugStatistic() {
        rightColumn.removeAllComponents();
        // Unresolved by assignee
        UnresolvedBugsByAssigneeWidget2 unresolvedByAssigneeWidget = new UnresolvedBugsByAssigneeWidget2();
        BugSearchCriteria unresolvedByAssigneeSearchCriteria = new BugSearchCriteria();
        unresolvedByAssigneeSearchCriteria.setProjectId(new NumberSearchField(
                CurrentProjectVariables.getProjectId()));
        unresolvedByAssigneeSearchCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.BugStatus.InProgress.name(),
                OptionI18nEnum.BugStatus.Open.name(), OptionI18nEnum.BugStatus.ReOpened.name()));
        unresolvedByAssigneeWidget.setSearchCriteria(unresolvedByAssigneeSearchCriteria);
        rightColumn.addComponent(unresolvedByAssigneeWidget);

        // Unresolve by priority widget
        UnresolvedBugsByPriorityWidget2 unresolvedByPriorityWidget = new UnresolvedBugsByPriorityWidget2();
        BugSearchCriteria unresolvedByPrioritySearchCriteria = new BugSearchCriteria();
        unresolvedByPrioritySearchCriteria.setProjectId(new NumberSearchField(
                CurrentProjectVariables.getProjectId()));
        unresolvedByPrioritySearchCriteria
                .setStatuses(new SetSearchField<>(OptionI18nEnum.BugStatus.InProgress.name(),
                        OptionI18nEnum.BugStatus.Open.name(), OptionI18nEnum.BugStatus.ReOpened.name()));
        unresolvedByPriorityWidget.setSearchCriteria(unresolvedByPrioritySearchCriteria);
        rightColumn.addComponent(unresolvedByPriorityWidget);
    }

    private void queryAndDisplayBugs() {
        queryBug(baseCriteria);
    }

    @Override
    public void queryBug(final BugSearchCriteria searchCriteria) {
        baseCriteria = searchCriteria;
        wrapBody.removeAllComponents();
        if (GROUP_DUE_DATE.equals(groupByState)) {
            searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("duedate", sortDirection)));
            bugGroupOrderComponent = new DueDateOrderComponent();
        } else if (GROUP_START_DATE.equals(groupByState)) {
            searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("createdTime", sortDirection)));
            bugGroupOrderComponent = new StartDateOrderComponent();
        } else if (PLAIN_LIST.equals(groupByState)) {
            searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("lastUpdatedTime", sortDirection)));
            bugGroupOrderComponent = new SimpleListOrderComponent();
        } else {
            throw new MyCollabException("Do not support group view by " + groupByState);
        }
        wrapBody.addComponent(bugGroupOrderComponent);
        final BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
        int totalBugs = bugService.getTotalCount(searchCriteria);
        currentPage = 0;
        int pages = totalBugs / 20;
        if (currentPage < pages) {
            Button moreBtn = new Button("More", new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    int totalTasks = bugService.getTotalCount(searchCriteria);
                    int pages = totalTasks / 20;
                    currentPage++;
                    List<SimpleBug> otherBugs = bugService.findPagableListByCriteria(new SearchRequest<>
                            (searchCriteria, currentPage + 1, 20));
                    bugGroupOrderComponent.insertBugs(otherBugs);
                    if (currentPage == pages) {
                        wrapBody.removeComponent(wrapBody.getComponent(1));
                    }
                }
            });
            moreBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
            wrapBody.addComponent(moreBtn);
        }
        List<SimpleBug> bugs = bugService.findPagableListByCriteria(new SearchRequest<>(searchCriteria, currentPage + 1, 20));
        bugGroupOrderComponent.insertBugs(bugs);
        displayBugStatistic();
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
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }

    @Override
    public void enableActionControls(int numOfSelectedItem) {
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }

    @Override
    public void disableActionControls() {
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }

    @Override
    public HasMassItemActionHandler getPopupActionHandlers() {
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }

    @Override
    public AbstractPagedBeanTable<BugSearchCriteria, SimpleBug> getPagedBeanTable() {
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }
}
