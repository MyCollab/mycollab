/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.TableViewField;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.user.AdminTypeConstants;
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.reporting.CustomizeReportOutputWindow;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    protected Set<TableViewField> getDefaultColumns() {
        return new HashSet<>(Arrays.asList(UserTableFieldDef.displayName, UserTableFieldDef.roleName,
                UserTableFieldDef.email, UserTableFieldDef.birthday,
                UserTableFieldDef.officePhone, UserTableFieldDef.homePhone, UserTableFieldDef.company));
    }

    @Override
    protected Set<TableViewField> getAvailableColumns() {
        return new HashSet<>(Arrays.asList(UserTableFieldDef.displayName, UserTableFieldDef.roleName,
                UserTableFieldDef.email, UserTableFieldDef.birthday,
                UserTableFieldDef.officePhone, UserTableFieldDef.homePhone, UserTableFieldDef.company));
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[]{"John Adams", "Administrator", "john.adam@mycollab.com", UserUIContext.formatDate(
                LocalDateTime.of(1979, 3, 13, 0, 0, 0)), "11111111", "11111111", "MyCollab"};
    }
}
