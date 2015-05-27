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
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
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
import org.tltv.gantt.client.shared.AbstractStep;
import org.tltv.gantt.client.shared.Step;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class GanttChartViewImpl extends AbstractPageView implements GanttChartView {
    private static final long serialVersionUID = 1L;

    private Gantt gantt;
    private LinkedHashMap<SimpleTask, Step> stepMap;
    private NativeSelect reso;
    private TaskHierarchyComp taskTable;

    private ProjectTaskListService taskListService;

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

        HorizontalLayout ganttLayout = constructGanttChart();

        MVerticalLayout wrapContent = new MVerticalLayout().withSpacing(false).withMargin(false)
                .with(createControls(), ganttLayout).expand(ganttLayout);
        wrapContent.addStyleName("gantt-view");
        this.with(header, wrapContent).expand(wrapContent);
    }

    private MHorizontalLayout constructGanttChart() {
        MHorizontalLayout mainLayout = new MHorizontalLayout().withSpacing(false).withWidth("100%");

        stepMap = new LinkedHashMap<>();

        taskTable = new TaskHierarchyComp();
        taskTable.setWidth("300px");

        gantt = new Gantt();
        gantt.setWidth(100, Unit.PERCENTAGE);
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
                updateTasksInfo(event.getStep(), event.getStartDate(), event.getEndDate());
            }
        });

        gantt.addResizeListener(new Gantt.ResizeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onGanttResize(ResizeEvent event) {
                updateTasksInfo(event.getStep(), event.getStartDate(), event.getEndDate());
            }
        });

        mainLayout.with(taskTable, gantt).expand(gantt);
        return mainLayout;
    }

    private void updateTasksInfo(AbstractStep step, long startDate, long endDate) {
//        SimpleTask task = stepMap.get(step);
//        GregorianCalendar calendar = new GregorianCalendar();
//        calendar.setTimeInMillis(startDate);
//        task.setStartdate(calendar.getTime());
//
//        calendar.setTimeInMillis(endDate);
//        task.setEnddate(calendar.getTime());
//
//        taskListService.updateWithSession(task, AppContext.getUsername());
//        taskTable.setCurrentDataList(stepMap.values());
    }

    public void displayGanttChart() {
        updateStepList();
    }

    @SuppressWarnings("unchecked")
    private void updateStepList() {
        TaskListSearchCriteria criteria = new TaskListSearchCriteria();
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        criteria.setStatus(new StringSearchField(OptionI18nEnum.StatusI18nEnum.Open.name()));
        List<SimpleTaskList> taskList = taskListService.findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        gantt.removeSteps();
        stepMap = new LinkedHashMap<>();

		/* Add steps */
        if (!taskList.isEmpty()) {
            for (SimpleTaskList task : taskList) {
//                Date startDate = task.getStartdate();
//                Date endDate = task.getEnddate();
//
//                if (endDate == null) {
//                    endDate = task.getDeadline();
//                }
//
//                if (startDate == null) {
//                    if (endDate == null) {
//                        startDate = DateTimeUtils.getCurrentDateWithoutMS();
//                        endDate = DateTimeUtils.subtractOrAddDayDuration(startDate, 1);
//                    } else {
//                        endDate = DateTimeUtils.trimHMSOfDate(endDate);
//                        startDate = DateTimeUtils.subtractOrAddDayDuration(endDate, -1);
//                    }
//                } else {
//                    startDate = DateTimeUtils.trimHMSOfDate(startDate);
//                    if (endDate == null) {
//                        endDate = DateTimeUtils.subtractOrAddDayDuration(startDate, 1);
//                    } else {
//                        endDate = DateTimeUtils.trimHMSOfDate(endDate);
//                        endDate = DateTimeUtils.subtractOrAddDayDuration(endDate, 1);
//                    }
//                }
//
//                if (endDate.before(gantt.getStartDate()) || startDate.after(gantt.getEndDate())) {
//                    continue;
//                }

                Step step = generateStepOfTaskList(task);
                if (step != null) {
                    gantt.addStep(step);
                    taskTable.addTaskList(task);
                }

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

        for (Step key : stepMap.values()) {
            gantt.addStep(key);
        }
    }

    private Step generateStepOfTaskList(SimpleTaskList taskList) {
        Date startDate = taskList.getStartDate();
        Date endDate = taskList.getEndDate();
        if (endDate.before(gantt.getStartDate()) || startDate.after(gantt.getEndDate())) {
            return null;
        } else {
            Step step = new Step();
            step.setCaption(tooltipGenerateForTaskList(taskList));
            step.setCaptionMode(Step.CaptionMode.HTML);
            step.setStartDate(startDate);
            step.setEndDate(endDate);
            return step;
        }
    }

    private String tooltipGenerateForTaskList(SimpleTaskList task) {
        return "";
    }

    private String tooltipGenerateForTask(SimpleTask task) {
        return ProjectLinkBuilder.generateProjectItemLinkWithTooltip(task.getProjectShortname(),
                task.getProjectid(), task.getTaskname(), ProjectTypeConstants.TASK, task.getId() + "", task.getTaskkey() + "");
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

        reso = new NativeSelect("Resolution");
        reso.setNullSelectionAllowed(false);
        reso.addItem(org.tltv.gantt.client.shared.Resolution.Hour);
        reso.addItem(org.tltv.gantt.client.shared.Resolution.Day);
        reso.addItem(org.tltv.gantt.client.shared.Resolution.Week);
        reso.setValue(gantt.getResolution());
        reso.setImmediate(true);
        reso.addValueChangeListener(resolutionValueChangeListener);

        controls.with(start, end, reso);
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
        reso.removeValueChangeListener(resolutionValueChangeListener);
        try {
            reso.setValue(resolution);
        } finally {
            reso.addValueChangeListener(resolutionValueChangeListener);
        }
    }
}
