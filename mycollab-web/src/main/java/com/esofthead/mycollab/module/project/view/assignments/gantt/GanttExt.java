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
package com.esofthead.mycollab.module.project.view.assignments.gantt;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.GanttEvent;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.vaadin.server.Page;
import org.joda.time.LocalDate;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.StepComponent;
import org.tltv.gantt.SubStepComponent;
import org.tltv.gantt.client.shared.AbstractStep;
import org.tltv.gantt.client.shared.Step;
import org.tltv.gantt.client.shared.SubStep;

import java.util.TimeZone;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
public class GanttExt extends Gantt {
    private LocalDate minDate, maxDate;
    private GanttItemContainer beanContainer;

    public GanttExt() {
        this.setTimeZone(TimeZone.getTimeZone("Atlantic/Reykjavik"));
        this.setImmediate(true);
        minDate = new LocalDate(2100, 1, 1);
        maxDate = new LocalDate(1970, 1, 1);
        this.setResizableSteps(true);
        this.setMovableSteps(true);
        this.setHeight((Page.getCurrent().getBrowserWindowHeight() - 270) + "px");
        beanContainer = new GanttItemContainer();

        this.addClickListener(new Gantt.ClickListener() {
            @Override
            public void onGanttClick(Gantt.ClickEvent event) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    StepExt step = (StepExt) event.getStep();
                    getUI().addWindow(new QuickEditGanttItemWindow(GanttExt.this, step.getGanttItemWrapper()));
                }
            }
        });

        this.addMoveListener(new Gantt.MoveListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onGanttMove(MoveEvent event) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    updateTasksInfoByResizeOrMove((StepExt) event.getStep(), event.getStartDate(), event.getEndDate());
                }
            }
        });

        this.addResizeListener(new Gantt.ResizeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onGanttResize(ResizeEvent event) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    updateTasksInfoByResizeOrMove((StepExt) event.getStep(), event.getStartDate(), event.getEndDate());
                }
            }
        });
    }

    public GanttItemContainer getBeanContainer() {
        return beanContainer;
    }

    public int getStepIndex(Step step) {
        StepComponent sc = this.stepComponents.get(step);
        return this.getState().steps.indexOf(sc);
    }

    void addTask(GanttItemWrapper task) {
        Step step = task.getStep();
        super.addStep(step);
        calculateMaxMinDates(task);
    }

    public void addTask(int index, GanttItemWrapper task) {
        Step step = task.getStep();
        super.addStep(index, step);
        calculateMaxMinDates(task);
    }

    private void updateGanttDates() {
        if (minDate.isAfter(maxDate)) {
            minDate = new LocalDate();
            maxDate = new LocalDate();
        }
        this.setStartDate(minDate.minusDays(14).toDate());
        this.setEndDate(maxDate.plusDays(14).toDate());
    }

    public void calculateMaxMinDates(GanttItemWrapper task) {
        if (minDate.isAfter(task.getStartDate())) {
            minDate = task.getStartDate();
        }

        if (maxDate.isBefore(task.getEndDate())) {
            maxDate = task.getEndDate();
        }

        updateGanttDates();
    }

    @Override
    public AbstractStep getStep(String uid) {
        if (uid == null) {
            return null;
        } else {
            StepExt key = new StepExt();
            key.setUid(uid);
            StepComponent sc = this.stepComponents.get(key);
            if (sc != null) {
                return sc.getState().step;
            } else {
                SubStep key1 = new SubStep();
                key1.setUid(uid);
                SubStepComponent sub = this.subStepMap.get(key1);
                return sub != null ? sub.getState().step : null;
            }
        }
    }

    private void updateTasksInfoByResizeOrMove(StepExt step, long startDate, long endDate) {
        final GanttItemWrapper ganttItemWrapper = step.getGanttItemWrapper();
        if (ganttItemWrapper.hasSubTasks()) {
            step.setStartDate(ganttItemWrapper.getStartDate().toDate());
            step.setEndDate(ganttItemWrapper.getEndDate().plusDays(1).toDate());
            EventBusFactory.getInstance().post(new GanttEvent.UpdateGanttItem(GanttExt.this, ganttItemWrapper));
            NotificationUtil.showWarningNotification("Can not adjust dates of parent task");
        } else {
            LocalDate suggestedStartDate = new LocalDate(startDate);
            LocalDate suggestedEndDate = new LocalDate(endDate);
            ganttItemWrapper.setStartAndEndDate(suggestedStartDate, suggestedEndDate, true, true);
            EventBusFactory.getInstance().post(new GanttEvent.UpdateGanttItem(GanttExt.this, ganttItemWrapper));
            EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue(GanttExt.this, ganttItemWrapper));
            this.calculateMaxMinDates(ganttItemWrapper);
        }
    }
}