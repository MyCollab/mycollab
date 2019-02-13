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
package com.mycollab.module.tracker.service.impl

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.project.i18n.OptionI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.*
import com.mycollab.module.tracker.dao.RelatedBugMapper
import com.mycollab.module.tracker.dao.RelatedBugMapperExt
import com.mycollab.module.tracker.domain.RelatedBug
import com.mycollab.module.tracker.domain.RelatedBugExample
import com.mycollab.module.tracker.domain.SimpleRelatedBug
import com.mycollab.module.tracker.service.BugRelationService
import com.mycollab.module.tracker.service.BugService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
class BugRelationServiceImpl(private val relatedBugMapper: RelatedBugMapper,
                             private val relatedBugMapperExt: RelatedBugMapperExt,
                             private val bugService: BugService) : DefaultCrudService<Int, RelatedBug>(), BugRelationService {

    override val crudMapper: ICrudGenericDAO<Int, RelatedBug>
        get() = relatedBugMapper as ICrudGenericDAO<Int, RelatedBug>

    override fun saveWithSession(record: RelatedBug, username: String?): Int {
        val bugId = record.bugid
        if (BugRelation.Duplicated.name == record.relatetype) {
            val bug = bugService.findById(bugId, 0)
            if (bug != null) {
                bug.status = StatusI18nEnum.Resolved.name
                bug.resolution = BugResolution.Duplicate.name
                bugService.updateSelectiveWithSession(bug, username)
            }
        }
        return super.saveWithSession(record, username)
    }

    override fun removeDuplicatedBugs(bugId: Int): Int {
        val ex = RelatedBugExample()
        ex.createCriteria().andBugidEqualTo(bugId).andRelatetypeEqualTo(BugRelation.Duplicated.name)
        return relatedBugMapper.deleteByExample(ex)
    }

    override fun findRelatedBugs(bugId: Int): List<SimpleRelatedBug> = relatedBugMapperExt.findRelatedBugs(bugId)
}
