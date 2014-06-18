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

import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.i18n.FileI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.reporting.ExportTaskListStreamResource;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TaskGroupDisplayViewImpl extends AbstractLazyPageView implements
		TaskGroupDisplayView {
	private static final long serialVersionUID = 1L;

	private PopupButton taskGroupSelection;
	private PopupButton taskSelection;
	private TaskGroupDisplayWidget taskLists;

	private Button reOrderBtn;

	private TaskGanttChart ganttChart;

	private PopupButton exportButtonControl;

	private VerticalLayout rightColumn;
	private VerticalLayout leftColumn;
	private TextField nameField;

	private TaskSearchViewImpl basicSearchView;

	private HorizontalLayout header;
	private HorizontalLayout mainLayout;
	private Button advanceDisplay;
	private Button simpleDisplay;
	private Button chartDisplay;
	private ToggleButtonGroup viewButtons;

	private boolean isSimpleDisplay;

	public TaskGroupDisplayViewImpl() {
		super();
	}

	private void implementTaskFilterButton() {

		this.taskSelection = new PopupButton(
				AppContext.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASKS));

		this.taskSelection.setEnabled(CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS));
		this.taskSelection.addStyleName("link");
		this.taskSelection.addStyleName("hdr-text");

		final VerticalLayout filterBtnLayout = new VerticalLayout();
		filterBtnLayout.setMargin(true);
		filterBtnLayout.setSpacing(true);
		filterBtnLayout.setWidth("200px");

		final Button allTasksFilterBtn = new Button(
				AppContext.getMessage(TaskGroupI18nEnum.FILTER_ALL_TASKS),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						taskSelection.setPopupVisible(false);
						taskSelection
								.setCaption(event.getButton().getCaption());
						displayAllTasks();
					}
				});
		allTasksFilterBtn.setStyleName("link");
		filterBtnLayout.addComponent(allTasksFilterBtn);

		final Button activeTasksFilterBtn = new Button(
				AppContext.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASKS),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						taskSelection.setPopupVisible(false);
						taskSelection
								.setCaption(event.getButton().getCaption());
						displayActiveTasksOnly();
					}
				});
		activeTasksFilterBtn.setStyleName("link");
		filterBtnLayout.addComponent(activeTasksFilterBtn);

		final Button pendingTasksFilterBtn = new Button(
				AppContext.getMessage(TaskGroupI18nEnum.FILTER_PENDING_TASKS),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						taskSelection.setPopupVisible(false);
						taskSelection
								.setCaption(event.getButton().getCaption());
						displayPendingTasksOnly();
					}
				});
		pendingTasksFilterBtn.setStyleName("link");
		filterBtnLayout.addComponent(pendingTasksFilterBtn);

		final Button archievedTasksFilterBtn = new Button(
				AppContext.getMessage(TaskGroupI18nEnum.FILTER_ARCHIEVED_TASKS),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						taskSelection.setPopupVisible(false);
						taskSelection
								.setCaption(event.getButton().getCaption());
						displayInActiveTasks();
					}
				});
		archievedTasksFilterBtn.setStyleName("link");
		filterBtnLayout.addComponent(archievedTasksFilterBtn);
		taskSelection.setContent(filterBtnLayout);

	}

	private void constructUI() {
		this.removeAllComponents();
		this.setMargin(new MarginInfo(false, true, true, true));
		this.setSpacing(true);

		header = new HorizontalLayout();
		header.setMargin(new MarginInfo(true, false, true, false));
		header.setStyleName("hdr-view");
		header.setSpacing(true);
		header.setWidth("100%");
		header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

		this.taskGroupSelection = new PopupButton(
				AppContext
						.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASK_GROUPS_TITLE));
		this.taskGroupSelection.setEnabled(CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS));
		this.taskGroupSelection.addStyleName("link");
		this.taskGroupSelection.addStyleName("hdr-text");
		final Image icon = new Image(null,
				MyCollabResource.newResource("icons/24/project/task.png"));
		header.addComponent(icon);
		header.addComponent(this.taskGroupSelection);
		header.setExpandRatio(this.taskGroupSelection, 1.0f);
		header.setComponentAlignment(this.taskGroupSelection,
				Alignment.MIDDLE_LEFT);

		final VerticalLayout filterBtnLayout = new VerticalLayout();
		filterBtnLayout.setMargin(true);
		filterBtnLayout.setSpacing(true);
		filterBtnLayout.setWidth("200px");

		final Button allTasksFilterBtn = new Button(
				AppContext
						.getMessage(TaskGroupI18nEnum.FILTER_ALL_TASK_GROUPS_TITLE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						TaskGroupDisplayViewImpl.this.taskGroupSelection
								.setPopupVisible(false);
						TaskGroupDisplayViewImpl.this.taskGroupSelection.setCaption(AppContext
								.getMessage(TaskGroupI18nEnum.FILTER_ALL_TASK_GROUPS_TITLE));
						TaskGroupDisplayViewImpl.this.displayAllTaskGroups();
					}
				});
		allTasksFilterBtn.setStyleName("link");
		filterBtnLayout.addComponent(allTasksFilterBtn);

		final Button activeTasksFilterBtn = new Button(
				AppContext
						.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASK_GROUPS_TITLE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						TaskGroupDisplayViewImpl.this.taskGroupSelection
								.setPopupVisible(false);
						TaskGroupDisplayViewImpl.this.taskGroupSelection.setCaption(AppContext
								.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASK_GROUPS_TITLE));
						TaskGroupDisplayViewImpl.this.displayActiveTaskGroups();
					}
				});
		activeTasksFilterBtn.setStyleName("link");
		filterBtnLayout.addComponent(activeTasksFilterBtn);

		final Button archievedTasksFilterBtn = new Button(
				AppContext
						.getMessage(TaskGroupI18nEnum.FILTER_ARCHIEVED_TASK_GROUPS_TITLE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						TaskGroupDisplayViewImpl.this.taskGroupSelection.setCaption(AppContext
								.getMessage(TaskGroupI18nEnum.FILTER_ARCHIEVED_TASK_GROUPS_TITLE));
						TaskGroupDisplayViewImpl.this.taskGroupSelection
								.setPopupVisible(false);
						TaskGroupDisplayViewImpl.this
								.displayInActiveTaskGroups();
					}
				});
		archievedTasksFilterBtn.setStyleName("link");
		filterBtnLayout.addComponent(archievedTasksFilterBtn);

		this.taskGroupSelection.setContent(filterBtnLayout);

		final Button newTaskListBtn = new Button(
				AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASKGROUP),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						final TaskGroupAddWindow taskListWindow = new TaskGroupAddWindow(
								TaskGroupDisplayViewImpl.this);
						UI.getCurrent().addWindow(taskListWindow);
					}
				});
		newTaskListBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS));
		newTaskListBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		newTaskListBtn.setDescription(AppContext
				.getMessage(TaskI18nEnum.BUTTON_NEW_TASKGROUP));
		newTaskListBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		header.addComponent(newTaskListBtn);
		header.setComponentAlignment(newTaskListBtn, Alignment.MIDDLE_RIGHT);

		this.reOrderBtn = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				EventBus.getInstance().fireEvent(
						new TaskListEvent.ReoderTaskList(this, null));
			}
		});
		this.reOrderBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS));
		this.reOrderBtn.setIcon(MyCollabResource
				.newResource("icons/16/project/reorder.png"));
		this.reOrderBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		this.reOrderBtn.setDescription(AppContext
				.getMessage(TaskI18nEnum.BUTTON_REODER_TASKGROUP));
		header.addComponent(this.reOrderBtn);
		header.setComponentAlignment(this.reOrderBtn, Alignment.MIDDLE_RIGHT);

		exportButtonControl = new PopupButton();
		exportButtonControl.addStyleName(UIConstants.THEME_BLUE_LINK);
		exportButtonControl.setIcon(MyCollabResource
				.newResource("icons/16/export.png"));
		exportButtonControl.setDescription("Export to file");

		VerticalLayout popupButtonsControl = new VerticalLayout();
		exportButtonControl.setContent(popupButtonsControl);
		exportButtonControl.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PIXELS);

		Button exportPdfBtn = new Button(
				AppContext.getMessage(FileI18nEnum.PDF));
		FileDownloader pdfDownloader = new FileDownloader(
				constructStreamResource(ReportExportType.PDF));
		pdfDownloader.extend(exportPdfBtn);
		exportPdfBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/pdf.png"));
		exportPdfBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportPdfBtn);

		Button exportExcelBtn = new Button(
				AppContext.getMessage(FileI18nEnum.EXCEL));
		FileDownloader excelDownloader = new FileDownloader(
				constructStreamResource(ReportExportType.EXCEL));
		excelDownloader.extend(exportExcelBtn);
		exportExcelBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/excel.png"));
		exportExcelBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportExcelBtn);

		header.addComponent(exportButtonControl);
		header.setComponentAlignment(exportButtonControl,
				Alignment.MIDDLE_RIGHT);

		advanceDisplay = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				advanceDisplay.addStyleName(UIConstants.BTN_ACTIVE);
				simpleDisplay.removeStyleName(UIConstants.BTN_ACTIVE);
				chartDisplay.removeStyleName(UIConstants.BTN_ACTIVE);
				displayAdvancedView();
			}
		});
		advanceDisplay.setIcon(MyCollabResource
				.newResource("icons/16/project/advanced_display.png"));
		advanceDisplay.addStyleName(UIConstants.BTN_ACTIVE);
		advanceDisplay.setDescription(AppContext
				.getMessage(TaskGroupI18nEnum.ADVANCED_VIEW_TOOLTIP));

		simpleDisplay = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				advanceDisplay.removeStyleName(UIConstants.BTN_ACTIVE);
				chartDisplay.removeStyleName(UIConstants.BTN_ACTIVE);
				simpleDisplay.addStyleName(UIConstants.BTN_ACTIVE);
				displaySimpleView();
			}
		});
		simpleDisplay.setIcon(MyCollabResource
				.newResource("icons/16/project/list_display.png"));
		simpleDisplay.setDescription(AppContext
				.getMessage(TaskGroupI18nEnum.LIST_VIEW_TOOLTIP));

		chartDisplay = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = -5707546605789537298L;

			@Override
			public void buttonClick(ClickEvent event) {
				advanceDisplay.removeStyleName(UIConstants.BTN_ACTIVE);
				simpleDisplay.removeStyleName(UIConstants.BTN_ACTIVE);
				chartDisplay.addStyleName(UIConstants.BTN_ACTIVE);
				displayGanttChartView();
			}
		});
		chartDisplay.setIcon(MyCollabResource
				.newResource("icons/16/project/chart_view.png"));

		viewButtons = new ToggleButtonGroup();
		viewButtons.addButton(simpleDisplay);
		viewButtons.addButton(advanceDisplay);
		viewButtons.addButton(chartDisplay);

		mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
		this.taskLists = new TaskGroupDisplayWidget();

		this.leftColumn = new VerticalLayout();
		this.leftColumn.addComponent(taskLists);
		this.leftColumn.setMargin(new MarginInfo(false, true, false, false));

		this.rightColumn = new VerticalLayout();
		this.rightColumn.setWidth("300px");
		this.rightColumn.setMargin(new MarginInfo(true, false, false, false));

		mainLayout.addComponent(leftColumn);
		mainLayout.addComponent(rightColumn);
		mainLayout.setExpandRatio(leftColumn, 1.0f);

		implementTaskFilterButton();
		basicSearchView = new TaskSearchViewImpl();
		basicSearchView.getSearchHandlers().addSearchHandler(
				new SearchHandler<TaskSearchCriteria>() {
					@Override
					public void onSearch(TaskSearchCriteria criteria) {
						doSearch(criteria);
					}
				});
		basicSearchView.removeComponent(basicSearchView.getComponent(0));

		displayAdvancedView();
		isSimpleDisplay = false;
	}

	private void doSearch(TaskSearchCriteria searchCriteria) {
		basicSearchView.getPagedBeanTable().setSearchCriteria(searchCriteria);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private StreamResource constructStreamResource(ReportExportType exportType) {
		final String title = "Task report of Project "
				+ ((CurrentProjectVariables.getProject() != null && CurrentProjectVariables
						.getProject().getName() != null) ? CurrentProjectVariables
						.getProject().getName() : "");

		final TaskListSearchCriteria tasklistSearchCriteria = new TaskListSearchCriteria();
		tasklistSearchCriteria.setProjectId(new NumberSearchField(
				SearchField.AND, CurrentProjectVariables.getProject().getId()));

		StreamResource res = null;
		if (exportType.equals(ReportExportType.PDF)) {
			res = new StreamResource(new ExportTaskListStreamResource(title,
					exportType,
					ApplicationContextUtil
							.getSpringBean(ProjectTaskListService.class),
					tasklistSearchCriteria, null), "task_list.pdf");
		} else if (exportType.equals(ReportExportType.CSV)) {
			res = new StreamResource(new ExportTaskListStreamResource(title,
					exportType,
					ApplicationContextUtil
							.getSpringBean(ProjectTaskListService.class),
					tasklistSearchCriteria, null), "task_list.csv");
		} else {
			res = new StreamResource(new ExportTaskListStreamResource(title,
					exportType,
					ApplicationContextUtil
							.getSpringBean(ProjectTaskListService.class),
					tasklistSearchCriteria, null), "task_list.xls");
		}

		return res;
	}

	private TaskListSearchCriteria createBaseSearchCriteria() {
		final TaskListSearchCriteria criteria = new TaskListSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		return criteria;
	}

	@Override
	protected void displayView() {
		constructUI();
		displayActiveTaskGroups();
		displayTaskStatistic();
	}

	private VerticalLayout createSearchPanel() {

		VerticalLayout basicSearchBody = new VerticalLayout();
		basicSearchBody.setSpacing(true);
		basicSearchBody.setMargin(new MarginInfo(true, false, true, false));
		basicSearchBody.addStyleName(UIConstants.BORDER_BOX_2);

		nameField = new TextField();

		nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
		UiUtils.addComponent(basicSearchBody, nameField,
				Alignment.MIDDLE_CENTER);

		HorizontalLayout control = new HorizontalLayout();
		control.setSpacing(true);
		control.setMargin(new MarginInfo(true, false, true, false));

		final Button searchBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH_LABEL));
		searchBtn.setIcon(MyCollabResource.newResource("icons/16/search.png"));
		searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		searchBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {

				TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
				searchCriteria.setProjectid(new NumberSearchField(
						CurrentProjectVariables.getProjectId()));
				searchCriteria.setTaskName(new StringSearchField(nameField
						.getValue().toString().trim()));
				TaskFilterParameter taskFilter = new TaskFilterParameter(
						searchCriteria, "Task Search");
				moveToTaskSearch(taskFilter);
			}
		});
		UiUtils.addComponent(control, searchBtn, Alignment.MIDDLE_CENTER);

		final Button advancedSearchBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
						searchCriteria.setProjectid(new NumberSearchField(
								CurrentProjectVariables.getProjectId()));
						searchCriteria.setTaskName(new StringSearchField(
								nameField.getValue().toString().trim()));
						TaskFilterParameter taskFilter = new TaskFilterParameter(
								searchCriteria, "Task Search");
						taskFilter.setAdvanceSearch(true);
						moveToTaskSearch(taskFilter);
					}
				});
		advancedSearchBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		UiUtils.addComponent(control, advancedSearchBtn,
				Alignment.MIDDLE_CENTER);
		UiUtils.addComponent(basicSearchBody, control, Alignment.MIDDLE_CENTER);

		return basicSearchBody;
	}

	void moveToTaskSearch(TaskFilterParameter taskFilter) {
		EventBus.getInstance()
				.fireEvent(new TaskEvent.Search(this, taskFilter));
	};

	private void displayTaskStatistic() {
		rightColumn.removeAllComponents();

		rightColumn.addComponent(createSearchPanel());
		UnresolvedTaskByAssigneeWidget unresolvedTaskByAssigneeWidget = new UnresolvedTaskByAssigneeWidget();
		rightColumn.addComponent(unresolvedTaskByAssigneeWidget);

		TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
		searchCriteria.setProjectid(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		searchCriteria.setStatuses(new SetSearchField<String>(SearchField.AND,
				new String[] { "Open" }));

		unresolvedTaskByAssigneeWidget.setSearchCriteria(searchCriteria);

		UnresolvedTaskByPriorityWidget unresolvedTaskByPriorityWidget = new UnresolvedTaskByPriorityWidget();
		rightColumn.addComponent(unresolvedTaskByPriorityWidget);
		unresolvedTaskByPriorityWidget.setSearchCriteria(searchCriteria);
	}

	private void displaySimpleView() {
		this.removeAllComponents();

		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(new MarginInfo(true, false, true, false));
		header.setStyleName("hdr-view");
		header.setSpacing(true);
		header.setWidth("100%");
		header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		final Image icon = new Image(null,
				MyCollabResource.newResource("icons/24/project/task.png"));
		header.addComponent(icon);

		UiUtils.addComponent(header, taskSelection, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(taskSelection, 1.0f);
		UiUtils.addComponent(header, viewButtons, Alignment.MIDDLE_RIGHT);

		this.addComponent(header);
		basicSearchView.setMargin(new MarginInfo(false, false, true, false));

		displayActiveTasksOnly();
		this.addComponent(basicSearchView.getWidget());

	}

	private void displayAdvancedView() {
		this.removeAllComponents();
		this.addComponent(header);
		this.addComponent(mainLayout);
		UiUtils.addComponent(this, header, Alignment.TOP_RIGHT);
		UiUtils.addComponent(header, viewButtons, Alignment.MIDDLE_RIGHT);
		this.addComponent(mainLayout);
	}

	private void displayGanttChartView() {
		this.removeAllComponents();
		VerticalLayout header = new VerticalLayout();
		header.setMargin(new MarginInfo(true, false, false, false));
		header.addComponent(viewButtons);
		header.setComponentAlignment(viewButtons, Alignment.MIDDLE_RIGHT);

		this.addComponent(header);
		ganttChart = new TaskGanttChart();
		this.addComponent(ganttChart);
		ganttChart.displayChart();
	}

	private void displayActiveTaskGroups() {
		final TaskListSearchCriteria criteria = this.createBaseSearchCriteria();
		criteria.setStatus(new StringSearchField("Open"));
		this.taskLists.setSearchCriteria(criteria);
	}

	private void displayInActiveTaskGroups() {
		final TaskListSearchCriteria criteria = this.createBaseSearchCriteria();
		criteria.setStatus(new StringSearchField("Closed"));
		this.taskLists.setSearchCriteria(criteria);
	}

	private void displayAllTaskGroups() {
		final TaskListSearchCriteria criteria = this.createBaseSearchCriteria();
		this.taskLists.setSearchCriteria(criteria);
	}

	@Override
	public void insertTaskList(final SimpleTaskList taskList) {
		this.taskLists.insetItemOnBottom(taskList);
	}

	private TaskSearchCriteria createTaskBaseSearchCriteria() {
		final TaskSearchCriteria criteria = new TaskSearchCriteria();
		criteria.setProjectid(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		return criteria;
	}

	private void displayActiveTasksOnly() {
		final TaskSearchCriteria criteria = this.createTaskBaseSearchCriteria();
		criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
				new String[] { "Open" }));
		this.doSearch(criteria);
	}

	private void displayPendingTasksOnly() {
		final TaskSearchCriteria criteria = this.createTaskBaseSearchCriteria();
		criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
				new String[] { "Pending" }));
		this.doSearch(criteria);
	}

	private void displayAllTasks() {
		final TaskSearchCriteria criteria = this.createTaskBaseSearchCriteria();
		this.doSearch(criteria);
	}

	private void displayInActiveTasks() {
		final TaskSearchCriteria criteria = this.createTaskBaseSearchCriteria();
		criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
				new String[] { "Closed" }));
		this.doSearch(criteria);
	}

	@Override
	public void enableActionControls(int numOfSelectedItem) {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public void disableActionControls() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public HasSearchHandlers<TaskListSearchCriteria> getSearchHandlers() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public HasSelectionOptionHandlers getOptionSelectionHandlers() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public HasMassItemActionHandlers getPopupActionHandlers() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public HasSelectableItemHandlers<SimpleTaskList> getSelectableItemHandlers() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public AbstractPagedBeanTable<TaskListSearchCriteria, SimpleTaskList> getPagedBeanTable() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}
}
