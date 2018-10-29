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
package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.fielddef.CaseTableFieldDef;
import com.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.table.AbstractPagedGrid;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class CaseListViewImpl extends AbstractListItemComp<CaseSearchCriteria, SimpleCase> implements CaseListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton().withListener(clickEvent -> UI.getCurrent().addWindow(
                new CaseListCustomizeWindow(grid)));
        this.addExtraButton(customizeViewBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<CaseSearchCriteria> createSearchPanel() {
        return new CaseSearchPanel(true);
    }

    @Override
    protected AbstractPagedGrid<CaseSearchCriteria, SimpleCase> createGrid() {
        return new CaseTableDisplay(
                CrmTypeConstants.CASE, CaseTableFieldDef.selected,
                Arrays.asList(CaseTableFieldDef.subject, CaseTableFieldDef.account,
                        CaseTableFieldDef.priority, CaseTableFieldDef.status,
                        CaseTableFieldDef.assignUser, CaseTableFieldDef.createdTime));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_CASE)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CASE)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new CaseCrmListNoItemView());
    }
}
