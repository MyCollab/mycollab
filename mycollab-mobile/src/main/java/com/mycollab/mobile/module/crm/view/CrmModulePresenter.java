package com.mycollab.mobile.module.crm.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.mobile.module.crm.event.CrmEvent;
import com.mycollab.mobile.shell.ModuleHelper;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.2
 */
public class CrmModulePresenter extends AbstractCrmPresenter<CrmModule> {
    private static final long serialVersionUID = -3370467477599009160L;

    public CrmModulePresenter() {
        super(CrmModule.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        ModuleHelper.setCurrentModule(getView());
        UserUIContext.updateLastModuleVisit(ModuleNameConstants.CRM);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            EventBusFactory.getInstance().post(new CrmEvent.GotoActivitiesView(this, null));
            AppUI.addFragment("crm", UserUIContext.getMessage(GenericI18Enum.MODULE_CRM));
        } else {
            MobileApplication.rootUrlResolver.getSubResolver("crm").handle(params);
        }
    }
}
