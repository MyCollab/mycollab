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
package com.mycollab.db.persistence

import com.mycollab.core.MyCollabException
import org.apache.ibatis.executor.parameter.ParameterHandler
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.scripting.LanguageDriver
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.type.Alias
import org.mybatis.scripting.velocity.SQLScriptSource
import java.io.IOException

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@Alias("velocity")
class VelocityDriverDeclare : LanguageDriver {

    override fun createParameterHandler(mappedStatement: MappedStatement, parameterObject: Any, boundSql: BoundSql): ParameterHandler {
        return DefaultParameterHandler(mappedStatement, parameterObject, boundSql)
    }

    override fun createSqlSource(configuration: Configuration, script: XNode, parameterTypeClass: Class<*>?): SqlSource {
        var parameterTypeClass = parameterTypeClass
        if (parameterTypeClass == null) {
            parameterTypeClass = Any::class.java
        }
        val templateStr = "$TOTAL_COUNT_EXPR_MACRO $SELECT_EXPR_MACRO ${script.node.textContent}"

        return SQLScriptSource(configuration, templateStr, parameterTypeClass)
    }

    override fun createSqlSource(configuration: Configuration, script: String, parameterTypeClass: Class<*>?): SqlSource {
        var parameterTypeClass = parameterTypeClass
        if (parameterTypeClass == null) {
            parameterTypeClass = Any::class.java
        }
        val templateStr = "$SELECT_EXPR_MACRO $TOTAL_COUNT_EXPR_MACRO $script"
        return SQLScriptSource(configuration, templateStr, parameterTypeClass)
    }

    companion object {
        private val TOTAL_COUNT_EXPR_MACRO: String
        private val SELECT_EXPR_MACRO: String

        init {
            TOTAL_COUNT_EXPR_MACRO = loadResource("totalCountExpr")
            SELECT_EXPR_MACRO = loadResource("selectExpr")
        }

        private fun loadResource(id: String): String {
            try {
                VelocityDriverDeclare::class.java.getResourceAsStream(id).use { inputStream ->
                    if (inputStream == null) {
                        throw MyCollabException("Can not load resource id $id")
                    } else {
                        val buf = StringBuffer()
                        var chBug = inputStream.read()
                        while (chBug != -1) {
                            buf.append(chBug.toChar())
                            chBug = inputStream.read()
                        }

                        return buf.toString()
                    }
                }
            } catch (e: IOException) {
                throw MyCollabException(e)
            }

        }
    }
}