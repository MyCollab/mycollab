/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
