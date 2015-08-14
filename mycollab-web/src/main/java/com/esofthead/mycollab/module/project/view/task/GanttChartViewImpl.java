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
import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.task.gantt.*;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UIUtils;
import com.esofthead.mycollab.vaadin.ui.ValueComboBox;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.Gantt.MoveEvent;
import org.tltv.gantt.Gantt.ResizeEvent;
import org.tltv.gantt.client.shared.Resolution;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class GanttChartViewImpl extends AbstractPageView implements GanttChartView {
    private static final long serialVersionUID = 1L;

    private boolean projectNavigatorVisibility = false;

    private MHorizontalLayout mainLayout;
    private GanttExt gantt;
    private GanttTreeTable taskTable;
    private Button toogleMenuShowBtn;
    private ProjectTaskService taskService;

    public GanttChartViewImpl() {
        this.setSizeFull();
        this.withMargin(true);

        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withStyleName("hdr-view").withWidth("100%");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label headerText = new Label("Gantt chart", ContentMode.HTML);
        headerText.setStyleName(UIConstants.HEADER_TEXT);
        CssLayout headerWrapper = new CssLayout();
        headerWrapper.addComponent(headerText);

        toogleMenuShowBtn = new Button("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                projectNavigatorVisibility = !projectNavigatorVisibility;
                setProjectNavigatorVisibility(projectNavigatorVisibility);
                if (projectNavigatorVisibility) {
                    toogleMenuShowBtn.setCaption("Hide menu");
                } else {
                    toogleMenuShowBtn.setCaption("Show menu");
                }
            }
        });
        toogleMenuShowBtn.addStyleName(UIConstants.THEME_LINK);

        HorizontalLayout resWrapper = new HorizontalLayout();
        Label resLbl = new Label("Resolution: ");
        final ComboBox resValue = new ValueComboBox(false, "Day", "Week");
        resValue.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String val = (String) resValue.getValue();
                if ("Day".equals(val)) {
                    gantt.setResolution(Resolution.Day);
                } else if ("Week".equals(val)) {
                    gantt.setResolution(Resolution.Week);
                }
            }
        });
        resWrapper.setSpacing(true);
        resWrapper.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        resWrapper.addComponent(resLbl);
        resWrapper.addComponent(resValue);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.M_BUTTON_BACK), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{
                        "task", "dashboard", UrlEncodeDecoder.encode(CurrentProjectVariables.getProjectId())}));
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        header.with(headerWrapper, toogleMenuShowBtn, resWrapper, cancelBtn).withAlign(headerWrapper, Alignment.MIDDLE_LEFT)
                .withAlign(toogleMenuShowBtn, Alignment.MIDDLE_RIGHT).withAlign(cancelBtn, Alignment.MIDDLE_RIGHT).expand(headerWrapper);
        taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);

        mainLayout = new MHorizontalLayout().withSpacing(false);
        mainLayout.addStyleName("gantt_container");
        mainLayout.setSizeFull();
        this.with(header, mainLayout).expand(mainLayout);
    }

    @Override
    public void detach() {
        setProjectNavigatorVisibility(true);
        super.detach();
    }

    private void setProjectNavigatorVisibility(boolean visibility) {
        ProjectView view = UIUtils.getRoot(this, ProjectView.class);
        if (view != null) {
            view.setNavigatorVisibility(visibility);
        }
    }

    private void updateTasksInfo(StepExt step, long startDate, long endDate) {
        GanttItemWrapper ganttItemWrapper = step.getGanttItemWrapper();
        SimpleTask task = ganttItemWrapper.getTask();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(startDate);
        ganttItemWrapper.setStartDate(calendar.getTime());

        calendar.setTimeInMillis(endDate);
        ganttItemWrapper.setEndDate(calendar.getTime());
        taskService.updateSelectiveWithSession(task, AppContext.getUsername());
        EventBusFactory.getInstance().post(new TaskEvent.GanttTaskUpdate(GanttChartViewImpl.this, ganttItemWrapper));
        ganttItemWrapper.markAsDirty();
        gantt.calculateMaxMinDates(ganttItemWrapper);
    }

    public void displayGanttChart() {
        toogleMenuShowBtn.setCaption("Show menu");
        setProjectNavigatorVisibility(false);
        mainLayout.removeAllComponents();

        gantt = new GanttExt();
        taskTable = new GanttTreeTable(gantt);

        gantt.addMoveListener(new Gantt.MoveListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onGanttMove(MoveEvent event) {
                updateTasksInfo((StepExt) event.getStep(), event.getStartDate(), event.getEndDate());
            }
        });

        gantt.addResizeListener(new Gantt.ResizeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onGanttResize(ResizeEvent event) {
                updateTasksInfo((StepExt) event.getStep(), event.getStartDate(), event.getEndDate());
            }
        });

        mainLayout.with(taskTable, gantt).expand(gantt);

        updateStepList();
    }

    @SuppressWarnings("unchecked")
    private void updateStepList() {
        final TaskSearchCriteria criteria = new TaskSearchCriteria();
        criteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        criteria.setHasParentTask(new BooleanSearchField());
        criteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("createdTime", SearchCriteria.ASC)));
        int totalTasks = taskService.getTotalCount(criteria);
        final int pages = totalTasks / 20;
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                for (int page = 0; page < pages + 1; page++) {
                    List<SimpleTask> tasks = taskService.findPagableListByCriteria(new SearchRequest<>(criteria, page + 1, 20));

                    if (!tasks.isEmpty()) {
                        for (final SimpleTask task : tasks) {
                            final GanttItemWrapper itemWrapper = new GanttItemWrapper(task);
                            gantt.addTask(itemWrapper);
                            taskTable.addTask(itemWrapper);
                        }
                        UI.getCurrent().push();
                    }
                }
            }
        });

        gantt.addClickListener(new Gantt.ClickListener() {
            @Override
            public void onGanttClick(Gantt.ClickEvent clickEvent) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    StepExt step = (StepExt) clickEvent.getStep();
                    getUI().addWindow(new QuickEditTaskWindow(gantt, step.getGanttItemWrapper()));
                }
            }
        });
    }


}
