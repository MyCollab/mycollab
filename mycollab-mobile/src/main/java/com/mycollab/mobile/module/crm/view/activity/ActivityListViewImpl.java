package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.crm.event.ActivityEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListViewComp;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.module.crm.i18n.TaskI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class ActivityListViewImpl extends AbstractListViewComp<ActivitySearchCriteria, SimpleActivity> implements ActivityListView {
    private static final long serialVersionUID = -7632616933330982900L;

    public ActivityListViewImpl() {
        setCaption(UserUIContext.getMessage(ActivityI18nEnum.TAB_ACTIVITY_TITLE));
    }

    @Override
    protected AbstractPagedBeanList<ActivitySearchCriteria, SimpleActivity> createBeanTable() {
        return new ActivityListDisplay();
    }

    @Override
    protected Component createRightComponent() {
        final NavigationBarQuickMenu addActivity = new NavigationBarQuickMenu();
        addActivity.setStyleName("add-btn");

        MVerticalLayout addButtons = new MVerticalLayout().withFullWidth();

        Button addTask = new Button(UserUIContext.getMessage(TaskI18nEnum.NEW), clickEvent -> EventBusFactory.getInstance().post(
                new ActivityEvent.TaskAdd(this, null))
        );
        addButtons.addComponent(addTask);

        Button addCall = new Button(UserUIContext.getMessage(CallI18nEnum.NEW), clickEvent -> EventBusFactory.getInstance().post(
                new ActivityEvent.CallAdd(this, null)));
        addButtons.addComponent(addCall);

        Button addMeeting = new Button(UserUIContext.getMessage(MeetingI18nEnum.NEW), clickEvent -> EventBusFactory.getInstance().post(
                new ActivityEvent.MeetingAdd(this, null)));
        addButtons.addComponent(addMeeting);

        addActivity.setContent(addButtons);
        return addActivity;
    }
}
