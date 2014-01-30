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
package com.esofthead.mycollab.module.crm.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class ActivityEvent {

    public static class GotoCalendar extends ApplicationEvent {

        public GotoCalendar(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoTodoList extends ApplicationEvent {

        public GotoTodoList(Object source, Object data) {
            super(source, data);
        }
    }

    public static class TaskAdd extends ApplicationEvent {

        public TaskAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class TaskEdit extends ApplicationEvent {

        public TaskEdit(Object source, Object data) {
            super(source, data);
        }
    }

    public static class TaskRead extends ApplicationEvent {

        public TaskRead(Object source, Object data) {
            super(source, data);
        }
    }

    public static class MeetingAdd extends ApplicationEvent {

        public MeetingAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class MeetingEdit extends ApplicationEvent {

        public MeetingEdit(Object source, Object data) {
            super(source, data);
        }
    }

    public static class MeetingRead extends ApplicationEvent {

        public MeetingRead(Object source, Object data) {
            super(source, data);
        }
    }

    public static class CallAdd extends ApplicationEvent {

        public CallAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class CallEdit extends ApplicationEvent {

        public CallEdit(Object source, Object data) {
            super(source, data);
        }
    }

    public static class CallRead extends ApplicationEvent {

        public CallRead(Object source, Object data) {
            super(source, data);
        }
    }
}
