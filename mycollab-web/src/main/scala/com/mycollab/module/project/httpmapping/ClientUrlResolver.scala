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
package com.mycollab.module.project.httpmapping

import com.mycollab.common.UrlTokenizer
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.event.ClientEvent
import com.mycollab.module.project.event.ClientEvent.{GotoAdd, GotoEdit, GotoRead}

/**
  * @author MyCollab Ltd
  * @since 5.2.9
  */
class ClientUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)

  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new ClientEvent.GotoList(this, null))
    }
  }

  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val clientId = token.getInt
      EventBusFactory.getInstance().post(new GotoRead(this, clientId))
    }
  }

  private class AddUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new GotoAdd(this, null))
    }
  }

  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val clientId = token.getInt
      EventBusFactory.getInstance().post(new GotoEdit(this, clientId))
    }
  }

  protected override def handlePage(params: String*) {
    EventBusFactory.getInstance().post(new ClientEvent.GotoList(this, null))
  }
}
