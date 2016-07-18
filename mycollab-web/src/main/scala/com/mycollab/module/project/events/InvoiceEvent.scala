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
package com.mycollab.module.project.events

import com.mycollab.events.ApplicationEvent
import com.mycollab.module.project.domain.SimpleInvoice

/**
  * @author MyCollab Ltd
  * @since 5.2.10
  */
object InvoiceEvent {

  class GotoList(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class NewInvoiceAdded(source: AnyRef, data: SimpleInvoice) extends ApplicationEvent(source, data) {}

  class InvoiceUpdateAdded(source: AnyRef, data: SimpleInvoice) extends ApplicationEvent(source, data) {}

  class InvoiceDelete(source: AnyRef, data: SimpleInvoice) extends ApplicationEvent(source, data) {}

  class DisplayInvoiceView(source: AnyRef, data: SimpleInvoice) extends ApplicationEvent(source, data) {}

}
