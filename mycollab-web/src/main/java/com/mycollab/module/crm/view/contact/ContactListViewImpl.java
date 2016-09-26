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
package com.mycollab.module.crm.view.contact;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.fielddef.ContactTableFieldDef;
import com.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ContactListViewImpl extends AbstractListItemComp<ContactSearchCriteria, SimpleContact> implements ContactListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton()
                .withListener(clickEvent -> UI.getCurrent().addWindow(new ContactListCustomizeWindow(tableItem)));
        this.addExtraButton(customizeViewBtn);

        MButton importBtn = ComponentUtils.createImportEntitiesButton()
                .withListener(clickEvent -> UI.getCurrent().addWindow(new ContactImportWindow()))
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT));
        this.addExtraButton(importBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<ContactSearchCriteria> createSearchPanel() {
        return new ContactSearchPanel();
    }

    @Override
    protected AbstractPagedBeanTable<ContactSearchCriteria, SimpleContact> createBeanTable() {
        return new ContactTableDisplay(CrmTypeConstants.CONTACT, ContactTableFieldDef.selected(),
                Arrays.asList(ContactTableFieldDef.name(), ContactTableFieldDef.title(),
                        ContactTableFieldDef.account(), ContactTableFieldDef.email(), ContactTableFieldDef.phoneOffice()));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_CONTACT)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new ContactCrmListNoItemView());
    }
}
