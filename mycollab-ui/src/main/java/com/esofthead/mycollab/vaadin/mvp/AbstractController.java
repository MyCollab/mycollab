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
package com.esofthead.mycollab.vaadin.mvp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.google.common.eventbus.EventBus;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public abstract class AbstractController implements Serializable {
	private static final long serialVersionUID = 1L;

	private EventBus eventBus = EventBusFactory.getInstance();
	private List<Object> subscribers = new ArrayList<Object>();

	public void register(Object subscriber) {
		eventBus.register(subscriber);
		subscribers.add(subscriber);
	}

	public void unregisterAll() {
		for (Object subscriber : subscribers) {
			eventBus.unregister(subscriber);
		}
	}

}
