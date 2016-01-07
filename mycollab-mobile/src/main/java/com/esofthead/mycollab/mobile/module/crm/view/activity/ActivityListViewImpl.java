/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CallI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
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
    private MVerticalLayout addButtons;

    public ActivityListViewImpl() {
        super();
        setCaption(AppContext.getMessage(ActivityI18nEnum.TAB_ACTIVITY_TITLE));
    }

    @Override
    protected AbstractPagedBeanList<ActivitySearchCriteria, SimpleActivity> createBeanTable() {
        ActivityListDisplay activityListDisplay = new ActivityListDisplay();
        return activityListDisplay;
    }

    @Override
    protected Component createRightComponent() {
        final NavigationBarQuickMenu addActivity = new NavigationBarQuickMenu();
        addActivity.setStyleName("add-btn");

        addButtons = new MVerticalLayout().withWidth("100%");

        Button addTask = new Button(AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, null));
            }
        });
        addButtons.addComponent(addTask);

        Button addCall = new Button(AppContext.getMessage(CallI18nEnum.BUTTON_NEW_CALL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ActivityEvent.CallAdd(this, null));
            }
        });
        addButtons.addComponent(addCall);

        Button addMeeting = new Button(AppContext.getMessage(MeetingI18nEnum.BUTTON_NEW_MEETING), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingAdd(this, null));
            }
        });
        addButtons.addComponent(addMeeting);

        addActivity.setContent(addButtons);
        return addActivity;
    }
}
