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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui.service

import com.google.common.eventbus.EventBus
import com.mycollab.cache.service.CacheService
import com.mycollab.core.AbstractNotification
import com.mycollab.core.BroadcastMessage
import com.mycollab.shell.event.ShellEvent
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.ui.MyCollabSession
import com.mycollab.web.DesktopApplication

import com.mycollab.vaadin.ui.MyCollabSession.EVENT_BUS_VAL

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
abstract class AbstractBroadcastReceiverService : BroadcastReceiverService {

    protected lateinit var myCollabApp: DesktopApplication

    override fun registerApp(myCollabApp: DesktopApplication) {
        this.myCollabApp = myCollabApp
    }

    override fun broadcast(message: BroadcastMessage) {
        if (message.wrapObj is AbstractNotification) {
            val eventBus = myCollabApp.getAttribute(MyCollabSession.EVENT_BUS_VAL) as EventBus?
            eventBus!!.post(ShellEvent.NewNotification(this, message.wrapObj))

            val cacheService = AppContextUtil.getSpringBean(CacheService::class.java)
            if (message.getsAccountId() != null) {
                cacheService.putValue(message.getsAccountId()!!.toString(), "notification", message.wrapObj)
            }
        } else {
            onBroadcast(message)
        }
    }

    protected abstract fun onBroadcast(message: BroadcastMessage)
}
