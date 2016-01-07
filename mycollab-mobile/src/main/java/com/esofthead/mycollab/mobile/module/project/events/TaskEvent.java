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
 * @since 4.5.0
 */
public class TaskEvent {

    public static class GotoList extends ApplicationEvent {
        private static final long serialVersionUID = 1461869492520239192L;

        public GotoList(Object source, Object data) {
            super(source, data);
        }

    }

    public static class GoInsideList extends ApplicationEvent {
        private static final long serialVersionUID = -2449868721849225816L;

        public GoInsideList(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoRead extends ApplicationEvent {
        private static final long serialVersionUID = -7944303423876647139L;

        public GotoRead(Object source, Object data) {
            super(source, data);
        }

    }

    public static class GotoEdit extends ApplicationEvent {
        private static final long serialVersionUID = 5697543588752387671L;

        public GotoEdit(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoAdd extends ApplicationEvent {
        private static final long serialVersionUID = -6613540831986571319L;

        public GotoAdd(Object source, Object data) {
            super(source, data);
        }

    }

    public static class GotoListView extends ApplicationEvent {
        private static final long serialVersionUID = -2630995127862780065L;

        public GotoListView(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoListAdd extends ApplicationEvent {
        private static final long serialVersionUID = 6167694649921999351L;

        public GotoListAdd(Object source, Object data) {
            super(source, data);
        }

    }

    public static class GotoListEdit extends ApplicationEvent {
        private static final long serialVersionUID = 4065678842068562308L;

        public GotoListEdit(Object source, Object data) {
            super(source, data);
        }

    }

}
