/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.eventmanager;

import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.EVENT_BUS_VAL;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class EventBusFactoryImpl extends EventBusFactory {
    private static final Logger LOG = LoggerFactory.getLogger(EventBusFactoryImpl.class);

    @Override
    public EventBus getInstanceInSession() {
        EventBus eventBus = (EventBus) MyCollabSession.getVariable(EVENT_BUS_VAL);
        LOG.debug("Event bus {}", eventBus);
        if (eventBus == null) {
            eventBus = new EventBus(new SubscriberEventBusExceptionHandler());
            MyCollabSession.putVariable(EVENT_BUS_VAL, eventBus);
            LOG.debug("Create new event bus {}", eventBus);
        }
        return eventBus;
    }

    private static class SubscriberEventBusExceptionHandler implements SubscriberExceptionHandler {

        @Override
        public void handleException(Throwable e, SubscriberExceptionContext context) {
            LOG.error("Error", e);
        }
    }
}