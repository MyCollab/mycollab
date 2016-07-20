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
package com.mycollab.module.project.view.task;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.domain.OptionVal;
import com.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.QueryAnalyzer;
import com.mycollab.common.service.OptionValService;
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
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.events.TaskEvent;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.view.task.components.*;
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
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.QueryParamHandler;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.ValueComboBox;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
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
public class TaskDashboardViewImpl extends AbstractPageView implements TaskDashboardView {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TaskDashboardViewImpl.class);

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
                    final ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                    SimpleTask task = projectTaskService.findById((Integer) event.getData(), AppContext.getAccountId());
                    if (task != null && taskGroupOrderComponent != null) {
                        taskGroupOrderComponent.insertTasks(Collections.singletonList(task));
                    }
                    displayTaskStatistic();

                    int totalTasks = projectTaskService.getTotalCount(baseCriteria);
                    taskSearchPanel.setTotalCountNumber(totalTasks);
                }
            };

    private ApplicationEventListener<ShellEvent.AddQueryParam> addQueryHandler = QueryParamHandler.queryParamHandler();

    public TaskDashboardViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        taskSearchPanel = new TaskSearchPanel();

        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();
        groupWrapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        groupWrapLayout.addComponent(new ELabel(AppContext.getMessage(GenericI18Enum.ACTION_SORT)));
        final ComboBox sortCombo = new ValueComboBox(false, AppContext.getMessage(GenericI18Enum.OPT_SORT_DESCENDING),
                AppContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING));
        sortCombo.addValueChangeListener(valueChangeEvent -> {
            String sortValue = (String) sortCombo.getValue();
            if (AppContext.getMessage(GenericI18Enum.OPT_SORT_ASCENDING).equals(sortValue)) {
                sortDirection = SearchCriteria.ASC;
            } else {
                sortDirection = SearchCriteria.DESC;
            }
            queryAndDisplayTasks();
        });
        sortDirection = SearchCriteria.DESC;
        groupWrapLayout.addComponent(sortCombo);

        groupWrapLayout.addComponent(new ELabel(AppContext.getMessage(GenericI18Enum.OPT_GROUP)));
        final ComboBox groupCombo = new ValueComboBox(false, AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE),
                AppContext.getMessage(GenericI18Enum.FORM_START_DATE), AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME),
                AppContext.getMessage(GenericI18Enum.OPT_PLAIN), AppContext.getMessage(GenericI18Enum.OPT_USER));
        groupCombo.addValueChangeListener(valueChangeEvent -> {
            groupByState = (String) groupCombo.getValue();
            queryAndDisplayTasks();
        });
        groupByState = AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE);
        groupWrapLayout.addComponent(groupCombo);

        taskSearchPanel.addHeaderRight(groupWrapLayout);

        MButton printBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(
                new TaskCustomizeReportOutputWindow(new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return baseCriteria;
                    }
                }))).withIcon(FontAwesome.PRINT).withStyleName(UIConstants.BUTTON_OPTION)
                .withDescription(AppContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        groupWrapLayout.addComponent(printBtn);

        MButton newTaskBtn = new MButton(AppContext.getMessage(TaskI18nEnum.NEW), clickEvent -> {
            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                SimpleTask newTask = new SimpleTask();
                newTask.setProjectid(CurrentProjectVariables.getProjectId());
                newTask.setSaccountid(AppContext.getAccountId());
                newTask.setLogby(AppContext.getUsername());
                UI.getCurrent().addWindow(new TaskAddWindow(newTask));
            }
        }).withIcon(FontAwesome.PLUS).withStyleName(UIConstants.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        groupWrapLayout.addComponent(newTaskBtn);

        MButton advanceDisplayBtn = new MButton("List").withIcon(FontAwesome.SITEMAP).withWidth("100px");

        MButton kanbanBtn = new MButton("Kanban", clickEvent -> displayKanbanView()).withWidth("100px").withIcon(FontAwesome.TH);

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
        baseCriteria = new TaskSearchCriteria();
        baseCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        statisticSearchCriteria = BeanUtility.deepClone(baseCriteria);

        OptionValService optionValService = AppContextUtil.getSpringBean(OptionValService.class);
        List<OptionVal> options = optionValService.findOptionValsExcludeClosed(ProjectTypeConstants.TASK,
                CurrentProjectVariables.getProjectId(), AppContext.getAccountId());

        if (CollectionUtils.isNotEmpty(options)) {
            SetSearchField<String> statuses = new SetSearchField<>();
            for (OptionVal option : options) {
                statuses.addValue(option.getTypeval());
            }
            statisticSearchCriteria.setStatuses(statuses);
        }

        if (StringUtils.isNotBlank(query)) {
            try {
                String jsonQuery = UrlEncodeDecoder.decode(query);
                List<SearchFieldInfo> searchFieldInfos = QueryAnalyzer.toSearchFieldInfos(jsonQuery, ProjectTypeConstants.TASK);
                taskSearchPanel.displaySearchFieldInfos(searchFieldInfos);
                TaskSearchCriteria searchCriteria = SearchFieldInfo.buildSearchCriteria(baseCriteria, searchFieldInfos);
                searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                queryTask(searchCriteria);
            } catch (Exception e) {
                LOG.error("Error", e);
                taskSearchPanel.selectQueryInfo(TaskSavedFilterComboBox.OPEN_TASKS);
            }
        } else {
            taskSearchPanel.selectQueryInfo(TaskSavedFilterComboBox.OPEN_TASKS);
        }
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

        if (AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("deadline", sortDirection)));
            taskGroupOrderComponent = new DueDateOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.FORM_START_DATE).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("startdate", sortDirection)));
            taskGroupOrderComponent = new StartDateOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.OPT_PLAIN).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("lastupdatedtime", sortDirection)));
            taskGroupOrderComponent = new SimpleListOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdtime", sortDirection)));
            taskGroupOrderComponent = new CreatedDateOrderComponent();
        } else if (AppContext.getMessage(GenericI18Enum.OPT_USER).equals(groupByState)) {
            baseCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("createdtime", sortDirection)));
            taskGroupOrderComponent = new UserOrderComponent();
        } else {
            throw new MyCollabException("Do not support group view by " + groupByState);
        }
        wrapBody.addComponent(taskGroupOrderComponent);
        final ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
        int totalTasks = projectTaskService.getTotalCount(baseCriteria);
        taskSearchPanel.setTotalCountNumber(totalTasks);
        currentPage = 0;
        int pages = totalTasks / 20;
        if (currentPage < pages) {
            Button moreBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_MORE), clickEvent -> {
                int newTotalTasks = projectTaskService.getTotalCount(baseCriteria);
                int newNumPages = newTotalTasks / 20;
                currentPage++;
                List<SimpleTask> otherTasks = projectTaskService.findPageableListByCriteria(new BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
                taskGroupOrderComponent.insertTasks(otherTasks);
                if (currentPage >= newNumPages) {
                    wrapBody.removeComponent(wrapBody.getComponent(1));
                }
            });
            moreBtn.addStyleName(UIConstants.BUTTON_ACTION);
            wrapBody.addComponent(moreBtn);
        }
        List<SimpleTask> tasks = projectTaskService.findPageableListByCriteria(new BasicSearchRequest<>(baseCriteria, currentPage + 1, 20));
        taskGroupOrderComponent.insertTasks(tasks);
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