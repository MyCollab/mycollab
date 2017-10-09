package com.mycollab.module.crm.view.activity;

import com.mycollab.core.utils.ClassUtils;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.mycollab.module.crm.view.parameters.AssignmentScreenData;
import com.mycollab.module.crm.view.parameters.CallScreenData;
import com.mycollab.module.crm.view.parameters.MeetingScreenData;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityRootPresenter extends CrmGenericPresenter<ActivityRootView> {
    private static final long serialVersionUID = 1L;

    public ActivityRootPresenter() {
        super(ActivityRootView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        super.onGo(container, data);
        AbstractPresenter presenter;

        if (ClassUtils.instanceOf(data, AssignmentScreenData.Read.class, AssignmentScreenData.Add.class,
                AssignmentScreenData.Edit.class, MeetingScreenData.Add.class,
                MeetingScreenData.Edit.class, MeetingScreenData.Read.class,
                CallScreenData.Read.class, CallScreenData.Add.class, CallScreenData.Edit.class,
                ActivityScreenData.GotoActivityList.class)) {
            presenter = PresenterResolver.getPresenter(ActivityPresenter.class);
        } else {
            presenter = PresenterResolver.getPresenter(ActivityCalendarPresenter.class);
        }

        presenter.go(view, data);
    }

}
