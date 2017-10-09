package com.mycollab.module.crm.view.activity;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ActivityRootView extends AbstractVerticalPageView {
    private static final long serialVersionUID = 1L;

    private final VerticalTabsheet activityTabs;

    private ActivityCalendarPresenter calendarPresenter;
    private ActivityPresenter eventPresenter;

    public ActivityRootView() {
        this.setSizeFull();

        final CssLayout contentWrapper = new CssLayout();
        contentWrapper.setStyleName("verticalTabView");
        contentWrapper.setWidth("100%");
        this.addComponent(contentWrapper);

        HorizontalLayout root = new HorizontalLayout();
        root.setStyleName("menuContent");

        activityTabs = new VerticalTabsheet();
        activityTabs.setSizeFull();
        activityTabs.setNavigatorWidth("170px");
        activityTabs.setNavigatorStyleName("sidebar-menu");
        activityTabs.setHeight(null);

        root.addComponent(activityTabs);
        root.setWidth("100%");
        buildComponents();
        contentWrapper.addComponent(root);
    }

    private void buildComponents() {
        activityTabs.addTab(constructCalendarView(), "calendar",
                UserUIContext.getMessage(ActivityI18nEnum.TAB_CALENDAR_TITLE),
                new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/22/crm/calendar.png")));

        activityTabs.addTab(constructActivityListView(), "activities",
                UserUIContext.getMessage(ActivityI18nEnum.TAB_ACTIVITY_TITLE),
                new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/22/crm/activitylist.png")));

        activityTabs.addSelectedTabChangeListener(selectedTabChangeEvent -> {
            Tab tab = ((VerticalTabsheet) selectedTabChangeEvent.getSource()).getSelectedTab();
            String caption = tab.getCaption();

            if (UserUIContext.getMessage(ActivityI18nEnum.TAB_CALENDAR_TITLE).equals(caption)) {
                calendarPresenter.go(ActivityRootView.this, new ActivityScreenData.GotoCalendar());
            } else if (UserUIContext.getMessage(ActivityI18nEnum.TAB_ACTIVITY_TITLE).equals(caption)) {
                ActivitySearchCriteria criteria = new ActivitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
                eventPresenter.go(ActivityRootView.this, new ActivityScreenData.GotoActivityList(criteria));
            }
        });
    }

    private HasComponents constructCalendarView() {
        calendarPresenter = PresenterResolver.getPresenter(ActivityCalendarPresenter.class);
        return calendarPresenter.getView();
    }

    private ComponentContainer constructActivityListView() {
        eventPresenter = PresenterResolver.getPresenter(ActivityPresenter.class);
        return eventPresenter.getView();
    }

    public Component gotoView(String viewName) {
        return activityTabs.selectTab(viewName);
    }

}
