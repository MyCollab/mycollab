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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractCssPageView extends CssLayout implements
		PageView, Serializable {

	private static Logger log = LoggerFactory
			.getLogger(AbstractCssPageView.class);
	private Map<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>> map;

	private CssLayout vTabsheetFix;
	private String vTabsheetFixWidth = "250px";
	private boolean vTabsheetIsLeft = true;
	private boolean showTabsheetFix = false;

	public AbstractCssPageView() {
		this.setStyleName("page-view");
	}

	protected void updateVerticalTabsheetFixStatus() {
		if (showTabsheetFix) {
			if (vTabsheetFix == null) {
				vTabsheetFix = new CssLayout();
				vTabsheetFix.setStyleName("verticaltabsheet-fix");
				this.addComponentAsFirst(vTabsheetFix);
			} else if (vTabsheetFix.getParent() != this) {
				this.addComponentAsFirst(vTabsheetFix);
			}
			vTabsheetFix.setWidth(this.vTabsheetFixWidth);
			if (this.vTabsheetIsLeft)
				vTabsheetFix.addStyleName("is-left");
			else
				vTabsheetFix.removeStyleName("is-left");
		} else {
			if (vTabsheetFix != null && vTabsheetFix.getParent() == this) {
				this.removeComponent(vTabsheetFix);
			}
		}
	}

	public void setVerticalTabsheetFix(boolean value) {
		this.showTabsheetFix = value;
		updateVerticalTabsheetFixStatus();
	}

	public void setVerticalTabsheetFixWidth(String width) {
		this.vTabsheetFixWidth = width;
		updateVerticalTabsheetFixStatus();
	}

	public void setVerticalTabsheetFixToLeft(boolean isLeft) {
		this.vTabsheetIsLeft = isLeft;
		updateVerticalTabsheetFixStatus();
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@Override
	public void addViewListener(
			ApplicationEventListener<? extends ApplicationEvent> listener) {
		if (map == null) {
			map = new HashMap<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>>();
		}

		Set<ApplicationEventListener<?>> listenerSet = map.get(listener
				.getEventType());
		if (listenerSet == null) {
			listenerSet = new LinkedHashSet<ApplicationEventListener<?>>();
			map.put(listener.getEventType(), listenerSet);
		}

		listenerSet.add(listener);
	}

	protected void fireEvent(ApplicationEvent event) {

		log.debug("Fire event: {}", event);

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
