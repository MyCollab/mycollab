/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.eventmanager;

import java.io.Serializable;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class EventBus implements Serializable {
	private static final long serialVersionUID = 1L;

	private static String eventbusFactoryImplClsName = "com.esofthead.mycollab.eventmanager.EventBusFactoryImpl";

	private static EventBusFactory eventbusFactory;

	static {
		try {
			Class<EventBusFactory> cls = (Class<EventBusFactory>) Class
					.forName(eventbusFactoryImplClsName);
			eventbusFactory = cls.newInstance();
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static EventBus getInstance() {
		return eventbusFactory.getInstance();
	}

	/**
	 * 
	 * @param listener
	 */
	abstract public void addListener(ApplicationEventListener<?> listener);

	/**
	 * 
	 * @param listener
	 */
	abstract public void removeListener(ApplicationEventListener<?> listener);

	/**
	 * 
	 * @param event
	 */
	abstract public void fireEvent(ApplicationEvent event);

	/**
	 * 
	 */
	abstract public void clear();
}
