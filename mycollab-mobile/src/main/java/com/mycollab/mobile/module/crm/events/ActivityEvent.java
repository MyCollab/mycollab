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
package com.mycollab.mobile.module.crm.events;

import com.mycollab.events.ApplicationEvent;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ActivityEvent {
    public static class TaskAdd extends ApplicationEvent {
        private static final long serialVersionUID = -297079855849498749L;

        public TaskAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class TaskEdit extends ApplicationEvent {
        private static final long serialVersionUID = -3058925851053273466L;

        public TaskEdit(Object source, Object data) {
            super(source, data);
        }
    }

    public static class TaskRead extends ApplicationEvent {
        private static final long serialVersionUID = 3729273634721320786L;

        public TaskRead(Object source, Object data) {
            super(source, data);
        }
    }

    public static class MeetingAdd extends ApplicationEvent {
        private static final long serialVersionUID = -6087559081474362239L;

        public MeetingAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class MeetingEdit extends ApplicationEvent {
        private static final long serialVersionUID = -5484004166527538238L;

        public MeetingEdit(Object source, Object data) {
            super(source, data);
        }
    }

    public static class MeetingRead extends ApplicationEvent {
        private static final long serialVersionUID = 4660645508301244730L;

        public MeetingRead(Object source, Object data) {
            super(source, data);
        }
    }

    public static class CallAdd extends ApplicationEvent {
        private static final long serialVersionUID = -4152647044241550707L;

        public CallAdd(Object source, Object data) {
            super(source, data);
        }
    }

    public static class CallEdit extends ApplicationEvent {
        private static final long serialVersionUID = 240833870944170101L;

        public CallEdit(Object source, Object data) {
            super(source, data);
        }
    }

    public static class CallRead extends ApplicationEvent {
        private static final long serialVersionUID = 4670213836749669945L;

        public CallRead(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoList extends ApplicationEvent {
        private static final long serialVersionUID = -4100336975752268823L;

        public GotoList(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GoToRelatedItems extends ApplicationEvent {
        private static final long serialVersionUID = 6886861280155117098L;

        public GoToRelatedItems(Object source, Object data) {
            super(source, data);
        }

    }
}
