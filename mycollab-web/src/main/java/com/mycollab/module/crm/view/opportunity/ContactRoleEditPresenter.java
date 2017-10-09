package com.mycollab.module.crm.view.opportunity;

import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

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
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
            SimpleOpportunity opportunity = (SimpleOpportunity) data.getParams();
            super.onGo(container, data);
            view.display(opportunity);

            AppUI.addFragment("crm/opportunity/addcontactroles", "Add Contact Roles");
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }

}
