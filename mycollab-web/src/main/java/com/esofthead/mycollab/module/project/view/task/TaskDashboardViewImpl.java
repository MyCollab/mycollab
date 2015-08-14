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
import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.service.OptionValService;
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
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.SavedFilterComboBox;
import com.esofthead.mycollab.vaadin.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.ValueComboBox;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TaskDashboardViewImpl extends AbstractLazyPageView implements TaskDashboardView {
    private static final long serialVersionUID = 1L;

    static final String DESCENDING = "Descending";
    static final String ASCENDING = "Ascending";

    static final String GROUP_DUE_DATE = "Due Date";
    static final String GROUP_START_DATE = "Start Date";
    static final String PLAIN_LIST = "Plain";

    private int currentPage = 0;

    private String groupByState;
    private String sortDirection;
    private TaskSearchCriteria baseCriteria;

    private TaskSearchPanel taskSearchPanel;
    private VerticalLayout wrapBody;
    private VerticalLayout rightColumn;
    private MHorizontalLayout mainLayout;
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
                        taskGroupOrderComponent.insertTasks(Arrays.asList(task));
                    }
                }
            };

    public TaskDashboardViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));
        taskSearchPanel = new TaskSearchPanel();
        MHorizontalLayout groupWrapLayout = new MHorizontalLayout();

        groupWrapLayout.addComponent(new Label("Filter:"));
        final SavedFilterComboBox savedFilterComboBox = new SavedFilterComboBox(ProjectTypeConstants.TASK);
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
                    TaskSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(TaskSearchCriteria.class,
                            fieldInfos);
                    criteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(TaskDashboardViewImpl.this, criteria));
                }
            }
        });
        groupWrapLayout.addComponent(savedFilterComboBox);

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
        final ComboBox groupCombo = new ValueComboBox(false, GROUP_DUE_DATE, GROUP_START_DATE, PLAIN_LIST);
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

        Button newTaskBtn = new Button(AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                UI.getCurrent().addWindow(new TaskAddWindow());
            }
        });
        newTaskBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        newTaskBtn.setIcon(FontAwesome.PLUS);
        newTaskBtn.setDescription(AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASKGROUP));
        newTaskBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        groupWrapLayout.addComponent(newTaskBtn);

        Button advanceDisplayBtn = new Button();
        advanceDisplayBtn.setIcon(FontAwesome.SITEMAP);
        advanceDisplayBtn.setDescription(AppContext.getMessage(TaskGroupI18nEnum.ADVANCED_VIEW_TOOLTIP));

        Button chartDisplayBtn = new Button(null, new Button.ClickListener() {
            private static final long serialVersionUID = -5707546605789537298L;

            @Override
            public void buttonClick(ClickEvent event) {
                displayGanttChartView();
            }
        });
        chartDisplayBtn.setDescription("Display Gantt chart");
        chartDisplayBtn.setIcon(FontAwesome.BAR_CHART_O);

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
        viewButtons.addButton(chartDisplayBtn);
        viewButtons.setDefaultButton(advanceDisplayBtn);
        groupWrapLayout.addComponent(viewButtons);

        mainLayout = new MHorizontalLayout().withFullHeight().withFullWidth();
        wrapBody = new VerticalLayout();
        wrapBody.setSpacing(true);

        rightColumn = new MVerticalLayout().withWidth("300px").withMargin(false);

        mainLayout.with(wrapBody, rightColumn).expand(wrapBody);

        FloatingComponent floatSidebar = FloatingComponent.floatThis(this.rightColumn);
        floatSidebar.setContainerId("main-body");
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

    private void constructUI() {
        this.with(taskSearchPanel, mainLayout);
    }

    @Override
    protected void displayView() {
        constructUI();
        baseCriteria = new TaskSearchCriteria();
        baseCriteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        OptionValService optionValService = ApplicationContextUtil.getSpringBean(OptionValService.class);
        List<OptionVal> options = optionValService.findOptionValsExcludeClosed(ProjectTypeConstants.TASK,
                CurrentProjectVariables.getProjectId(), AppContext.getAccountId());

        SetSearchField<String> statuses = new SetSearchField<>();
        for (OptionVal option : options) {
            statuses.addValue(option.getTypeval());
        }
        baseCriteria.setStatuses(statuses);
        queryAndDisplayTasks();
        displayTaskStatistic();
    }

    private void displayTaskStatistic() {
        rightColumn.removeAllComponents();
        UnresolvedTaskByAssigneeWidget unresolvedTaskByAssigneeWidget = new UnresolvedTaskByAssigneeWidget();
        rightColumn.addComponent(unresolvedTaskByAssigneeWidget);

        unresolvedTaskByAssigneeWidget.setSearchCriteria(baseCriteria);

        UnresolvedTaskByPriorityWidget unresolvedTaskByPriorityWidget = new UnresolvedTaskByPriorityWidget();
        rightColumn.addComponent(unresolvedTaskByPriorityWidget);
        unresolvedTaskByPriorityWidget.setSearchCriteria(baseCriteria);
    }

    @Override
    public void queryTask(final TaskSearchCriteria searchCriteria) {
        baseCriteria = searchCriteria;
        wrapBody.removeAllComponents();

        if (GROUP_DUE_DATE.equals(groupByState)) {
            searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("deadline", sortDirection)));
            taskGroupOrderComponent = new DueDateOrderComponent();
        } else if (GROUP_START_DATE.equals(groupByState)) {
            searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("startdate", sortDirection)));
            taskGroupOrderComponent = new StartDateOrderComponent();
        } else if (PLAIN_LIST.equals(groupByState)) {
            searchCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("lastupdatedtime", sortDirection)));
            taskGroupOrderComponent = new SimpleListOrderComponent();
        } else {
            throw new MyCollabException("Do not support group view by " + groupByState);
        }
        wrapBody.addComponent(taskGroupOrderComponent);
        final ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
        int totalTasks = projectTaskService.getTotalCount(searchCriteria);
        currentPage = 0;
        int pages = totalTasks / 20;
        if (currentPage < pages) {
            Button moreBtn = new Button("More", new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    int totalTasks = projectTaskService.getTotalCount(searchCriteria);
                    int pages = totalTasks / 20;
                    currentPage++;
                    List<SimpleTask> otherTasks = projectTaskService.findPagableListByCriteria(new SearchRequest<>
                            (searchCriteria, currentPage + 1, 20));
                    taskGroupOrderComponent.insertTasks(otherTasks);
                    if (currentPage == pages) {
                        wrapBody.removeComponent(wrapBody.getComponent(1));
                    }
                }
            });
            moreBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
            wrapBody.addComponent(moreBtn);
        }
        List<SimpleTask> tasks = projectTaskService.findPagableListByCriteria(new SearchRequest<>(searchCriteria, currentPage + 1, 20));
        taskGroupOrderComponent.insertTasks(tasks);
    }

    private void queryAndDisplayTasks() {
        queryTask(baseCriteria);
    }

    private void displayGanttChartView() {
        EventBusFactory.getInstance().post(new TaskEvent.GotoGanttChart(this, null));
    }

    private void displayKanbanView() {
        EventBusFactory.getInstance().post(new TaskEvent.GotoKanbanView(this, null));
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
    public HasSearchHandlers<TaskSearchCriteria> getSearchHandlers() {
        return taskSearchPanel;
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
    public HasSelectableItemHandlers<SimpleTask> getSelectableItemHandlers() {
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }

    @Override
    public AbstractPagedBeanTable<TaskSearchCriteria, SimpleTask> getPagedBeanTable() {
        throw new UnsupportedOperationException("This view doesn't support this operation");
    }
}