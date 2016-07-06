/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.esb

import javax.annotation.{PostConstruct, PreDestroy}

import com.google.common.eventbus.AsyncEventBus
import org.springframework.beans.factory.annotation.Autowired

/**
  * @author MyCollab Ltd
  * @since 5.1.0
  */
class GenericCommand {
  @Autowired protected val asyncEventBus: AsyncEventBus = null

  @PostConstruct
  def registerHandler(): Unit = {
    asyncEventBus.register(this)
  }

  @PreDestroy
  def unregisterHandler(): Unit = {
    asyncEventBus.unregister(this)
  }
}
