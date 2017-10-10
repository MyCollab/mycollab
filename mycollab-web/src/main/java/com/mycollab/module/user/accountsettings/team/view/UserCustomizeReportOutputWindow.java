/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.TableViewField;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.user.AdminTypeConstants;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.vaadin.reporting.CustomizeReportOutputWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class UserCustomizeReportOutputWindow extends CustomizeReportOutputWindow<UserSearchCriteria, SimpleUser> {
    public UserCustomizeReportOutputWindow(VariableInjector<UserSearchCriteria> variableInjector) {
        super(AdminTypeConstants.USER, UserUIContext.getMessage(UserI18nEnum.LIST), SimpleUser.class,
                AppContextUtil.getSpringBean(UserService.class), variableInjector);
    }

    @Override
    protected Collection<TableViewField> getDefaultColumns() {
        return Arrays.asList(UserTableFieldDef.displayName, UserTableFieldDef.roleName,
                UserTableFieldDef.email, UserTableFieldDef.birthday,
                UserTableFieldDef.officePhone, UserTableFieldDef.homePhone, UserTableFieldDef.company);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(UserTableFieldDef.displayName, UserTableFieldDef.roleName,
                UserTableFieldDef.email, UserTableFieldDef.birthday,
                UserTableFieldDef.officePhone, UserTableFieldDef.homePhone, UserTableFieldDef.company);
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[]{"John Adams", "Administrator", "john.adam@mycollab.com", UserUIContext.formatDate(new
                LocalDate(1979, 3, 13).toDate()), "11111111", "11111111", "MyCollab"};
    }
}
