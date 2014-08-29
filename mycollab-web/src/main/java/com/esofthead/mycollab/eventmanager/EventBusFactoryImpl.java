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
package com.esofthead.mycollab.eventmanager;

import static com.esofthead.mycollab.common.MyCollabSession.EVENT_BUS_VAL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class EventBusFactoryImpl extends EventBusFactory {
	private static Logger log = LoggerFactory
			.getLogger(EventBusFactoryImpl.class);

	@Override
	EventBus getInstanceInSession() {
		EventBus eventBus = (EventBus) MyCollabSession
				.getVariable(EVENT_BUS_VAL);
		log.debug("Event bus {}", eventBus);
		if (eventBus == null) {
			eventBus = new EventBus(new SubscriberEventBusExceptionHandler());
			MyCollabSession.putVariable(EVENT_BUS_VAL, eventBus);
			log.debug("Create new event bus {}", eventBus);
		}
		return eventBus;
	}

	private static class SubscriberEventBusExceptionHandler implements
			SubscriberExceptionHandler {

		@Override
		public void handleException(Throwable exception,
				SubscriberExceptionContext context) {
			EventBusFactory
					.getInstance()
					.post(new ShellEvent.NotifyErrorEvent(
							SubscriberEventBusExceptionHandler.this, exception));
		}

	}
}
