/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.task;

import com.mycollab.db.arguments.BooleanSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.ui.TimeLogComp;
import com.mycollab.mobile.module.project.ui.TimeLogEditView;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ItemTimeLogging;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TaskTimeLogComp extends TimeLogComp<SimpleTask> {
    private static final long serialVersionUID = 8006444639083945910L;

    @Override
    protected Double getTotalBillableHours(SimpleTask bean) {
        ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setType(StringSearchField.and(ProjectTypeConstants.TASK));
        criteria.setTypeId(new NumberSearchField(bean.getId()));
        criteria.setIsBillable(new BooleanSearchField(true));
        return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
    }

    @Override
    protected Double getTotalNonBillableHours(SimpleTask bean) {
        ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setType(StringSearchField.and(ProjectTypeConstants.TASK));
        criteria.setTypeId(new NumberSearchField(bean.getId()));
        criteria.setIsBillable(new BooleanSearchField(false));
        return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
    }

    @Override
    protected Double getRemainedHours(SimpleTask bean) {
        if (bean.getRemainestimate() != null) {
            return bean.getRemainestimate();
        }
        return 0d;
    }

    @Override
    protected boolean hasEditPermission() {
        return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS);
    }

    @Override
    protected void showEditTimeView(SimpleTask bean) {
        EventBusFactory.getInstance().post(new ShellEvent.PushView(this, new TaskTimeLogView(bean)));
    }

    private static class TaskTimeLogView extends TimeLogEditView<SimpleTask> {
        private static final long serialVersionUID = -5178708279456191875L;

        protected TaskTimeLogView(SimpleTask bean) {
            super(bean);
        }

        @Override
        protected void saveTimeInvest(double spentHours, boolean isBillable, Date forDate) {
            ItemTimeLogging item = new ItemTimeLogging();
            item.setLoguser(AppContext.getUsername());
            item.setLogvalue(spentHours);
            item.setTypeid(bean.getId());
            item.setType(ProjectTypeConstants.TASK);
            item.setSaccountid(AppContext.getAccountId());
            item.setProjectid(CurrentProjectVariables.getProjectId());
            item.setLogforday(forDate);
            item.setIsbillable(isBillable);

            itemTimeLoggingService.saveWithSession(item, AppContext.getUsername());
        }

        @Override
        protected void updateTimeRemain(double newValue) {
            ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
            bean.setRemainestimate(newValue);
            taskService.updateWithSession(bean, AppContext.getUsername());
        }

        @Override
        protected ItemTimeLoggingSearchCriteria getItemSearchCriteria() {
            ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setType(StringSearchField.and(ProjectTypeConstants.TASK));
            searchCriteria.setTypeId(new NumberSearchField(bean.getId()));
            return searchCriteria;
        }

        @Override
        protected double getEstimateRemainTime() {
            if (bean.getRemainestimate() != null) {
                return bean.getRemainestimate();
            }
            return 0;
        }

        @Override
        protected boolean isEnableAdd() {
            return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS);
        }
    }
}
