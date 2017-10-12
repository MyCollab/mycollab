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
package com.mycollab.community.vaadin.ui.service;

import com.mycollab.core.AbstractNotification;
import com.mycollab.core.BroadcastMessage;
import com.mycollab.vaadin.web.ui.service.AbstractBroadcastReceiverService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BroadcastReceiverServiceImpl extends AbstractBroadcastReceiverService {
    @Override
    protected void onBroadcast(BroadcastMessage message) {
        Object wrapObj = message.getWrapObj();
        if (wrapObj instanceof AbstractNotification) {
            AbstractNotification notification = (AbstractNotification) wrapObj;
        }
    }
}
