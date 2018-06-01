package com.mycollab.common.service.impl

import com.mycollab.common.dao.NotificationItemMapper
import com.mycollab.common.domain.NotificationItem
import com.mycollab.common.domain.NotificationItemExample
import com.mycollab.common.service.NotificationItemService
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import org.joda.time.DateTime
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.PreparedStatement
import javax.sql.DataSource

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Service
open class NotificationItemServiceImpl(private val notificationItemMapper: NotificationItemMapper,
                                  private val dataSource: DataSource) : DefaultCrudService<Int, NotificationItem>(), NotificationItemService {

    override val crudMapper: ICrudGenericDAO<Int, NotificationItem>
        get() = notificationItemMapper as ICrudGenericDAO<Int, NotificationItem>

    override fun batchInsertItems(notificationUsers: List<String>, module: String, type: String, typeId: String, messages: List<String>, sAccountId: Int) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        val now = DateTimeUtils.convertDateTimeToUTC(DateTime.now().toDate())
        jdbcTemplate.batchUpdate("INSERT INTO `m_notification_item`(`notificationUser`, `module`, `type`, `typeId`, `message`, `createdTime`, `isRead`, `sAccountId`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                object : BatchPreparedStatementSetter {
                    override fun setValues(preparedStatement: PreparedStatement, i: Int) {
                        preparedStatement.setString(1, notificationUsers.get(i))
                        preparedStatement.setString(2, module)
                        preparedStatement.setString(3, type)
                        preparedStatement.setString(4, typeId)
                        preparedStatement.setString(5, messages[i])
                        preparedStatement.setDate(6, Date(now.time))
                        preparedStatement.setBoolean(7, false)
                        preparedStatement.setInt(8, sAccountId)
                    }

                    override fun getBatchSize(): Int = notificationUsers.size
                })
    }

    override fun markNotificationRead(targetUser: String, module: String, type: String, typeId: String) {
        val example = NotificationItemExample()
        example.createCriteria().andNotificationuserEqualTo(targetUser).andModuleEqualTo(module).andTypeEqualTo(type).andTypeidEqualTo(typeId)
        val notificationItem = NotificationItem()
        notificationItem.isread = true
        notificationItemMapper.updateByExampleSelective(notificationItem, example)
    }

    override fun findUnreadNotificationItemsByUser(targetUser: String, sAccountId: Int): List<NotificationItem> {
        val example = NotificationItemExample()
        example.createCriteria().andNotificationuserEqualTo(targetUser).andIsreadEqualTo(false)
        return notificationItemMapper.selectByExample(example)
    }
}