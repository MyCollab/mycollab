package com.mycollab.module.tracker.service.impl


import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.project.i18n.OptionI18nEnum
import com.mycollab.module.tracker.dao.RelatedBugMapper
import com.mycollab.module.tracker.dao.RelatedBugMapperExt
import com.mycollab.module.tracker.domain.RelatedBug
import com.mycollab.module.tracker.domain.RelatedBugExample
import com.mycollab.module.tracker.domain.SimpleRelatedBug
import com.mycollab.module.tracker.service.BugRelationService
import com.mycollab.module.tracker.service.BugService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
class BugRelationServiceImpl : DefaultCrudService<Int, RelatedBug>(), BugRelationService {
    @Autowired
    private val relatedBugMapper: RelatedBugMapper? = null

    @Autowired
    private val relatedBugMapperExt: RelatedBugMapperExt? = null

    @Autowired
    private val bugService: BugService? = null

    override val crudMapper: ICrudGenericDAO<Int, RelatedBug>
        get() = relatedBugMapper as ICrudGenericDAO<Int, RelatedBug>

    override fun saveWithSession(record: RelatedBug, username: String?): Int {
        val bugId = record.bugid
        if (OptionI18nEnum.BugRelation.Duplicated.name == record.relatetype) {
            val bug = bugService!!.findById(bugId, 0)
            if (bug != null) {
                bug.status = OptionI18nEnum.BugStatus.Resolved.name
                bug.resolution = OptionI18nEnum.BugRelation.Duplicated.name
                bugService.updateSelectiveWithSession(bug, username)
            }
        }
        return super.saveWithSession(record, username)
    }

    override fun removeDuplicatedBugs(bugId: Int?): Int {
        val ex = RelatedBugExample()
        ex.createCriteria().andBugidEqualTo(bugId).andRelatetypeEqualTo(OptionI18nEnum.BugRelation.Duplicated.name)
        return relatedBugMapper!!.deleteByExample(ex)
    }

    override fun findRelatedBugs(bugId: Int?): List<SimpleRelatedBug> {
        return relatedBugMapperExt!!.findRelatedBugs(bugId)
    }
}
