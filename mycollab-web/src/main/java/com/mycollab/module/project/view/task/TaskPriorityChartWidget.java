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
package com.mycollab.module.project.view.task;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.mycollab.ui.chart.PieChartWrapper;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.events.TaskEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.mvp.ViewComponent;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class TaskPriorityChartWidget extends PieChartWrapper<TaskSearchCriteria> implements ITaskPriorityChartWidget {
    public TaskPriorityChartWidget() {
        super(TaskPriority.class, 350, 280);
    }

    @Override
    protected DefaultPieDataset createDataset() {
        // create the dataset...
        final DefaultPieDataset dataset = new DefaultPieDataset();

        TaskPriority[] priorities = OptionI18nEnum.task_priorities;
        for (TaskPriority priority : priorities) {
            boolean isFound = false;
            for (GroupItem item : groupItems) {
                if (priority.name().equals(item.getGroupid())) {
                    dataset.setValue(priority.name(), item.getValue());
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                dataset.setValue(priority.name(), 0);
            }
        }

        return dataset;
    }

    @Override
    protected List<GroupItem> loadGroupItems() {
        ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
        return taskService.getPrioritySummary(searchCriteria);
    }

    @Override
    public void clickLegendItem(String key) {
        TaskSearchCriteria cloneSearchCriteria = BeanUtility.deepClone(searchCriteria);
        cloneSearchCriteria.setPriorities(new SetSearchField<>(key));
        EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(this, cloneSearchCriteria));
    }
}
