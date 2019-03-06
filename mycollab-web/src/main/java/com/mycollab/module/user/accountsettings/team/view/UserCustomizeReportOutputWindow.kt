/**
 * Copyright © MyCollab
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view

import com.google.common.collect.Sets.newHashSet
import com.mycollab.common.TableViewField
import com.mycollab.db.query.VariableInjector
import com.mycollab.module.user.AdminTypeConstants
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef.birthday
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef.company
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef.displayName
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef.email
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef.homePhone
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef.officePhone
import com.mycollab.module.user.accountsettings.fielddef.UserTableFieldDef.roleName
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.mycollab.module.user.service.UserService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.reporting.CustomizeReportOutputWindow
import java.time.LocalDate

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
class UserCustomizeReportOutputWindow(variableInjector: VariableInjector<UserSearchCriteria>) :
        CustomizeReportOutputWindow<UserSearchCriteria, SimpleUser>(AdminTypeConstants.USER,
                UserUIContext.getMessage(UserI18nEnum.LIST), SimpleUser::class.java,
                AppContextUtil.getSpringBean(UserService::class.java), variableInjector) {

    override fun getDefaultColumns(): Set<TableViewField> =
            newHashSet(displayName, roleName, email, birthday, officePhone, homePhone, company)

    override fun getAvailableColumns(): Set<TableViewField> =
            newHashSet(displayName, roleName, email, birthday, officePhone, homePhone, company)

    override fun getSampleMap(): Map<String, String> = mapOf(
            displayName.field to "John Adams",
            roleName.field to "Administrator",
            email.field to "john.adams@mycollab.com",
            birthday.field to UserUIContext.formatDate(LocalDate.of(1979, 3, 13)),
            officePhone.field to "11111111",
            homePhone.field to "11111111",
            company.field to "MyCollab"
    )
}
