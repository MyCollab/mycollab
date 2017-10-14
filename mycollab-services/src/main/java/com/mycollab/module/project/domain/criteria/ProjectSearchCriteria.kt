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
package com.mycollab.module.project.domain.criteria

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.db.query.*

import java.util.Arrays

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectSearchCriteria : SearchCriteria() {

    var projectKeys: SetSearchField<Int>? = null
    var projectStatuses: SetSearchField<String>? = null
    var involvedMember: StringSearchField? = null
    var projectName: StringSearchField? = null
    var accountId: NumberSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField
        val p_template = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_NAME,
                NumberParam("template", "m_prj_project", "istemplate"))

        @JvmField
        val p_name = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_NAME,
                StringParam("name", "m_prj_project", "name"))

        @JvmField
        val p_startdate = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_START_DATE,
                DateParam("startdate", "m_prj_project", "planStartDate"))

        @JvmField
        val p_enddate = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_END_DATE,
                DateParam("enddate", "m_prj_project", "planEndDate"))

        @JvmField
        val p_createdtime = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createdtime", "m_prj_project", "createdTime"))

        @JvmField
        val p_status = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_STATUS,
                StringListParam("status", "m_prj_project", "projectStatus", Arrays.asList(OptionI18nEnum.StatusI18nEnum.Open.name,
                        OptionI18nEnum.StatusI18nEnum.Closed.name, OptionI18nEnum.StatusI18nEnum.Archived.name)))
    }
}
