/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.shell.events

import com.mycollab.events.ApplicationEvent

/**
 * @author myCollab Ltd
 * @since 5.2.1
 */
object ShellEvent {

    class GotoLoginView(source: AnyRef) extends ApplicationEvent(source, null) {}

    class GotoMainPage(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoCrmModule(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoProjectModule(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoUserAccountModule(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class PushView(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class NavigateBack(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class LogOut(source: AnyRef) extends ApplicationEvent(source, null) {}
}
