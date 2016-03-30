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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.esofthead.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ActivityListViewImpl extends AbstractListItemComp<ActivitySearchCriteria, SimpleActivity> implements ActivityListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        // do nothing

    }

    @Override
    protected DefaultGenericSearchPanel<ActivitySearchCriteria> createSearchPanel() {
        return new ActivitySearchPanel();
    }

    @Override
    protected AbstractPagedBeanTable<ActivitySearchCriteria, SimpleActivity> createBeanTable() {
        return new ActivityTableDisplay(
                new TableViewField(null, "selected", UIConstants.TABLE_CONTROL_WIDTH),
                Arrays.asList(
                        new TableViewField(ActivityI18nEnum.FORM_SUBJECT, "subject", UIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(ActivityI18nEnum.FORM_STATUS, "status", UIConstants.TABLE_S_LABEL_WIDTH),
                        new TableViewField(TaskI18nEnum.FORM_START_DATE, "startDate", UIConstants.TABLE_DATE_TIME_WIDTH),
                        new TableViewField(TaskI18nEnum.TABLE_END_DATE_HEADER, "endDate", UIConstants.TABLE_DATE_TIME_WIDTH)));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();
        if (AppContext.canAccess(RolePermissionCollections.CRM_CALL)
                || AppContext.canAccess(RolePermissionCollections.CRM_MEETING)
                || AppContext.canAccess(RolePermissionCollections.CRM_TASK)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        return container;
    }
}
