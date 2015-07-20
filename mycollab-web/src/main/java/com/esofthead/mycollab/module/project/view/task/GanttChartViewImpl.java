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

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.task.gantt.*;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.DateFieldExt;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.Gantt.MoveEvent;
import org.tltv.gantt.Gantt.ResizeEvent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.Calendar;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class GanttChartViewImpl extends AbstractPageView implements GanttChartView {
    private static final long serialVersionUID = 1L;

    private GanttExt gantt;
    private NativeSelect chartResolution;
    private TaskHierarchyComp taskTable;

    private ProjectTaskListService taskListService;
    private ProjectTaskService taskService;

    public GanttChartViewImpl() {
        this.setStyleName("gantt-view");
        this.setSizeFull();
        this.withMargin(new MarginInfo(false, true, true, true));

        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withStyleName("hdr-view").withWidth("100%");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label headerText = new Label(String.format("%s %s (Gantt chart)", FontAwesome.BAR_CHART_O.getHtml(),
                CurrentProjectVariables.getProject().getName()), ContentMode.HTML);
        headerText.setStyleName(UIConstants.HEADER_TEXT);
        CssLayout headerWrapper = new CssLayout();
        headerWrapper.addComponent(headerText);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{
                        "task", "dashboard", UrlEncodeDecoder.encode(CurrentProjectVariables.getProjectId())}));
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        header.with(headerWrapper, cancelBtn).withAlign(headerWrapper, Alignment.MIDDLE_LEFT).withAlign(cancelBtn,
                Alignment.MIDDLE_RIGHT).expand(headerWrapper);

        taskListService = ApplicationContextUtil.getSpringBean(ProjectTaskListService.class);
        taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);

        HorizontalLayout ganttLayout = constructGanttChart();

        MVerticalLayout wrapContent = new MVerticalLayout().withSpacing(false).withMargin(false)
                .with(createControls(), ganttLayout).expand(ganttLayout);
        wrapContent.addStyleName("gantt-view");
        this.with(header, wrapContent).expand(wrapContent);
    }

    private MHorizontalLayout constructGanttChart() {
        MHorizontalLayout mainLayout = new MHorizontalLayout().withSpacing(false).withWidth("100%");

        taskTable = new TaskHierarchyComp();
        taskTable.setWidth("300px");

        gantt = new GanttExt();
        gantt.setSizeFull();
        gantt.setResizableSteps(true);
        gantt.setMovableSteps(true);
        gantt.setVerticalScrollDelegateTarget(taskTable);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);
        cal.add(Calendar.DATE, -14);

        gantt.setStartDate(cal.getTime());
        cal.add(Calendar.DATE, 28);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        gantt.setEndDate(cal.getTime());

        gantt.addMoveListener(new Gantt.MoveListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onGanttMove(MoveEvent event) {
                updateTasksInfo((StepExt)event.getStep(), event.getStartDate(), event.getEndDate());
            }
        });

        gantt.addResizeListener(new Gantt.ResizeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onGanttResize(ResizeEvent event) {
                updateTasksInfo((StepExt)event.getStep(), event.getStartDate(), event.getEndDate());
            }
        });

        mainLayout.with(taskTable, gantt).expand(gantt);
        return mainLayout;
    }

    private void updateTasksInfo(StepExt step, long startDate, long endDate) {
        GanttItemWrapper ganttItemWrapper =  step.getGanttItemWrapper();
        if (ganttItemWrapper instanceof TaskGanttItemWrapper) {
            SimpleTask task = ((TaskGanttItemWrapper)ganttItemWrapper).getTask();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(startDate);
            task.setStartdate(calendar.getTime());

            calendar.setTimeInMillis(endDate);
            task.setEnddate(calendar.getTime());
            taskService.updateSelectiveWithSession(task, AppContext.getUsername());
        }
    }

    public void displayGanttChart() {
        updateStepList();
    }

    @SuppressWarnings("unchecked")
    private void updateStepList() {
        gantt.removeSteps();
        taskTable.removeAllItems();
        TaskListSearchCriteria criteria = new TaskListSearchCriteria();
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        criteria.setStatus(new StringSearchField(OptionI18nEnum.StatusI18nEnum.Open.name()));
        List<SimpleTaskList> taskList = taskListService.findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        if (!taskList.isEmpty()) {
            for (SimpleTaskList task : taskList) {
                GanttItemWrapper itemWrapper = new TaskListGanttItemWrapper(task, gantt.getStartDate(), gantt.getEndDate());
                taskTable.addTaskList(itemWrapper);
                gantt.addStep(itemWrapper.getStep());
					/* Add style for row block */
//                if (task.isCompleted()) {
//                    step.setBackgroundColor("53C540");
//                    step.setStyleName("completed");
//                } else if (task.isPending()) {
//                    step.setBackgroundColor("e2f852");
//                } else if (task.isOverdue()) {
//                    step.setBackgroundColor("FC4350");
//                }
//                stepMap.put(taskList, step);
            }
        }
    }

    private Panel createControls() {
        Panel panel = new Panel();
        panel.setWidth(100, Unit.PERCENTAGE);

        MHorizontalLayout controls = new MHorizontalLayout().withMargin(true);
        panel.setContent(controls);

        DateFieldExt start = new DateFieldExt(AppContext.getMessage(TaskI18nEnum.FORM_START_DATE));
        start.setValue(gantt.getStartDate());
        start.setResolution(Resolution.DAY);
        start.setImmediate(true);
        start.addValueChangeListener(startDateValueChangeListener);

        DateField end = new DateFieldExt(AppContext.getMessage(TaskI18nEnum.FORM_END_DATE));
        end.setValue(gantt.getEndDate());
        end.setResolution(Resolution.DAY);
        end.setImmediate(true);
        end.addValueChangeListener(endDateValueChangeListener);

        chartResolution = new NativeSelect("Resolution");
        chartResolution.setNullSelectionAllowed(false);
        chartResolution.addItem(org.tltv.gantt.client.shared.Resolution.Day);
        chartResolution.addItem(org.tltv.gantt.client.shared.Resolution.Week);
        chartResolution.setValue(gantt.getResolution());
        chartResolution.setImmediate(true);
        chartResolution.addValueChangeListener(resolutionValueChangeListener);

        controls.with(start, end, chartResolution);
        panel.setStyleName(UIConstants.THEME_NO_BORDER);

        return panel;
    }

    private Property.ValueChangeListener startDateValueChangeListener = new Property.ValueChangeListener() {
        private static final long serialVersionUID = 1L;

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            gantt.setStartDate((Date) event.getProperty().getValue());
            updateStepList();
        }
    };

    private Property.ValueChangeListener endDateValueChangeListener = new Property.ValueChangeListener() {
        private static final long serialVersionUID = 1L;

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            gantt.setEndDate((Date) event.getProperty().getValue());
            updateStepList();
        }
    };

    private Property.ValueChangeListener resolutionValueChangeListener = new Property.ValueChangeListener() {
        private static final long serialVersionUID = 1L;

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            org.tltv.gantt.client.shared.Resolution res = (org.tltv.gantt.client.shared.Resolution) event.getProperty().getValue();
            if (validateResolutionChange(res)) {
                gantt.setResolution(res);
            }
        }
    };

    private boolean validateResolutionChange(final org.tltv.gantt.client.shared.Resolution res) {
        long max = 4 * 7 * 24 * 60 * 60000L;
        if (res == org.tltv.gantt.client.shared.Resolution.Hour
                && (gantt.getEndDate().getTime() - gantt.getStartDate().getTime()) > max) {

            // revert to previous resolution
            setResolution(gantt.getResolution());

            // make user to confirm hour resolution, if the timeline range is
            // more than one week long.

            ConfirmDialogExt.show(UI.getCurrent(),
                    AppContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE),
                    "Timeline range is a quite long for hour resolution. Rendering may be slow. Continue anyway?",
                    AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                    AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                    new ConfirmDialog.Listener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClose(final ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                setResolution(res);
                                gantt.setResolution(res);
                            }
                        }
                    });
            return false;
        }
        return true;
    }

    private void setResolution(org.tltv.gantt.client.shared.Resolution resolution) {
        chartResolution.removeValueChangeListener(resolutionValueChangeListener);
        try {
            chartResolution.setValue(resolution);
        } finally {
            chartResolution.addValueChangeListener(resolutionValueChangeListener);
        }
    }

    void insertSteps(final GanttItemWrapper parent, final List<GanttItemWrapper> childs) {
        final int stepIndex = gantt.getStepIndex(parent.getStep());
        if (stepIndex != -1) {
            UI.getCurrent().setPollInterval(1000);
            for (GanttItemWrapper child : childs) {
                taskTable.addItem(child);
                taskTable.setParent(child, parent);
            }
            updateGanttChartBaseOnTreeTableContainer();
        }
    }

    void updateGanttChartBaseOnTreeTableContainer() {
        gantt.removeSteps();
        Collection<GanttItemWrapper> items = (Collection<GanttItemWrapper>) taskTable.getItemIds();
        for (GanttItemWrapper item: items) {
            gantt.addStep(item.getStep());
        }
    }

    class TaskHierarchyComp extends TreeTable {
        TaskHierarchyComp() {
            super();
            this.addContainerProperty("name", String.class, "");
            this.setColumnHeader("name", "Name");
            this.addGeneratedColumn("name", new ColumnGenerator() {
                @Override
                public Object generateCell(Table table, Object itemId, Object columnId) {
                    GanttItemWrapper item = (GanttItemWrapper) itemId;
                    return new Label(item.getName());
                }
            });

            this.addExpandListener(new Tree.ExpandListener() {
                @Override
                public void nodeExpand(Tree.ExpandEvent expandEvent) {
                    GanttItemWrapper item = (GanttItemWrapper) expandEvent.getItemId();
                    List<GanttItemWrapper> subTasks = item.subTasks();
                    GanttChartViewImpl.this.insertSteps(item, subTasks);
                }
            });

            this.addCollapseListener(new Tree.CollapseListener() {
                @Override
                public void nodeCollapse(Tree.CollapseEvent collapseEvent) {
                    updateGanttChartBaseOnTreeTableContainer();
                }
            });
        }

        void addTaskList(GanttItemWrapper itemWrapper) {
            this.addItem(itemWrapper);
        }
    }
}
