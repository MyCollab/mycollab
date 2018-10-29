/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.table.AbstractPagedGrid;

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
    public void showNoItemView() {

    }

    @Override
    protected DefaultGenericSearchPanel<ActivitySearchCriteria> createSearchPanel() {
        return new ActivitySearchPanel();
    }

    @Override
    protected AbstractPagedGrid<ActivitySearchCriteria, SimpleActivity> createGrid() {
        return new ActivityTableDisplay(
                new GridFieldMeta(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH),
                Arrays.asList(
                        new GridFieldMeta(ActivityI18nEnum.FORM_SUBJECT, "subject", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new GridFieldMeta(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_S_LABEL_WIDTH),
                        new GridFieldMeta(GenericI18Enum.FORM_START_DATE, "startDate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                        new GridFieldMeta(GenericI18Enum.FORM_END_DATE, "endDate", WebUIConstants.TABLE_DATE_TIME_WIDTH)));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();
        if (UserUIContext.canAccess(RolePermissionCollections.CRM_CALL)
                || UserUIContext.canAccess(RolePermissionCollections.CRM_MEETING)
                || UserUIContext.canAccess(RolePermissionCollections.CRM_TASK)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        return container;
    }
}
