/**
 * Copyright 2009-2012 the original author or authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
