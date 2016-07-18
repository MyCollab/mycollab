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
package com.mycollab.vaadin.web.ui.service;

import com.google.common.eventbus.EventBus;
import com.mycollab.core.AbstractNotification;
import com.mycollab.core.BroadcastMessage;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.web.DesktopApplication;

import static com.mycollab.vaadin.ui.MyCollabSession.EVENT_BUS_VAL;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
public abstract class AbstractBroadcastReceiverService implements BroadcastReceiverService {

    protected DesktopApplication myCollabApp;

    public void registerApp(DesktopApplication myCollabApp) {
        this.myCollabApp = myCollabApp;
    }

    @Override
    public void broadcast(BroadcastMessage message) {
        if (message.getWrapObj() instanceof AbstractNotification) {
            EventBus eventBus = (EventBus) myCollabApp.getAttribute(EVENT_BUS_VAL);
            eventBus.post(new ShellEvent.NewNotification(this, message.getWrapObj()));
        } else {
            onBroadcast(message);
        }
    }

    abstract protected void onBroadcast(BroadcastMessage message);
}
