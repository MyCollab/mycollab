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
package com.mycollab.shell.events

import com.mycollab.eventmanager.ApplicationEvent

/**
  * @author MyCollab Ltd.
  * @since 5.0.5
  */
object ShellEvent {

  class NotifyErrorEvent(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class RefreshPage(source: AnyRef) extends ApplicationEvent(source, null) {}

  class GotoMainPage(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class LogOut(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoSetupPage(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoProjectModule(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoCrmModule(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoFileModule(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoUserAccountModule(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoConsolePage(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoForgotPasswordPage(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class NewNotification(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class AddQueryParam(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

}
