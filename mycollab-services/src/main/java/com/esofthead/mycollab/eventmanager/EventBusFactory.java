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

import com.esofthead.mycollab.core.MyCollabException;
import com.google.common.eventbus.EventBus;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("unchecked")
public abstract class EventBusFactory {
	private static String eventbusFactoryImplClsName = "com.esofthead.mycollab.eventmanager.EventBusFactoryImpl";

	private static EventBusFactory eventbusFactoryImpl;

	static {
		try {
			Class<EventBusFactory> cls = (Class<EventBusFactory>) Class
					.forName(eventbusFactoryImplClsName);
			eventbusFactoryImpl = cls.newInstance();
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	abstract EventBus getInstanceInSession();

	public static EventBus getInstance() {
		return eventbusFactoryImpl.getInstanceInSession();
	}
}
