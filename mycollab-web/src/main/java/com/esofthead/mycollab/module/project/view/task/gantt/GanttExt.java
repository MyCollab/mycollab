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
package com.esofthead.mycollab.module.project.view.task.gantt;

import com.vaadin.server.Page;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.StepComponent;
import org.tltv.gantt.SubStepComponent;
import org.tltv.gantt.client.shared.AbstractStep;
import org.tltv.gantt.client.shared.Step;
import org.tltv.gantt.client.shared.SubStep;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
public class GanttExt extends Gantt {
    private GregorianCalendar minDate, maxDate;

    public GanttExt() {
        minDate = new GregorianCalendar();
        maxDate = new GregorianCalendar();
        this.setResizableSteps(true);
        this.setMovableSteps(true);
        this.setHeight((Page.getCurrent().getBrowserWindowHeight() - 200) + "px");
        updateGanttMinDate();
        updateGanttMaxDate();
    }

    public int getStepIndex(Step step) {
        StepComponent sc = this.stepComponents.get(step);
        return this.getState().steps.indexOf(sc);
    }

    public void addTask(GanttItemWrapper task) {
        Step step = task.getStep();
        super.addStep(step);
        calculateMaxMinDates(task);
    }

    public void addTask(int index, GanttItemWrapper task) {
        Step step = task.getStep();
        System.out.println("Addf step: " + index);
        super.addStep(index, step);
        calculateMaxMinDates(task);
    }

    private void updateGanttMinDate() {
        Calendar cloneVal = new GregorianCalendar();
        cloneVal.setTimeInMillis(minDate.getTimeInMillis());
        cloneVal.add(Calendar.DATE, -14);
        this.setStartDate(cloneVal.getTime());
    }

    private void updateGanttMaxDate() {
        Calendar cloneVal = new GregorianCalendar();
        cloneVal.setTimeInMillis(maxDate.getTimeInMillis());
        cloneVal.add(Calendar.DATE, 14);
        this.setEndDate(cloneVal.getTime());
    }

    public void calculateMaxMinDates(GanttItemWrapper task) {
        if (minDate.getTimeInMillis() > task.getStartDate().getTime()) {
            minDate.setTimeInMillis(task.getStartDate().getTime());
            updateGanttMinDate();
        }

        if (maxDate.getTimeInMillis() < task.getEndDate().getTime()) {
            maxDate.setTimeInMillis(task.getEndDate().getTime());
            updateGanttMaxDate();
        }
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
}