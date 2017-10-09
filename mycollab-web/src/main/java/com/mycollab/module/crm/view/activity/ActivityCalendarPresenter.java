package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityCalendarPresenter extends CrmGenericPresenter<ActivityCalendarView> {
    private static final long serialVersionUID = 1L;

    public ActivityCalendarPresenter() {
        super(ActivityCalendarView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.ACTIVITY);
        super.onGo(container, data);

        AppUI.addFragment("crm/activity/calendar", "Calendar");
    }
}
