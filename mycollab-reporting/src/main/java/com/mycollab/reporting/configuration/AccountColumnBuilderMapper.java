/**
 * This file is part of mycollab-reporting.
 *
 * mycollab-reporting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-reporting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-reporting.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.reporting.configuration;

import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.User;
import com.mycollab.reporting.ColumnBuilderClassMapper;
import com.mycollab.reporting.expression.DateExpression;
import com.mycollab.reporting.expression.PrimaryTypeFieldExpression;
import com.mycollab.reporting.generator.ComponentBuilderGenerator;
import com.mycollab.reporting.generator.HyperlinkBuilderGenerator;
import com.mycollab.reporting.generator.SimpleExpressionBuilderGenerator;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@Component
public class AccountColumnBuilderMapper implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        ColumnBuilderClassMapper.put(SimpleUser.class, buildUserMap());
        ColumnBuilderClassMapper.put(SimpleRole.class, buildRoleMap());
    }

    private Map<String, ComponentBuilderGenerator> buildUserMap() {
        Map<String, ComponentBuilderGenerator> map = new HashMap<>();

        DRIExpression<String> userNameExpr = new PrimaryTypeFieldExpression<>(SimpleUser.Field.displayName.name());
        DRIExpression<String> userHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String username = reportParameters.getFieldValue(User.Field.username.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, username);
            }
        };
        map.put(User.Field.username.name(), new HyperlinkBuilderGenerator(userNameExpr, userHrefExpr));

        DRIExpression<String> roleNameExpr = new PrimaryTypeFieldExpression<>(SimpleUser.Field.roleName.name());
        DRIExpression<String> roleHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer roleId = reportParameters.getFieldValue(SimpleUser.Field.roleid.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return AccountLinkGenerator.generatePreviewFullRoleLink(siteUrl, roleId);
            }
        };
        map.put(SimpleUser.Field.roleid.name(), new HyperlinkBuilderGenerator(roleNameExpr, roleHrefExpr));
        map.put(User.Field.dateofbirth.name(), new SimpleExpressionBuilderGenerator(new DateExpression(User.Field.dateofbirth.name())));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildRoleMap() {
        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> roleNameExpr = new PrimaryTypeFieldExpression<>(Role.Field.rolename.name());
        DRIExpression<String> roleHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer roleId = reportParameters.getFieldValue(Role.Field.id.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return AccountLinkGenerator.generatePreviewFullRoleLink(siteUrl, roleId);
            }
        };
        map.put(Role.Field.rolename.name(), new HyperlinkBuilderGenerator(roleNameExpr, roleHrefExpr));
        return map;
    }
}
