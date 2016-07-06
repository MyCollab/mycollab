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
package com.mycollab.module.crm.view.activity;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AssetResource;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ActivityRootView extends AbstractPageView {
    private static final long serialVersionUID = 1L;

    private final VerticalTabsheet activityTabs;

    private ActivityCalendarPresenter calendarPresenter;
    private ActivityPresenter eventPresenter;

    public ActivityRootView() {
        super();
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
        activityTabs.setContainerStyleName("tab-content");
        activityTabs.setHeight(null);

        root.addComponent(activityTabs);
        root.setWidth("100%");
        buildComponents();
        contentWrapper.addComponent(root);
    }

    private void buildComponents() {
        activityTabs.addTab(constructCalendarView(), "calendar",
                AppContext.getMessage(ActivityI18nEnum.TAB_CALENDAR_TITLE),
                new AssetResource("icons/22/crm/calendar.png"));

        activityTabs.addTab(constructActivityListView(), "activities",
                AppContext.getMessage(ActivityI18nEnum.TAB_ACTIVITY_TITLE),
                new AssetResource("icons/22/crm/activitylist.png"));

        activityTabs
                .addSelectedTabChangeListener(new SelectedTabChangeListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void selectedTabChange(SelectedTabChangeEvent event) {
                        Tab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                        String caption = tab.getCaption();

                        if (AppContext.getMessage(ActivityI18nEnum.TAB_CALENDAR_TITLE).equals(caption)) {
                            calendarPresenter.go(ActivityRootView.this, new ActivityScreenData.GotoCalendar());
                        } else if (AppContext.getMessage(ActivityI18nEnum.TAB_ACTIVITY_TITLE).equals(caption)) {
                            ActivitySearchCriteria criteria = new ActivitySearchCriteria();
                            criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                            eventPresenter.go(ActivityRootView.this, new ActivityScreenData.GotoActivityList(criteria));
                        }
                    }
                });
    }

    private ComponentContainer constructCalendarView() {
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
