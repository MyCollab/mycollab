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
package com.mycollab.db.persistence;

import com.mycollab.core.MyCollabException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.Alias;
import org.mybatis.scripting.velocity.SQLScriptSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@Alias("velocity")
public class VelocityDriverDeclare implements LanguageDriver {
    private static final String TOTAL_COUNT_EXPR_MACRO;
    private static final String SELECT_EXPR_MACRO;

    static {
        TOTAL_COUNT_EXPR_MACRO = loadResource("totalCountExpr");
        SELECT_EXPR_MACRO = loadResource("selectExpr");
    }

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterTypeClass) {
        if (parameterTypeClass == null) {
            parameterTypeClass = Object.class;
        }
        String templateStr = TOTAL_COUNT_EXPR_MACRO + " " + SELECT_EXPR_MACRO + " " + script.getNode().getTextContent();

        return new SQLScriptSource(configuration, templateStr, parameterTypeClass);
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterTypeClass) {
        if (parameterTypeClass == null) {
            parameterTypeClass = Object.class;
        }
        String templateStr = new StringBuilder().append(SELECT_EXPR_MACRO)
                .append(" ").append(TOTAL_COUNT_EXPR_MACRO).append(" ")
                .append(script).toString();
        return new SQLScriptSource(configuration, templateStr,
                parameterTypeClass);
    }

    private static String loadResource(String id) {
        try (InputStream inputStream = VelocityDriverDeclare.class.getResourceAsStream(id)) {
            if (inputStream == null) {
                throw new MyCollabException("Can not load resource id " + id);
            } else {
                StringBuffer buf = new StringBuffer();
                int chBug;
                while ((chBug = inputStream.read()) != -1) {
                    buf.append((char) chBug);
                }

                return buf.toString();
            }
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }
}