package com.mycollab.module.crm.view.file;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class FileDashboardPresenter extends CrmGenericPresenter<FileDashboardView> {
    private static final long serialVersionUID = 1L;

    public FileDashboardPresenter() {
        super(FileDashboardView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.FILE);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_DOCUMENT)) {
            super.onGo(container, data);
            view.displayFiles();
            AppUI.addFragment("crm/file/dashboard", "Customer: File Dashboard");
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
