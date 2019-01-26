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
package com.mycollab.module.project.view.settings

import com.google.common.collect.Sets.newHashSet
import com.mycollab.common.TableViewField
import com.mycollab.db.query.VariableInjector
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.billingRate
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.memberName
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.numOpenBugs
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.numOpenTasks
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.overtimeRate
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.projectName
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.roleName
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.totalBillableLogTime
import com.mycollab.module.project.fielddef.ProjectMemberTableFieldDef.totalNonBillableLogTime
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.reporting.CustomizeReportOutputWindow

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
class ProjectMemberCustomizeReportOutputWindow(variableInjector: VariableInjector<ProjectMemberSearchCriteria>) :
        CustomizeReportOutputWindow<ProjectMemberSearchCriteria, SimpleProjectMember>(ProjectTypeConstants.MEMBER,
                UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), SimpleProjectMember::class.java,
                AppContextUtil.getSpringBean(ProjectMemberService::class.java), variableInjector) {

    override fun getDefaultColumns(): Set<TableViewField> =
            newHashSet(memberName, roleName, billingRate, overtimeRate)

    override fun getAvailableColumns(): Set<TableViewField> =
            newHashSet(projectName, memberName, roleName, numOpenTasks, numOpenBugs, totalBillableLogTime,
                    totalNonBillableLogTime, billingRate, overtimeRate)

    override fun getSampleMap(): Map<String, String> = mapOf(
            projectName.field to "MyCollab",
            memberName.field to "John Adam",
            roleName.field to "Administrator",
            numOpenTasks.field to "12",
            numOpenBugs.field to "3",
            totalBillableLogTime.field to "40",
            totalNonBillableLogTime.field to "8",
            billingRate.field to "50",
            overtimeRate.field to "70"
    )
}
