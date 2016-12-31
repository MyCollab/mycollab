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
package com.mycollab.mobile.module.project.view.bug;

import com.google.common.base.MoreObjects;
import com.mycollab.db.arguments.BooleanSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.mobile.module.project.ui.TimeLogComp;
import com.mycollab.mobile.module.project.ui.TimeLogEditView;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ItemTimeLogging;
import com.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugTimeLogComp extends TimeLogComp<SimpleBug> {
    private static final long serialVersionUID = -7045421785222291708L;

    @Override
    protected Double getTotalBillableHours(SimpleBug bean) {
        ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setType(StringSearchField.and(ProjectTypeConstants.BUG));
        criteria.setTypeId(new NumberSearchField(bean.getId()));
        criteria.setIsBillable(new BooleanSearchField(true));
        return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
    }

    @Override
    protected Double getTotalNonBillableHours(SimpleBug bean) {
        ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setType(StringSearchField.and(ProjectTypeConstants.BUG));
        criteria.setTypeId(new NumberSearchField(bean.getId()));
        criteria.setIsBillable(new BooleanSearchField(false));
        return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
    }

    @Override
    protected Double getRemainedHours(SimpleBug bean) {
       return MoreObjects.firstNonNull(bean.getRemainestimate(), 0d);
    }

    @Override
    protected boolean hasEditPermission() {
        return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS);
    }

    @Override
    protected void showEditTimeView(SimpleBug bean) {
        EventBusFactory.getInstance().post(new ShellEvent.PushView(this, new BugTimeLogView(bean)));
    }

    private static class BugTimeLogView extends TimeLogEditView<SimpleBug> {
        private static final long serialVersionUID = -482636222620345326L;

        BugTimeLogView(SimpleBug bean) {
            super(bean);
        }

        @Override
        protected void saveTimeInvest(double spentHours, boolean isBillable, Date forDate) {
            ItemTimeLogging item = new ItemTimeLogging();
            item.setLoguser(UserUIContext.getUsername());
            item.setLogvalue(spentHours);
            item.setTypeid(bean.getId());
            item.setType(ProjectTypeConstants.BUG);
            item.setSaccountid(MyCollabUI.getAccountId());
            item.setProjectid(CurrentProjectVariables.getProjectId());
            item.setLogforday(forDate);
            item.setIsbillable(isBillable);

            itemTimeLoggingService.saveWithSession(item, UserUIContext.getUsername());
        }

        @Override
        protected void updateTimeRemain(double newValue) {
            BugService bugService = AppContextUtil.getSpringBean(BugService.class);
            bean.setRemainestimate(newValue);
            bugService.updateWithSession(bean, UserUIContext.getUsername());
        }

        @Override
        protected ItemTimeLoggingSearchCriteria getItemSearchCriteria() {
            ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setType(StringSearchField.and(ProjectTypeConstants.BUG));
            searchCriteria.setTypeId(new NumberSearchField(bean.getId()));
            return searchCriteria;
        }

        @Override
        protected double getEstimateRemainTime() {
            return MoreObjects.firstNonNull(bean.getRemainestimate(), 0d);
        }

        @Override
        protected boolean isEnableAdd() {
            return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS);
        }
    }
}
