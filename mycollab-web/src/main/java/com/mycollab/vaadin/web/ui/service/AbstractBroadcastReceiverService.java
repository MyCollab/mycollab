/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui.service;

import com.google.common.eventbus.EventBus;
import com.mycollab.cache.service.CacheService;
import com.mycollab.core.AbstractNotification;
import com.mycollab.core.BroadcastMessage;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.MyCollabSession;
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
            EventBus eventBus = (EventBus) myCollabApp.getAttribute(MyCollabSession.EVENT_BUS_VAL);
            eventBus.post(new ShellEvent.NewNotification(this, message.getWrapObj()));

            CacheService cacheService = AppContextUtil.getSpringBean(CacheService.class);
            if (message.getsAccountId() != null) {
                cacheService.putValue(message.getsAccountId() + "", "notification", message.getWrapObj());
            }
        } else {
            onBroadcast(message);
        }
    }

    abstract protected void onBroadcast(BroadcastMessage message);
}
