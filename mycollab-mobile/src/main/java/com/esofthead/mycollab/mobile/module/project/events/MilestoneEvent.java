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
 * @since 4.5.2
 */
public class MilestoneEvent {

    public static class GotoList extends ApplicationEvent {
        private static final long serialVersionUID = 5974983067244370237L;

        public GotoList(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoAdd extends ApplicationEvent {
        private static final long serialVersionUID = 4004012882662987445L;

        public GotoAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoRead extends ApplicationEvent {
        private static final long serialVersionUID = -3386533751870834834L;

        public GotoRead(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoEdit extends ApplicationEvent {
        private static final long serialVersionUID = 4905749540330317402L;

        public GotoEdit(Object source, Object data) {
            super(source, data);
        }
    }
}
