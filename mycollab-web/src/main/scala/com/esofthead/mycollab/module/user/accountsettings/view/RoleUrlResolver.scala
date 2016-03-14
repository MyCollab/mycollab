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
import com.esofthead.mycollab.module.user.events.RoleEvent
import com.esofthead.mycollab.module.user.service.RoleService
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class RoleUrlResolver extends AccountSettingUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)

  private class ListUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance.post(new RoleEvent.GotoList(ListUrlResolver.this, null))
    }
  }

  private class AddUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance.post(new RoleEvent.GotoAdd(AddUrlResolver.this, null))
    }
  }

  private class EditUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      val roleId = new UrlTokenizer(params(0)).getInt
      val roleService = ApplicationContextUtil.getSpringBean(classOf[RoleService])
      val role = roleService.findById(roleId, AppContext.getAccountId)
      EventBusFactory.getInstance.post(new RoleEvent.GotoEdit(EditUrlResolver.this, role))
    }
  }

  private class PreviewUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      val roleId = new UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance.post(new RoleEvent.GotoRead(PreviewUrlResolver.this, roleId))
    }
  }

}
