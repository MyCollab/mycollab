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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.common.SessionIdGenerator;
import com.esofthead.mycollab.core.SessionExpireException;
import com.esofthead.mycollab.vaadin.MyCollabUI;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MyCollabSession {

    public static final String EVENT_BUS_VAL = "eventBusVal";

    public static final String CURRENT_PROJECT = "project";

    public static final String PROJECT_MEMBER = "project_member";

    public static final String USER_TIMEZONE = "USER_TIMEZONE";

    public static final String CURRENT_MODULE = "currentModule";

    public static final String CONTROLLER_REGISTRY = "CONTROLLER_REGISTRY";

    public static final String HISTORY_VAL = "historyVal";

    public static final String PRESENTER_VAL = "presenterMap";

    public static final String VIEW_MANAGER_VAL = "viewMap";

    private MyCollabSession() {
    }

    /**
     * @param key
     * @param value
     */
    public static void putVariable(String key, Object value) {
        try {
            ((MyCollabUI) UI.getCurrent()).setAttribute(key, value);
        } catch (Exception e) {
            throw new SessionExpireException("Expire Exception");
        }
    }

    /**
     * @param key
     */
    public static void removeVariable(String key) {
        try {
            ((MyCollabUI) UI.getCurrent()).setAttribute(key, null);
        } catch (Exception e) {
            throw new SessionExpireException("Expire Exception");
        }
    }

    /**
     * @param key
     * @return
     */
    public static Object getVariable(String key) {
        try {
            return ((MyCollabUI) UI.getCurrent()).getAttribute(key);
        } catch (Exception e) {
            throw new SessionExpireException("Expire Exception");
        }
    }

    public static String getSessionId() {
        return SessionIdGenerator.getSessionId();
    }
}