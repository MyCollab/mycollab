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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class ContactRoleEditPresenter extends CrmGenericPresenter<ContactRoleEditView> {
    private static final long serialVersionUID = 1L;

    public ContactRoleEditPresenter() {
        super(ContactRoleEditView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
            SimpleOpportunity opportunity = (SimpleOpportunity) data.getParams();
            super.onGo(container, data);
            view.display(opportunity);

            AppContext.addFragment("crm/opportunity/addcontactroles", "Add Contact Roles");
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }

}
