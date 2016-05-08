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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.esofthead.mycollab.common.service.OptionValService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.db.query.VariableInjector;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.reporting.StreamResourceUtils;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.task.components.*;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
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
public class TaskDashboardViewImpl extends AbstractPageView implements TaskDashboardView {
    private static final long serialVersionUID = 1L;

    static final String DESCENDING = "Descending";
    static final String ASCENDING = "Ascending";

    static final String GROUP_DUE_DATE = "Due Date";
    static final String GROUP_START_DATE = "Start Date";
    static final String GROUP_CREATED_DATE = "Created Date";
    static final String PLAIN_LIST = "Plain";
    static final String GROUP_USER = "User";

    private int currentPage = 0;

    private String groupByState;
    private String sortDirection;

    private TaskSearchCriteria baseCriteria;
    private TaskSearchCriteria statisticSearchCriteria;

    private TaskSearchPanel taskSearchPanel;
    private MVerticalLayout wrapBody;
    private VerticalLayout rightColumn;
    private TaskGroupOrderComponent taskGroupOrderComponent;

    private ApplicationEventListener<TaskEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<TaskEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(TaskEvent.SearchRequest event) {
                    TaskSearchCriteria criteria = (TaskSearchCriteria) event.getData();
                    if (criteria != null) {
                        queryTask(criteria);
                    }
                }
            };

    private ApplicationEventListener<TaskEvent.NewTaskAdded> newTaskAddedHandler = new
            ApplicationEventListener<TaskEvent.NewTaskAdded>() {
                @Override
                @Subscribe
                public void handle(TaskEvent.NewTaskAdded event) {
                    final ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    SimpleTask task = projectTaskService.findById((Integer) event.getData(), AppContext.getAccountId());
                    if (task != null && taskGroupOrderComponent != null) {
                        taskGroupOrderComponent.insertTasks(Collections.singletonList(task));
                    }
                    displayTaskStatistic();

                    int totalTasks = projectTaskService.getTotalCount(baseCriteria);
                    taskSearchPanel.setTotalCountNumber(totalTasks);
                }
            };

    public TaskDashboardViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        taskSearchPanel = new TaskSearchPanel();

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
                queryAndDisplayTasks();
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
                queryAndDisplayTasks();
            }
        });
        groupByState = GROUP_DUE_DATE;
        groupWrapLayout.addComponent(groupCombo);

        taskSearchPanel.addHeaderRight(groupWrapLayout);

        MButton exportPdfBtn = new MButton("").withIcon(FontAwesome.FILE_PDF_O).withStyleName(UIConstants.BUTTON_OPTION)
                .withDescription("Export to PDF");
        FileDownloader pdfFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.PDF));
        pdfFileDownloader.extend(exportPdfBtn);

        MButton exportExcelBtn = new MButton("").withIcon(FontAwesome.FILE_EXCEL_O).withStyleName(UIConstants.BUTTON_OPTION).withDescription("Export to Excel");
        FileDownloader excelFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.EXCEL));
        excelFileDownloader.extend(exportExcelBtn);

        MButton exportCsvBtn = new MButton("").withIcon(FontAwesome.FILE_TEXT_O).withStyleName(UIConstants.BUTTON_OPTION).withDescription("Export to CSV");
        FileDownloader csvFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.CSV));
        csvFileDownloader.extend(exportCsvBtn);

        ButtonGroup exportButtonGroup = new ButtonGroup();
        exportButtonGroup.addButton(exportPdfBtn);
        exportButtonGroup.addButton(exportExcelBtn);
        exportButtonGroup.addButton(exportCsvBtn);

        groupWrapLayout.with(exportButtonGroup);

        Button newTaskBtn = new Button(AppContext.getMessage(TaskI18nEnum.NEW), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    SimpleTask newTask = new SimpleTask();
                    newTask.setProjectid(CurrentProjectVariables.getProjectId());
                    newTask.setSaccountid(AppContext.getAccountId());
                    newTask.setLogby(AppContext.getUsername());
                    UI.getCurrent().addWindow(new TaskAddWindow(newTask));
                }
            }
        });
        newTaskBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        newTaskBtn.setIcon(FontAwesome.PLUS);
        newTaskBtn.setStyleName(UIConstants.BUTTON_ACTION);
        groupWrapLayout.addComponent(newTaskBtn);

        Button advanceDisplayBtn = new Button("List");
        advanceDisplayBtn.setWidth("100px");
        advanceDisplayBtn.setIcon(FontAwesome.SITEMAP);
        advanceDisplayBtn.setDescription("Advance View");

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
        rightColumn = new MVerticalLayout().withWidth("370px").withMargin(new MarginInfo(true, false, false, false));
        mainLayout.with(wrapBody, rightColumn).expand(wrapBody);
        this.with(taskSearchPanel, mainLayout);
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        EventBusFactory.getInstance().register(newTaskAddedHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(searchHandler);
        EventBusFactory.getInstance().unregister(newTaskAddedHandler);
        super.detach();
    }

    public void displayView() {
        baseCriteria = new TaskSearchCriteria();
        baseCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        OptionValService optionValService = ApplicationContextUtil.getSpringBean(OptionValService.class);
        List<OptionVal> options = optionValService.findOptionValsExcludeClosed(ProjectTypeConstants.TASK,
                CurrentProjectVariables.getProjectId(), AppContext.getAccountId());

        SetSearchField<String> statuses = new SetSearchField<>();
        for (OptionVal option : options) {
            statuses.addValue(option.getTypeval());
        }
        baseCriteria.setStatuses(statuses);
        statisticSearchCriteria = BeanUtility.deepClone(baseCriteria);

        taskSearchPanel.selectQueryInfo(TaskSavedFilterComboBox.OPEN_TASKS);
    }

    @Override
    public void showNoItemView() {

    }

    private void displayTaskStatistic() {
        rightColumn.removeAllComponents();
        final TaskStatusTrendChartWidget taskStatusTrendChartWidget = new TaskStatusTrendChartWidget();
        rightColumn.addComponent(taskStatusTrendChartWidget);
        UnresolvedTaskByAssigneeWidget unresolvedTaskByAssigneeWidget = new UnresolvedTaskByAssigneeWidget();
        unresolvedTaskByAssigneeWidget.setSearchCriteria(statisticSearchCriteria);
        rightColumn.addComponent(unresolvedTaskByAssigneeWidget);

        UnresolvedTaskByPriorityWidget unresolvedTaskByPriorityWidget = new UnresolvedTaskByPriorityWidget();
        unresolvedTaskByPriorityWidget.setSearchCriteria(statisticSearchCriteria);
        rightColumn.addComponent(unresolvedTaskByPriorityWidget);

        UnresolvedTaskByStatusWidget unresolvedTaskByStatusWidget = new UnresolvedTaskByStatusWidget();
        unresolvedTaskByStatusWidget.setSearchCriteria(statisticSearchCriteria);
        rightColumn.addComponent(unresolvedTaskByStatusWidget);

        AsyncInvoker.access(new AsyncInvoker.PageCommand() {
            @Override
            public void run() {
                TimelineTrackingSearchCriteria timelineTrackingSearchCriteria = new TimelineTrackingSearchCriteria();
                timelineTrackingSearchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                taskStatusTrendChartWidget.display(timelineTrackingSearchCriteria);
            }
        });
    }

    @Override
    public void queryTask(final TaskSearchCriteria searchCriteria) {
        baseCriteria = searchCriteria;
        queryAndDisplayTasks();
        displayTaskStatistic();
    }

    private void queryAndDisplayTasks() {
        wrapBody.removeAllComponents();

        if (GROUP_DUE_DATE.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("deadline", sortDirection)));
            taskGroupOrderComponent = new DueDateOrderComponent();
        } else if (GROUP_START_DATE.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("startdate", sortDirection)));
            taskGroupOrderComponent = new StartDateOrderComponent();
        } else if (PLAIN_LIST.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("lastupdatedtime", sortDirection)));
            taskGroupOrderComponent = new SimpleListOrderComponent();
        } else if (GROUP_CREATED_DATE.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdtime", sortDirection)));
            taskGroupOrderComponent = new CreatedDateOrderComponent();
        } else if (GROUP_USER.equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdtime", sortDirection)));
            taskGroupOrderComponent = new UserOrderComponent();
        } else {
            throw new MyCollabException("Do not support group view by " + groupByState);
        }
        wrapBody.addComponent(taskGroupOrderComponent);
        final ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
        int totalTasks = projectTaskService.getTotalCount(baseCriteria);
        taskSearchPanel.setTotalCountNumber(totalTasks);
        currentPage = 0;
        int pages = totalTasks / 20;
        if (currentPage < pages) {
            Button moreBtn = new Button("More", new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    int totalTasks = projectTaskService.getTotalCount(baseCriteria);
                    int pages = totalTasks / 20;
                    currentPage++;
                    List<SimpleTask> otherTasks = projectTaskService.findPagableListByCriteria(new BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
                    taskGroupOrderComponent.insertTasks(otherTasks);
                    if (currentPage == pages) {
                        wrapBody.removeComponent(wrapBody.getComponent(1));
                    }
                }
            });
            moreBtn.addStyleName(UIConstants.BUTTON_ACTION);
            wrapBody.addComponent(moreBtn);
        }
        List<SimpleTask> tasks = projectTaskService.findPagableListByCriteria(new BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
        taskGroupOrderComponent.insertTasks(tasks);
    }

    private void displayKanbanView() {
        EventBusFactory.getInstance().post(new TaskEvent.GotoKanbanView(this, null));
    }

    private StreamResource buildStreamSource(ReportExportType exportType) {
        return StreamResourceUtils.buildTaskStreamResource(exportType, new VariableInjector<TaskSearchCriteria>() {
            @Override
            public TaskSearchCriteria eval() {
                return baseCriteria;
            }
        });
    }

    @Override
    public void enableActionControls(int numOfSelectedItem) {

    }

    @Override
    public void disableActionControls() {

    }

    @Override
    public HasSearchHandlers<TaskSearchCriteria> getSearchHandlers() {
        return taskSearchPanel;
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
    public HasSelectableItemHandlers<SimpleTask> getSelectableItemHandlers() {
        return null;
    }

    @Override
    public AbstractPagedBeanTable<TaskSearchCriteria, SimpleTask> getPagedBeanTable() {
        return null;
    }
}