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
package com.mycollab.mybatis.plugin.ext

import com.mycollab.core.utils.DateTimeUtils
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes

import java.sql.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@MappedJdbcTypes(JdbcType.TIMESTAMP)
class DateTypeHandler : BaseTypeHandler<LocalDateTime>() {

    @Throws(SQLException::class)
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: LocalDateTime, jdbcType: JdbcType) {
        val date = DateTimeUtils.convertDateTimeToUTC(parameter)
        ps.setTimestamp(i, Timestamp(date.second * 1000L))
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnName: String): LocalDateTime? {
        val sqlTimestamp = rs.getTimestamp(columnName)
        return if (sqlTimestamp != null) {
            DateTimeUtils.convertTimeFromUTCToSystemTimezone(sqlTimestamp.time)
        } else null
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): LocalDateTime? {
        val sqlTimestamp = rs.getTimestamp(columnIndex)
        return if (sqlTimestamp != null) {
            DateTimeUtils.convertTimeFromUTCToSystemTimezone(sqlTimestamp.time)
        } else null
    }

    // TODO
    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): LocalDateTime? {
        val sqlTimestamp = cs.getTimestamp(columnIndex)
        return if (sqlTimestamp != null) {
            LocalDateTime.now()
        } else null
    }
}
