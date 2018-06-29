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
package com.mycollab.module.project.service.impl

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.ModuleNameConstants
import com.mycollab.core.cache.CleanCache
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.MessageMapper
import com.mycollab.module.project.dao.MessageMapperExt
import com.mycollab.module.project.domain.Message
import com.mycollab.module.project.domain.SimpleMessage
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria
import com.mycollab.module.project.esb.DeleteProjectMessageEvent
import com.mycollab.module.project.service.MessageService
import com.mycollab.module.project.service.ProjectActivityStreamService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "title", extraFieldName = "projectid")
class MessageServiceImpl(private val messageMapper: MessageMapper,
                         private val messageMapperExt: MessageMapperExt,
                         private val asyncEventBus: AsyncEventBus) : DefaultService<Int, Message, MessageSearchCriteria>(), MessageService {

    override val crudMapper: ICrudGenericDAO<Int, Message>
        get() = messageMapper as ICrudGenericDAO<Int, Message>

    override val searchMapper: ISearchableDAO<MessageSearchCriteria>
        get() = messageMapperExt

    @CleanCache
    fun postDirtyUpdate(sAccountId: Int?) {
        asyncEventBus.post(CleanCacheEvent(sAccountId, arrayOf(ProjectActivityStreamService::class.java)))
    }

    override fun massRemoveWithSession(items: List<Message>, username: String?, sAccountId: Int) {
        super.massRemoveWithSession(items, username, sAccountId)
        val event = DeleteProjectMessageEvent(items.toTypedArray(), username, sAccountId)
        asyncEventBus.post(event)
    }

    override fun findById(messageId: Int, sAccountId: Int) =
         messageMapperExt.findMessageById(messageId)

    companion object {

        init {
            ClassInfoMap.put(MessageServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.MESSAGE))
        }
    }
}
