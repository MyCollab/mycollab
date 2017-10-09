package com.mycollab.common.service.impl

import com.mycollab.common.dao.OptionValMapper
import com.mycollab.common.dao.TimelineTrackingCachingMapper
import com.mycollab.common.dao.TimelineTrackingMapper
import com.mycollab.common.domain.*
import com.mycollab.common.i18n.OptionI18nEnum
import com.mycollab.common.service.OptionValService
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.cache.CacheKey
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n.OptionI18nEnum.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

import javax.sql.DataSource
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.GregorianCalendar

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
@Service
class OptionValServiceImpl : DefaultCrudService<Int, OptionVal>(), OptionValService {
    @Autowired
    private val optionValMapper: OptionValMapper? = null

    @Autowired
    private val timelineTrackingMapper: TimelineTrackingMapper? = null

    @Autowired
    private val timelineTrackingCachingMapper: TimelineTrackingCachingMapper? = null

    @Autowired
    private val dataSource: DataSource? = null

    override val crudMapper: ICrudGenericDAO<Int, OptionVal>
        get() = optionValMapper as ICrudGenericDAO<Int, OptionVal>

    override fun findOptionVals(type: String, projectId: Int?, sAccountId: Int?): List<OptionVal> {
        val ex = OptionValExample()
        ex.createCriteria().andTypeEqualTo(type).andSaccountidEqualTo(sAccountId).andExtraidEqualTo(projectId)
        ex.orderByClause = "orderIndex ASC"
        ex.isDistinct = true

        return optionValMapper!!.selectByExampleWithBLOBs(ex)
    }

    override fun findOptionValsExcludeClosed(type: String, projectId: Int?, @CacheKey sAccountId: Int?): List<OptionVal> {
        val ex = OptionValExample()
        ex.createCriteria().andTypeEqualTo(type).andTypevalNotEqualTo(OptionI18nEnum.StatusI18nEnum.Closed.name)
                .andSaccountidEqualTo(sAccountId).andExtraidEqualTo(projectId)
        ex.orderByClause = "orderIndex ASC"
        ex.isDistinct = true

        return optionValMapper!!.selectByExampleWithBLOBs(ex)
    }

    override fun saveWithSession(record: OptionVal, username: String?): Int {
        checkSaveOrUpdateValid(record)
        return super.saveWithSession(record, username)
    }

    private fun checkSaveOrUpdateValid(record: OptionVal) {
        val typeVal = record.typeval
        if (java.lang.Boolean.TRUE == record.isdefault) {
            val ex = OptionValExample()
            ex.createCriteria().andTypeEqualTo(record.type).andTypevalEqualTo(typeVal)
                    .andFieldgroupEqualTo(record.fieldgroup)
                    .andSaccountidEqualTo(record.saccountid)
            if (optionValMapper!!.countByExample(ex) > 0) {
                throw UserInvalidInputException("There is already column name " + typeVal)
            }
        } else {
            val ex = OptionValExample()
            ex.createCriteria().andTypeEqualTo(record.type).andTypevalEqualTo(typeVal)
                    .andFieldgroupEqualTo(record.fieldgroup).andSaccountidEqualTo(record
                    .saccountid).andIsdefaultEqualTo(java.lang.Boolean.FALSE)
            if (optionValMapper!!.countByExample(ex) > 0) {
                throw UserInvalidInputException("There is already column name " + typeVal)
            }
        }
    }

    override fun updateWithSession(record: OptionVal, username: String?): Int {
        if (java.lang.Boolean.FALSE == record.isdefault) {
            val timelineTrackingExample = TimelineTrackingExample()
            timelineTrackingExample.createCriteria().andTypeEqualTo(record.type).andFieldvalEqualTo(record.typeval)
                    .andFieldgroupEqualTo(record.fieldgroup).andExtratypeidEqualTo(record.extraid)
            val timelineTracking = TimelineTracking()
            timelineTracking.fieldval = record.typeval
            timelineTrackingMapper!!.updateByExampleSelective(timelineTracking, timelineTrackingExample)

            val timelineTrackingCachingExample = TimelineTrackingCachingExample()
            timelineTrackingCachingExample.createCriteria().andTypeEqualTo(record.type).andFieldvalEqualTo(record.typeval).andFieldgroupEqualTo(record.fieldgroup).andExtratypeidEqualTo(record.extraid)
            val timelineTrackingCaching = TimelineTrackingCaching()
            timelineTrackingCaching.fieldval = record.typeval
            timelineTrackingCachingMapper!!.updateByExampleSelective(timelineTrackingCaching,
                    timelineTrackingCachingExample)
        }
        return super.updateWithSession(record, username)
    }

    override fun massUpdateOptionIndexes(mapIndexes: List<Map<String, Int>>, sAccountId: Int?) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.batchUpdate("UPDATE `m_options` SET `orderIndex`=? WHERE `id`=?", object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(preparedStatement: PreparedStatement, i: Int) {
                preparedStatement.setInt(1, mapIndexes[i]["index"]!!)
                preparedStatement.setInt(2, mapIndexes[i]["id"]!!)
            }

            override fun getBatchSize(): Int {
                return mapIndexes.size
            }
        })
    }

    override fun isExistedOptionVal(type: String, typeVal: String, fieldGroup: String, projectId: Int?, sAccountId: Int?): Boolean {
        val ex = OptionValExample()
        ex.createCriteria().andTypeEqualTo(type).andTypevalEqualTo(typeVal).andFieldgroupEqualTo(fieldGroup)
                .andSaccountidEqualTo(sAccountId).andExtraidEqualTo(projectId)
        return optionValMapper!!.countByExample(ex) > 0
    }

    override fun createDefaultOptions(sAccountId: Int?) {
        val option = OptionVal()
        option.createdtime = GregorianCalendar().time
        option.isdefault = true
        option.saccountid = sAccountId
        option.type = ProjectTypeConstants.TASK
        option.typeval = OptionI18nEnum.StatusI18nEnum.Open.name
        option.color = "fdde86"
        option.fieldgroup = "status"
        saveWithSession(option, null)

        option.typeval = OptionI18nEnum.StatusI18nEnum.InProgress.name
        option.id = null
        saveWithSession(option, null)

        option.typeval = OptionI18nEnum.StatusI18nEnum.Archived.name
        option.id = null
        saveWithSession(option, null)

        option.typeval = OptionI18nEnum.StatusI18nEnum.Closed.name
        option.id = null
        saveWithSession(option, null)

        option.typeval = OptionI18nEnum.StatusI18nEnum.Pending.name
        option.id = null
        saveWithSession(option, null)

        option.type = ProjectTypeConstants.MILESTONE
        option.typeval = MilestoneStatus.Closed.name
        option.id = null
        saveWithSession(option, null)

        option.typeval = MilestoneStatus.InProgress.name
        option.id = null
        saveWithSession(option, null)

        option.typeval = MilestoneStatus.Future.name
        option.id = null
        saveWithSession(option, null)
    }
}
