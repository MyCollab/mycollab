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
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.ui.chart.Key;
import com.mycollab.ui.chart.PieChartWrapper;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class TaskAssigneeChartWidget extends PieChartWrapper<TaskSearchCriteria> implements ITaskAssigneeChartWidget {
    public TaskAssigneeChartWidget() {
        super(350, 280);
    }

    @Override
    protected DefaultPieDataset createDataset() {
        // create the dataset...
        final DefaultPieDataset dataset = new DefaultPieDataset();
        if (!groupItems.isEmpty()) {
            for (GroupItem item : groupItems) {
                String assignUser = (item.getGroupid() != null) ? item.getGroupid() : "";
                String assignUserFullName = item.getGroupid() == null ? UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED) :
                        item.getGroupname();
                if (assignUserFullName == null || "".equals(assignUserFullName.trim())) {
                    assignUserFullName = StringUtils.extractNameFromEmail(assignUser);
                }
                dataset.setValue(new Key(assignUser, assignUserFullName), item.getValue());
            }
        }
        return dataset;
    }

    @Override
    protected List<GroupItem> loadGroupItems() {
        ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
        return taskService.getAssignedTasksSummary(searchCriteria);
    }

    @Override
    public void clickLegendItem(String key) {
        TaskSearchCriteria cloneSearchCriteria = BeanUtility.deepClone(searchCriteria);
        cloneSearchCriteria.setAssignUser(StringSearchField.and(key));
        EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(this, cloneSearchCriteria));
    }
}
