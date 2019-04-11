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
package com.mycollab.module.project.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.ICrudService
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.project.domain.Component
import com.mycollab.module.project.domain.SimpleTicketRelation
import com.mycollab.module.project.domain.TicketRelation
import com.mycollab.module.project.domain.Version

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface TicketRelationService : ICrudService<Int, TicketRelation>, IService {

    fun saveAffectedVersionsOfTicket(ticketId: Int, ticketType: String, versions: List<Version>?)

    fun saveFixedVersionsOfTicket(ticketId: Int, ticketType: String, versions: List<Version>?)

    fun saveComponentsOfTicket(ticketId: Int, ticketType: String, components: List<Component>?)

    fun updateAffectedVersionsOfTicket(ticketId: Int, ticketType: String, versions: List<Version>?)

    fun updateFixedVersionsOfTicket(ticketId: Int, ticketType: String, versions: List<Version>?)

    fun updateComponentsOfTicket(ticketId: Int, ticketType: String, components: List<Component>?)

    fun findRelatedTickets(ticketId:Int, ticketType:String): List<SimpleTicketRelation>

    fun removeRelationsByRel(ticketId: Int, ticketType: String, rel:String)
}
