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

import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.shared.ui.MarginInfo;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class TaskSearchViewImpl extends AbstractPageView implements TaskSearchView {
    private static final long serialVersionUID = 1L;

    private TaskSearchPanel taskSearchPanel;
    private TaskTableDisplay tableItem;

    public void setSearchInputValue(String value) {
        taskSearchPanel.setTextField(value);
    }

    public TaskSearchViewImpl() {
        this.withMargin(new MarginInfo(false, true, true, true));

        MVerticalLayout taskListLayout = new MVerticalLayout().withSpacing(false);
        with(taskListLayout);

        this.taskSearchPanel = new TaskSearchPanel();
        this.generateDisplayTable();

        taskListLayout.with(taskSearchPanel, tableItem);
    }

    private void generateDisplayTable() {
        this.tableItem = new TaskTableDisplay(TaskTableFieldDef.id,
                Arrays.asList(TaskTableFieldDef.taskname,
                        TaskTableFieldDef.startdate, TaskTableFieldDef.duedate,
                        TaskTableFieldDef.assignee,
                        TaskTableFieldDef.percentagecomplete), SearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS);

        this.tableItem.addTableListener(new TableClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(final TableClickEvent event) {
                final SimpleTask task = (SimpleTask) event.getData();
                if ("taskname".equals(event.getFieldName())) {
                    EventBusFactory.getInstance().post(
                            new TaskEvent.GotoRead(TaskSearchViewImpl.this,
                                    task.getId()));
                }
            }
        });
    }

    @Override
    public HasSearchHandlers<TaskSearchCriteria> getSearchHandlers() {
        return this.taskSearchPanel;
    }

    @Override
    public IPagedBeanTable<TaskSearchCriteria, SimpleTask> getPagedBeanTable() {
        return this.tableItem;
    }


    @Override
    public void moveToAdvanceSearch() {
        taskSearchPanel.getAdvanceSearch();
    }

    @Override
    public void moveToBasicSearch() {
        taskSearchPanel.getBasicSearch();
    }

}
