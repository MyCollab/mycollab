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
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.EventService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd
 * @since 1.0
 */
public class ActivityListDisplay extends DefaultPagedBeanList<EventService, ActivitySearchCriteria, SimpleActivity> {
    private static final long serialVersionUID = -2050012123292483060L;

    public ActivityListDisplay() {
        super(ApplicationContextUtil.getSpringBean(EventService.class), new ActivityRowDisplayHandler());
    }

    static public class ActivityRowDisplayHandler implements RowDisplayHandler<SimpleActivity> {

        @Override
        public Component generateRow(final SimpleActivity simpleEvent, int rowIndex) {
            Button b = new Button(simpleEvent.getSubject(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (simpleEvent.getEventType().equals(CrmTypeConstants.TASK)) {
                        EventBusFactory.getInstance().post(new ActivityEvent.TaskRead(this, simpleEvent.getId()));
                    } else if (simpleEvent.getEventType().equals(CrmTypeConstants.CALL)) {
                        EventBusFactory.getInstance().post(new ActivityEvent.CallRead(this, simpleEvent.getId()));
                    } else if (simpleEvent.getEventType().equals(CrmTypeConstants.MEETING)) {
                        EventBusFactory.getInstance().post(new ActivityEvent.MeetingRead(this, simpleEvent.getId()));
                    }
                }
            });

            if ("Held".equals(simpleEvent.getStatus())) {
                b.addStyleName(UIConstants.LINK_COMPLETED);
            } else {
                if (simpleEvent.getEndDate() != null && (simpleEvent.getEndDate().before(new GregorianCalendar().getTime()))) {
                    b.addStyleName(UIConstants.LINK_OVERDUE);
                }
            }
            b.setWidth("100%");
            return b;
        }
    }
}
