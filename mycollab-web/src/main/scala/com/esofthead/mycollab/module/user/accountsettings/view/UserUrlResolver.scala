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
package com.esofthead.mycollab.premium.module.user.accountsettings.view

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.events.UserEvent
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class UserUrlResolver extends AccountUrlResolver {
    this.addSubResolver("list", new ListUrlResolver)
    this.addSubResolver("add", new AddUrlResolver)
    this.addSubResolver("edit", new EditUrlResolver)
    this.addSubResolver("preview", new PreviewUrlResolver)

    private class ListUrlResolver extends AccountUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance.post(new UserEvent.GotoList(ListUrlResolver.this, null))
        }
    }

    private class AddUrlResolver extends AccountUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance.post(new UserEvent.GotoAdd(AddUrlResolver.this, null))
        }
    }

    private class EditUrlResolver extends AccountUrlResolver {
        protected override def handlePage(params: String*) {
            val username: String = new UrlTokenizer(params(0)).getString
            val userService: UserService = ApplicationContextUtil.getSpringBean(classOf[UserService])
            val user: SimpleUser = userService.findUserByUserNameInAccount(username, AppContext.getAccountId)
            EventBusFactory.getInstance.post(new UserEvent.GotoEdit(EditUrlResolver.this, user))
        }
    }

    private class PreviewUrlResolver extends AccountUrlResolver {
        protected override def handlePage(params: String*) {
            val username: String = new UrlTokenizer(params(0)).getString
            EventBusFactory.getInstance.post(new UserEvent.GotoRead(PreviewUrlResolver.this, username))
        }
    }

}
