package com.mycollab.module.crm.view;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class CrmHomePresenter extends CrmGenericPresenter<CrmHomeView> {
    private static final long serialVersionUID = 1L;

    public CrmHomePresenter() {
        super(CrmHomeView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        super.onGo(container, data);
        CrmModule.navigateItem(CrmTypeConstants.DASHBOARD);
        view.lazyLoadView();
        AppUI.addFragment("crm/dashboard", "Customer Dashboard");
    }
}
