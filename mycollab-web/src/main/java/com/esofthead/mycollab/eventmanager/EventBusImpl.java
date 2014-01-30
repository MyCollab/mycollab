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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
class EventBusImpl extends EventBus {
	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(EventBusImpl.class);

	private Map<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>> map = new HashMap<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>>();

	EventBusImpl() {

	}

	@Override
	public void addListener(ApplicationEventListener<?> listener) {

		Set<ApplicationEventListener<?>> listenerSet = map.get(listener
				.getEventType());
		if (listenerSet == null) {
			listenerSet = new LinkedHashSet<ApplicationEventListener<?>>();
			map.put(listener.getEventType(), listenerSet);
		}

		listenerSet.add(listener);

		log.debug("Added event bus listener: {}", listener.getClass());

	}

	@Override
	public void removeListener(ApplicationEventListener<?> listener) {
		Set<ApplicationEventListener<?>> listenerSet = map.get(listener
				.getEventType());
		listenerSet.remove(listener);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void fireEvent(ApplicationEvent event) {
		Class<? extends ApplicationEvent> eventType = event.getClass();

		Set<ApplicationEventListener<?>> eventSet = map.get(eventType);
		if (eventSet != null) {
			Iterator<ApplicationEventListener<?>> listenerSet = map.get(
					eventType).iterator();

			while (listenerSet.hasNext()) {
				ApplicationEventListener<?> listener = listenerSet.next();
				@SuppressWarnings("unchecked")
				ApplicationEventListener<ApplicationEvent> l = (ApplicationEventListener<ApplicationEvent>) listener;
				l.handle(event);
			}
		} else {
			log.error("No listener is registered for event type " + eventType);
		}

	}
}
