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

import com.esofthead.mycollab.common.TooltipBuilder;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.BusinessDayTimeUtils;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.events.GanttEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;
import org.tltv.gantt.client.shared.Step;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.esofthead.mycollab.common.TooltipBuilder.TdUtil.buildCellName;
import static com.esofthead.mycollab.common.TooltipBuilder.TdUtil.buildCellValue;

/**
 * @author MyCollab Ltd.
 * @since 5.0.8
 */
public class GanttItemWrapper {
    private AssignWithPredecessors task;
    private LocalDate startDate, endDate;
    private LocalDate fixedStartDateByChilds = new LocalDate(1970, 1, 1), fixedEndDatebyChilds = new LocalDate(2100, 1, 1);

    private GanttExt gantt;
    private GanttItemWrapper parent;
    private StepExt ownStep;
    private List<GanttItemWrapper> subItems;

    public GanttItemWrapper(GanttExt gantt, AssignWithPredecessors task) {
        this.gantt = gantt;
        setTask(task);

        if (task instanceof MilestoneGanttItem) {
            subItems = buildSubTasks(gantt, this, (MilestoneGanttItem) task);
        } else if (task instanceof TaskGanttItem) {
            subItems = buildSubTasks(gantt, this, (TaskGanttItem) task);
        }
    }

    public void removeSubTask(GanttItemWrapper subTask) {
        if (CollectionUtils.isNotEmpty(subItems)) {
            subItems.remove(subTask);
            if (task instanceof MilestoneGanttItem) {
                ((MilestoneGanttItem) task).removeSubTask((TaskGanttItem) subTask.task);
            } else if (task instanceof TaskGanttItem) {
                ((TaskGanttItem) task).removeSubTask((TaskGanttItem) subTask.task);
            }
        }
    }

    public void addSubTask(GanttItemWrapper subTask) {
        if (subItems == null) {
            subItems = new ArrayList<>();
        }
        subItems.add(subTask);
    }

    public AssignWithPredecessors getTask() {
        return task;
    }

    public void setTask(AssignWithPredecessors task) {
        this.task = task;
        if (task.getStartDate() == null) {
            startDate = new LocalDate();
            task.setStartDate(startDate.toDate());
        } else {
            startDate = new LocalDate(task.getStartDate());
        }

        if (task.getEndDate() == null) {
            endDate = new LocalDate();
            task.setEndDate(task.getEndDate());
        } else {
            endDate = new LocalDate(task.getEndDate());
        }

        buildAssociateStepIfNotExisted();
    }

    private void buildAssociateStepIfNotExisted() {
        if (ownStep == null) {
            ownStep = new StepExt();
            ownStep.setCaptionMode(Step.CaptionMode.HTML);
            ownStep.setShowProgress(false);
            ownStep.setGanttItemWrapper(this);
        }

        ownStep.setCaption(task.getName());
        ownStep.setDescription(buildTooltip());
        ownStep.setStartDate(startDate.toDate());
        ownStep.setEndDate(endDate.plusDays(1).toDate());
        if (task.getProgress() == null) {
            ownStep.setProgress(0);
        } else {
            ownStep.setProgress(task.getProgress());
        }

        if (isMilestone()) {
            ownStep.setBackgroundColor("C2DFFF");
        } else if (isTask() && task.hasSubAssignments()) {
            ownStep.setBackgroundColor("E4F1FF");
        } else {
            ownStep.setBackgroundColor("E4F1FF");
        }
    }

    public boolean isMilestone() {
        return task instanceof MilestoneGanttItem;
    }

    public boolean isTask() {
        return task instanceof TaskGanttItem;
    }

    public boolean hasSubTasks() {
        return subItems.size() > 0;
    }

    public String getName() {
        return task.getName();
    }

    public void setName(String name) {
        task.setName(name);
        ownStep.setCaption(name);
        gantt.markStepDirty(ownStep);
    }

    public List<GanttItemWrapper> subTasks() {
        return subItems;
    }

    private static List<GanttItemWrapper> buildSubTasks(GanttExt gantt, GanttItemWrapper parent, MilestoneGanttItem ganttItem) {
        List<TaskGanttItem> items = ganttItem.getSubTasks();
        return buildSubTasks(gantt, parent, items);
    }

    private static List<GanttItemWrapper> buildSubTasks(GanttExt gantt, GanttItemWrapper parent, TaskGanttItem ganttItem) {
        List<TaskGanttItem> items = ganttItem.getSubTasks();
        return buildSubTasks(gantt, parent, items);
    }

    private static List<GanttItemWrapper> buildSubTasks(GanttExt gantt, GanttItemWrapper parent, List<TaskGanttItem> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            List<GanttItemWrapper> tmpList = new ArrayList<>(items.size());
            for (TaskGanttItem item : items) {
                GanttItemWrapper ganttItemWrapper = new GanttItemWrapper(gantt, item);
                tmpList.add(ganttItemWrapper);
                ganttItemWrapper.setParent(parent);
            }
            return tmpList;
        } else {
            return new ArrayList<>();
        }
    }

    void calculateDatesByChildTasks() {
        if (CollectionUtils.isNotEmpty(subItems)) {
            LocalDate calStartDate = new LocalDate(2100, 1, 1);
            LocalDate calEndDate = new LocalDate(1970, 1, 1);
            for (GanttItemWrapper item : subItems) {
                calStartDate = DateTimeUtils.min(calStartDate, item.getStartDate());
                calEndDate = DateTimeUtils.max(calEndDate, item.getEndDate());
            }
            fixedStartDateByChilds = calStartDate;
            fixedEndDatebyChilds = calEndDate;
            setStartAndEndDate(calStartDate, calEndDate, true, true);
        }
    }

    public Integer getId() {
        return task.getId();
    }

    public void setId(Integer id) {
        task.setId(id);
    }

    public String getType() {
        if (task instanceof TaskGanttItem) {
            return ProjectTypeConstants.TASK;
        } else if (task instanceof MilestoneGanttItem) {
            return ProjectTypeConstants.MILESTONE;
        } else {
            throw new MyCollabException("Do not support assignment type " + this);
        }
    }

    public Long getDuration() {
        if (task.getDuration() != null) {
            return task.getDuration();
        } else {
            return BusinessDayTimeUtils.duration(startDate, endDate) * DateTimeUtils.MILISECONDS_IN_A_DAY;
        }
    }

    public void setDuration(Long duration) {
        task.setDuration(duration);
        if (startDate != null) {
            LocalDate expectedEndDate = BusinessDayTimeUtils.plusDays(startDate, (int) (duration.longValue() / DateTimeUtils.MILISECONDS_IN_A_DAY));
            setStartAndEndDate(startDate, expectedEndDate, true, true);
        }
    }

    public List<TaskPredecessor> getPredecessors() {
        return task.getPredecessors();
    }

    public void setPredecessors(List<TaskPredecessor> predecessors) {
        task.setPredecessors(predecessors);
    }

    public List<TaskPredecessor> getDependents() {
        return task.getDependents();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate date) {
        long duration = getDuration();
        LocalDate expectedEndDate = BusinessDayTimeUtils.plusDays(date, (int) (duration / DateTimeUtils.MILISECONDS_IN_A_DAY));
        setStartAndEndDate(date, expectedEndDate, true, true);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate date) {
        setStartAndEndDate(startDate, date, true, true);
    }


    public LocalDate getActualStartDate() {
        return new LocalDate(task.getActualStartDate());
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        if (actualStartDate == null) {
            task.setActualStartDate(null);
        } else {
            task.setActualStartDate(actualStartDate.toDate());
        }
    }

    public LocalDate getActualEndDate() {
        return new LocalDate(task.getActualEndDate());
    }

    public void setActualEndDate(LocalDate actualEndDate) {
        if (actualEndDate == null) {
            task.setActualEndDate(null);
        } else {
            task.setActualEndDate(actualEndDate.toDate());
        }
    }

    public Date getDueDate() {
        return task.getDeadline();
    }

    public Double getPercentageComplete() {
        return task.getProgress();
    }

    public void setPercentageComplete(Double percentageComplete) {
        task.setProgress(percentageComplete);
        while (parent != null) {
            parent.setPercentageComplete(parent.getPercentageComplete());
            parent = parent.getParent();
        }
    }

    public boolean setStartAndEndDate(LocalDate newStartDate, LocalDate newEndDate, boolean askToCheckPredecessors,
                                      boolean requestToCheckDependents) {
        if (newStartDate.isBefore(fixedStartDateByChilds) || newEndDate.isAfter(fixedEndDatebyChilds)) {
            throw new UserInvalidInputException("Invalid constraints");
        }
        boolean hasChange = false;
        if (!this.startDate.isEqual(newStartDate)) {
            hasChange = true;
            this.startDate = newStartDate;
            task.setStartDate(startDate.toDate());
            ownStep.setStartDate(startDate.toDate());
        }

        if (!this.endDate.isEqual(newEndDate)) {
            hasChange = true;
            this.endDate = newEndDate;
            task.setEndDate(endDate.toDate());
            ownStep.setEndDate(endDate.plusDays(1).toDate());
        }

        if (hasChange) {
            int duration = BusinessDayTimeUtils.duration(newStartDate, newEndDate);
            setDuration(duration * DateTimeUtils.MILISECONDS_IN_A_DAY);
            onDateChanges(askToCheckPredecessors, requestToCheckDependents);
        }

        return hasChange;
    }

    public Integer getGanttIndex() {
        return task.getGanttIndex();
    }

    public void setGanttIndex(Integer ganttIndex) {
        task.setGanttIndex(ganttIndex);
    }

    public String getAssignUser() {
        return task.getAssignUser();
    }

    public void setAssignUser(String assignUser) {
        task.setAssignUser(assignUser);
    }

    String buildTooltip() {
        TooltipBuilder tooltipBuilder = new TooltipBuilder();
        tooltipBuilder.setTitle(task.getName());
        Tr trRow2 = new Tr();
        Td cell21 = buildCellName(AppContext.getMessage(TaskI18nEnum.FORM_START_DATE));
        String startDate = AppContext.formatDate(task.getStartDate());
        Td cell22 = buildCellValue(startDate);
        Td cell23 = buildCellName(AppContext.getMessage(TaskI18nEnum.FORM_ACTUAL_START_DATE));
        String actualStartDate = AppContext.formatDate(task.getActualStartDate());
        Td cell24 = buildCellValue(actualStartDate);
        trRow2.appendChild(cell21, cell22, cell23, cell24);
        tooltipBuilder.appendRow(trRow2);

        Tr trRow3 = new Tr();
        Td cell31 = buildCellName(AppContext.getMessage(TaskI18nEnum.FORM_END_DATE));
        String endDate = AppContext.formatDate(task.getEndDate());
        Td cell32 = buildCellValue(endDate);
        Td cell33 = buildCellName(AppContext.getMessage(TaskI18nEnum.FORM_ACTUAL_END_DATE));
        String actualEndDate = AppContext.formatDate(task.getActualEndDate());
        Td cell34 = buildCellValue(actualEndDate);
        trRow3.appendChild(cell31, cell32, cell33, cell34);
        tooltipBuilder.appendRow(trRow3);

        return tooltipBuilder.create().write();
    }

    public GanttItemWrapper getParent() {
        return parent;
    }

    public void setParent(GanttItemWrapper parent) {
        this.parent = parent;
    }

    public void updateParentRelationship(GanttItemWrapper newParent) {
        GanttItemWrapper oldParent = this.parent;
        if (oldParent != null) {
            oldParent.removeSubTask(this);
        }

        this.parent = newParent;
        if (parent != null) {
            this.parent.addSubTask(this);
        }

        if (parent != null && parent.getTask() instanceof MilestoneGanttItem) {
            ((TaskGanttItem) getTask()).setMilestoneId(parent.getId());
        } else if (parent != null && parent.getTask() instanceof TaskGanttItem) {
            ((TaskGanttItem) getTask()).setParentTaskId(parent.getId());
        } else if (parent == null) {
            ((TaskGanttItem) getTask()).setParentTaskId(null);
            ((TaskGanttItem) getTask()).setMilestoneId(null);
        } else {
            throw new MyCollabException("Not support parent type " + getTask());
        }
    }

    public Step getStep() {
        return ownStep;
    }

    public void adjustTaskDatesByPredecessors(List<TaskPredecessor> predecessors) {
        if (CollectionUtils.isNotEmpty(predecessors)) {
            LocalDate currentStartDate = new LocalDate(getStartDate());
            LocalDate currentEndDate = new LocalDate(getEndDate());
            LocalDate boundStartDate = new LocalDate(1970, 1, 1);
            LocalDate boundEndDate = new LocalDate(2100, 1, 1);

            for (TaskPredecessor predecessor : predecessors) {
                int ganttIndex = predecessor.getGanttIndex();

                GanttItemWrapper ganttPredecessor = gantt.getBeanContainer().getItemByGanttIndex(ganttIndex);
                int dur = getDuration().intValue() - 1;
                if (ganttPredecessor != null) {
                    Integer lagDay = predecessor.getLagday() + 1;

                    if (TaskPredecessor.FS.equals(predecessor.getPredestype())) {
                        LocalDate endDate = new LocalDate(ganttPredecessor.getEndDate());
                        endDate = endDate.plusDays(1);
                        LocalDate expectedStartDate = BusinessDayTimeUtils.plusDays(endDate, lagDay);
                        if (boundStartDate.isBefore(expectedStartDate)) {
                            boundStartDate = expectedStartDate;
                        }
                        if (currentStartDate.isBefore(expectedStartDate)) {
                            currentStartDate = expectedStartDate;
                            LocalDate expectedEndDate = currentStartDate.plusDays(dur);
                            currentEndDate = DateTimeUtils.min(boundEndDate, expectedEndDate);
                        }
                    } else if (TaskPredecessor.FF.equals(predecessor.getPredestype())) {
                        LocalDate endDate = new LocalDate(ganttPredecessor.getEndDate());
                        LocalDate expectedEndDate = BusinessDayTimeUtils.plusDays(endDate, lagDay);
                        if (boundEndDate.isAfter(expectedEndDate)) {
                            boundEndDate = expectedEndDate;
                        }
                        if (currentEndDate.isAfter(expectedEndDate)) {
                            currentEndDate = expectedEndDate;
                            LocalDate expectedStartDate = currentEndDate.minusDays(dur);
                            currentStartDate = DateTimeUtils.max(boundStartDate, expectedStartDate);
                        }
                    } else if (TaskPredecessor.SF.equals(predecessor.getPredestype())) {
                        LocalDate startDate = new LocalDate(ganttPredecessor.getStartDate());
                        LocalDate expectedEndDate = BusinessDayTimeUtils.plusDays(startDate, lagDay);
                        if (boundEndDate.isAfter(expectedEndDate)) {
                            boundEndDate = expectedEndDate;
                        }
                        if (currentEndDate.isAfter(expectedEndDate)) {
                            currentEndDate = expectedEndDate;
                            LocalDate expectedStartDate = currentEndDate.minusDays(dur);
                            currentStartDate = DateTimeUtils.max(boundStartDate, expectedStartDate);
                        }
                    } else if (TaskPredecessor.SS.equals(predecessor.getPredestype())) {
                        LocalDate startDate = new LocalDate(ganttPredecessor.getStartDate());
                        LocalDate expectedStartDate = BusinessDayTimeUtils.plusDays(startDate, lagDay);

                        if (boundStartDate.isBefore(expectedStartDate)) {
                            boundStartDate = expectedStartDate;
                        }

                        if (currentStartDate.isBefore(expectedStartDate)) {
                            currentStartDate = expectedStartDate;
                            LocalDate expectedEndDate = BusinessDayTimeUtils.plusDays(startDate, dur);
                            currentEndDate = DateTimeUtils.min(boundEndDate, expectedEndDate);
                        }
                    } else {
                        throw new MyCollabException("Do not support predecessor type " + predecessor.getPredestype());
                    }

                    if (currentEndDate.isBefore(currentStartDate)) {
                        throw new UserInvalidInputException("Invalid constraint");
                    }
                }
            }

            setStartAndEndDate(currentStartDate, currentEndDate, false, true);
        }
    }

    private void adjustDependentTasksDates() {
        List<TaskPredecessor> dependents = task.getDependents();
        if (CollectionUtils.isNotEmpty(dependents)) {
            for (TaskPredecessor dependent : dependents) {
                GanttItemWrapper dependentGanttItem = gantt.getBeanContainer().getItemByGanttIndex(dependent.getGanttIndex());
                if (dependentGanttItem != null) {
                    dependentGanttItem.adjustTaskDatesByPredecessors(dependentGanttItem.getPredecessors());
                }
            }
        }
    }

    private void onDateChanges(boolean askToCheckPredecessors, boolean requestToCheckDependents) {
        ownStep.setDescription(buildTooltip());
        EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue(GanttItemWrapper.this, this));

        if (askToCheckPredecessors) {
            adjustTaskDatesByPredecessors(getPredecessors());
        }

        if (requestToCheckDependents) {
            adjustDependentTasksDates();
        }
        updateParentDates();
        gantt.markStepDirty(ownStep);
    }

    void updateParentDates() {
        GanttItemWrapper parentTask = this.getParent();
        if (parentTask != null) {
            parentTask.calculateDatesByChildTasks();
        }
    }

    boolean isIndentable() {
        GanttItemContainer beanContainer = gantt.getBeanContainer();
        GanttItemWrapper prevItemId = beanContainer.prevItemId(this);
        if (prevItemId != null && this.getParent() != prevItemId) {
            return true;
        }
        return false;
    }

    boolean isOutdentable() {
        return (this.getParent() != null);
    }

    public Task buildNewTask() {
        if (task instanceof TaskGanttItem) {
            return ((TaskGanttItem) task).buildNewTask();
        } else {
            throw new MyCollabException("Invalid method call");
        }
    }

    public boolean isAncestor(GanttItemWrapper item) {
        if (item != null) {
            if (this == item) {
                return true;
            }
            return isAncestor(item.getParent());
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GanttItemWrapper)) return false;

        GanttItemWrapper that = (GanttItemWrapper) o;

        return task.equals(that.task);

    }

    @Override
    public int hashCode() {
        return task.hashCode();
    }
}
