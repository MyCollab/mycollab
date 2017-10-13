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
package com.mycollab.module.tracker.service.impl

import com.mycollab.module.tracker.dao.BugRelatedItemMapper
import com.mycollab.module.tracker.domain.*
import com.mycollab.module.tracker.service.BugRelatedItemService
import org.apache.commons.collections.CollectionUtils
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class BugRelatedItemServiceImpl(private val bugRelatedItemMapper: BugRelatedItemMapper) : BugRelatedItemService {

    override fun saveAffectedVersionsOfBug(bugId: Int, versions: List<Version>) {
        insertAffectedVersionsOfBug(bugId, versions)
    }

    private fun insertAffectedVersionsOfBug(bugId: Int, versions: List<Version>) {
        versions.forEach {
            val relatedItem = BugRelatedItem()
            relatedItem.bugid = bugId
            relatedItem.typeid = it.id
            relatedItem.type = SimpleRelatedBug.AFFVERSION
            bugRelatedItemMapper.insert(relatedItem)
        }
    }

    override fun saveFixedVersionsOfBug(bugId: Int, versions: List<Version>) {
        insertFixedVersionsOfBug(bugId, versions)
    }

    private fun insertFixedVersionsOfBug(bugId: Int?, versions: List<Version>) {
        versions.forEach {
            val relatedItem = BugRelatedItem()
            relatedItem.bugid = bugId
            relatedItem.typeid = it.id
            relatedItem.type = SimpleRelatedBug.FIXVERSION
            bugRelatedItemMapper.insert(relatedItem)
        }
    }

    override fun saveComponentsOfBug(bugId: Int, components: List<Component>) {
        insertComponentsOfBug(bugId, components)
    }

    private fun insertComponentsOfBug(bugId: Int, components: List<Component>) {
        components.forEach {
            val relatedItem = BugRelatedItem()
            relatedItem.bugid = bugId
            relatedItem.typeid = it.id
            relatedItem.type = SimpleRelatedBug.COMPONENT
            bugRelatedItemMapper.insert(relatedItem)
        }
    }

    private fun deleteTrackerBugRelatedItem(bugId: Int?, type: String) {
        val ex = BugRelatedItemExample()
        ex.createCriteria().andBugidEqualTo(bugId).andTypeEqualTo(type)

        bugRelatedItemMapper.deleteByExample(ex)
    }


    override fun updateAffectedVersionsOfBug(bugId: Int, versions: List<Version>) {
        deleteTrackerBugRelatedItem(bugId, SimpleRelatedBug.AFFVERSION)
        if (CollectionUtils.isNotEmpty(versions)) {
            insertAffectedVersionsOfBug(bugId, versions)
        }
    }

    override fun updateFixedVersionsOfBug(bugId: Int, versions: List<Version>) {
        deleteTrackerBugRelatedItem(bugId, SimpleRelatedBug.FIXVERSION)
        if (CollectionUtils.isNotEmpty(versions)) {
            insertFixedVersionsOfBug(bugId, versions)
        }
    }

    override fun updateComponentsOfBug(bugId: Int, components: List<Component>) {
        deleteTrackerBugRelatedItem(bugId, SimpleRelatedBug.COMPONENT)
        if (CollectionUtils.isNotEmpty(components)) {
            insertComponentsOfBug(bugId, components)
        }
    }
}
