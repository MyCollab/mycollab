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
package com.mycollab.shell.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ShellEvent {
    class NotifyErrorEvent(source: Any, val data: Any?) : ApplicationEvent(source)

    class RefreshPage(source: Any) : ApplicationEvent(source)

    class GotoMainPage(source: Any, val data: Any?) : ApplicationEvent(source)

    class LogOut(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoProjectModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoCrmModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoFileModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoUserAccountModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoForgotPasswordPage(source: Any, val data: Any?) : ApplicationEvent(source)

    class NewNotification(source: Any, val data: Any?) : ApplicationEvent(source)

    class AddQueryParam(source: Any, val data: Any?) : ApplicationEvent(source)
}