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
package com.esofthead.mycollab.mobile.module.project.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class MessageEvent {
    public static class GotoAdd extends ApplicationEvent {
        private static final long serialVersionUID = -6704245340724816519L;

        public GotoAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoList extends ApplicationEvent {
        private static final long serialVersionUID = -834543150969461602L;

        public GotoList(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoRead extends ApplicationEvent {
        private static final long serialVersionUID = 134479710975293923L;

        public GotoRead(Object source, Object data) {
            super(source, data);
        }
    }
}
