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

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.AssignWithPredecessors;
import com.esofthead.mycollab.module.project.domain.MilestoneGanttItem;
import com.esofthead.mycollab.module.project.domain.ProjectGanttItem;
import com.esofthead.mycollab.module.project.domain.TaskGanttItem;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.task.gantt.GanttExt;
import com.esofthead.mycollab.module.project.view.task.gantt.GanttItemWrapper;
import com.esofthead.mycollab.module.project.view.task.gantt.GanttTreeTable;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tltv.gantt.client.shared.Resolution;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class GanttChartViewImpl extends AbstractPageView implements GanttChartView {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(GanttChartViewImpl.class);

    private boolean projectNavigatorVisibility = false;

    private MHorizontalLayout mainLayout;
    private GanttExt gantt;
    private GanttTreeTable taskTable;
    private GanttAssignmentService ganttAssignmentService;

    public GanttChartViewImpl() {
        this.setSizeFull();
        this.withMargin(true);

        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, false, true, false))
                .withStyleName("hdr-view").withWidth("100%");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label headerText = new Label("Gantt chart", ContentMode.HTML);
        headerText.setStyleName(UIConstants.HEADER_TEXT);
        CssLayout headerWrapper = new CssLayout();
        headerWrapper.addComponent(headerText);

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

        header.with(headerWrapper, resWrapper).withAlign(headerWrapper, Alignment.MIDDLE_LEFT).expand(headerWrapper);

        ganttAssignmentService = ApplicationContextUtil.getSpringBean(GanttAssignmentService.class);

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

    public void displayGanttChart() {
        setProjectNavigatorVisibility(false);
        mainLayout.removeAllComponents();

        gantt = new GanttExt();
        taskTable = new GanttTreeTable(gantt);

        mainLayout.with(taskTable, gantt).expand(gantt);

        showSteps();
    }

    @Override
    public GanttExt getGantt() {
        return gantt;
    }

    @Override
    public GanttTreeTable getTaskTable() {
        return taskTable;
    }

    @SuppressWarnings("unchecked")
    private void showSteps() {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                List<AssignWithPredecessors> assignments = ganttAssignmentService.getTaskWithPredecessors(Arrays.asList
                        (CurrentProjectVariables.getProjectId()), AppContext.getAccountId());
                if (assignments.size() == 1) {
                    ProjectGanttItem projectGanttItem = (ProjectGanttItem) assignments.get(0);
                    List<MilestoneGanttItem> milestoneGanttItems = projectGanttItem.getMilestones();
                    for (MilestoneGanttItem milestoneGanttItem : milestoneGanttItems) {
                        GanttItemWrapper itemWrapper = new GanttItemWrapper(gantt, milestoneGanttItem);
                        taskTable.addTask(itemWrapper);
                    }

                    List<TaskGanttItem> taskGanttItems = projectGanttItem.getTasksWithNoMilestones();
                    for (TaskGanttItem taskGanttItem : taskGanttItems) {
                        GanttItemWrapper itemWrapper = new GanttItemWrapper(gantt, taskGanttItem);
                        taskTable.addTask(itemWrapper);
                    }
                    taskTable.updateWholeGanttIndexes();
                    UI.getCurrent().push();
                } else {
                    LOG.error("Error to query multiple value " + CurrentProjectVariables.getProjectId());
                }
            }
        });
    }
}
