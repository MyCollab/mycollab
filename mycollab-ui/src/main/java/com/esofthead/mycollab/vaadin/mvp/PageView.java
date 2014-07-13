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
import java.lang.reflect.Method;
import java.util.EventListener;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.util.ReflectTools;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public interface PageView extends ComponentContainer, CacheableComponent {

	ComponentContainer getWidget();

	<E> void addViewListener(ViewListener<E> listener);

	public static interface ViewListener<E> extends EventListener, Serializable {
		public static final Method viewInitMethod = ReflectTools.findMethod(
				ViewListener.class, "initView", ViewEvent.class);

		void initView(ViewEvent<E> event);
	}

	public static class ViewEvent<E> extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public static final String VIEW_IDENTIFIER = "viewevent";

		public ViewEvent(E data) {
			super(null, data);
		}
	}
}
