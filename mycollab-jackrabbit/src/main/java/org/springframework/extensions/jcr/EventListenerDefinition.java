/**
 * This file is part of mycollab-jackrabbit.
 *
 * mycollab-jackrabbit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-jackrabbit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-jackrabbit.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.springframework.extensions.jcr;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventListener;

import org.springframework.beans.factory.InitializingBean;

/**
 * Transport class used for registering event types inside a workspace. It contains defaults for all
 * properties except the listener (obviously):
 * <ol>
 * <li>absPath = "/"</li>
 * <li>eventTypes = Event.NODE_ADDED | Event.NODE_REMOVED | Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED |
 * Event.PROPERTY_REMOVED</li>
 * <li>deep = true</li>
 * <li>uuid = null</li>
 * <li>nodeTypeName = null</li>
 * <li>noLocal = false</li>
 * </ol>
 * @see javax.jcr.observation.ObservationManager#addEventListener(javax.jcr.observation.EventListener, int,
 *      java.lang.String, boolean, java.lang.String[], java.lang.String[], boolean)
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class EventListenerDefinition implements InitializingBean {

    private EventListener listener;
    private int eventTypes = Event.NODE_ADDED | Event.NODE_REMOVED | Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED
            | Event.PROPERTY_REMOVED;

    String absPath = "/";
    boolean deep = true;
    String[] uuid;
    String[] nodeTypeName;
    boolean noLocal = false;

    /**
     * @return Returns the absPath.
     */
    public String getAbsPath() {
        return absPath;
    }

    /**
     * @param absPath The absPath to set.
     */
    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    /**
     * @return Returns the eventTypes.
     */
    public int getEventTypes() {
        return eventTypes;
    }

    /**
     * @param eventTypes The eventTypes to set.
     */
    public void setEventTypes(int eventTypes) {
        this.eventTypes = eventTypes;
    }

    /**
     * @return Returns the isDeep.
     */
    public boolean isDeep() {
        return deep;
    }

    /**
     * @param isDeep The isDeep to set.
     */
    public void setDeep(boolean deep) {
        this.deep = deep;
    }

    /**
     * @return Returns the listener.
     */
    public EventListener getListener() {
        return listener;
    }

    /**
     * @param listener The listener to set.
     */
    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    /**
     * @return Returns the nodeTypeName.
     */
    public String[] getNodeTypeName() {
        return nodeTypeName;
    }

    /**
     * @param nodeTypeName The nodeTypeName to set.
     */
    public void setNodeTypeName(String[] nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    /**
     * @return Returns the noLocal.
     */
    public boolean isNoLocal() {
        return noLocal;
    }

    /**
     * @param noLocal The noLocal to set.
     */
    public void setNoLocal(boolean noLocal) {
        this.noLocal = noLocal;
    }

    /**
     * @return Returns the uuid.
     */
    public String[] getUuid() {
        return uuid;
    }

    /**
     * @param uuid The uuid to set.
     */
    public void setUuid(String[] uuid) {
        this.uuid = uuid;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (listener == null)
            throw new IllegalArgumentException("listener is required");
    }

}
